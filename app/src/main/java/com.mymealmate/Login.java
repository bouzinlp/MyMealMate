package com.mymealmate;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import com.mymealmate.WSClient.TestConnection;
import com.mymealmate.WSHelper.IWSCallEventListener;
import java.util.Date;
import org.xmlpull.v1.XmlPullParser;

public class Login extends Activity implements OnClickListener, IWSCallEventListener {
    private MyMealMateDatabase db_;
    private String participantId;
    private ProgressDialog pleaseWait;
    private Prefs prefs;

    public Login() {
        this.participantId = XmlPullParser.NO_NAMESPACE;
        this.pleaseWait = null;
        this.db_ = null;
        this.prefs = null;
    }

    private void goToDiary() {
        WSClient.setParticipantId(this.participantId);
        if (this.prefs.getFirstRun().booleanValue()) {
            CompletionCheck.feedbackChecks(this, this.db_);
        } else {
            CompletionCheck.injectSMS(this, getString(R.string.first_run_message));
            this.prefs.setFirstRun(Boolean.valueOf(true));
        }
        this.db_.close();
        finish();
        startActivity(new Intent(this, Diary.class));
    }

    private void showProgress() {
        if (this.pleaseWait == null) {
            this.pleaseWait = new ProgressDialog(this);
        }
        this.pleaseWait.setMessage(getString(R.string.connecting_server));
        this.pleaseWait.setIndeterminate(true);
        this.pleaseWait.setCancelable(false);
        this.pleaseWait.show();
    }

    private void tryToUploadData() {
        Date date = new Date(this.prefs.getLastUpload());
        Date date2 = new Date();
        if (!date2.after(date)) {
            return;
        }
        if (date.getDate() != date2.getDate() || date.getMonth() != date2.getMonth()) {
            WSHelper wSHelper = new WSHelper();
            wSHelper.setDataUploadedListener(new IWSCallEventListener() {
                public void callFinished(boolean z) {
                    if (z) {
                        Login.this.prefs.setLastUpload(Long.valueOf(new Date().getTime()));
                    }
                }
            });
            wSHelper.uploadAllAsync(this.db_);
        }
    }

    public void callFinished(boolean z) {
        this.pleaseWait.dismiss();
        if (z) {
            new Builder(this).setMessage(getString(R.string.login_proceed)).setIcon(17301659).setPositiveButton("Commit", this).setNegativeButton("Cancel", null).setTitle(R.string.login_succeed).show();
        } else {
            new Builder(this).setMessage(getString(R.string.user_not_found)).setIcon(17301543).setPositiveButton("OK", null).setTitle(R.string.login_failed).show();
        }
    }

    public void onClick(DialogInterface dialogInterface, int i) {
        if (i == -1) {
            this.prefs.setParticipantId(this.participantId);
            goToDiary();
        }
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        requestWindowFeature(1);
        setContentView(R.layout.login);
        ((EditText) findViewById(R.id.uploadAddress)).setText(WSClient.getUrl());
        AlarmSetter.setFeedbackAlarm(this);
        this.db_ = new MyMealMateDatabase(this);
        this.prefs = new Prefs(this.db_, this);
        this.participantId = this.prefs.getParticipantId();
        if (this.participantId.length() <= 0) {
            return;
        }
        if (this.prefs.getRunStandalone().booleanValue() || !this.prefs.getHolidayMode().booleanValue()) {
            this.prefs.getRunStandalone().booleanValue();
            goToDiary();
            return;
        }
        OnClickListener anonymousClass1 = new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                if (i == -1) {
                    Login.this.tryToUploadData();
                }
                Login.this.goToDiary();
            }
        };
        new Builder(this).setMessage(getString(R.string.holiday_mode_on_question)).setIcon(17301543).setPositiveButton("Yes", anonymousClass1).setNegativeButton("No", anonymousClass1).show();
    }

    public void onStandaloneClick(View view) {
        if (((CheckBox) view).isChecked()) {
            this.prefs.setRunStandalone(Boolean.valueOf(true));
            findViewById(R.id.uploadAddress).setEnabled(false);
            findViewById(R.id.loginEdit).setEnabled(false);
            return;
        }
        this.prefs.setRunStandalone(Boolean.valueOf(false));
        findViewById(R.id.uploadAddress).setEnabled(true);
        findViewById(R.id.loginEdit).setEnabled(true);
    }

    public void onStop() {
        this.db_.close();
        super.onStop();
    }

    public void onTestClick(View view) {
        if (this.prefs.getRunStandalone().booleanValue()) {
            this.participantId = "standalone";
            this.prefs.setParticipantId(this.participantId);
            goToDiary();
            return;
        }
        TextView textView = (TextView) findViewById(R.id.loginEdit);
        EditText editText = (EditText) findViewById(R.id.uploadAddress);
        if (textView.getText().length() == 0 || editText.getText().length() == 0) {
            new Builder(this).setMessage(getString(R.string.user_empty)).setIcon(17301543).setPositiveButton("OK", null).setTitle(R.string.login_failed).show();
            return;
        }
        this.participantId = textView.getText().toString();
        WSClient.setUrl(editText.getText().toString());
        showProgress();
        WSHelper wSHelper = new WSHelper(new TestConnection().addProperty("Usernumber", this.participantId));
        wSHelper.setCallFinishedListener(this);
        wSHelper.start();
    }
}