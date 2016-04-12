package com.mymealmate;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import com.mymealmate.WSHelper.IWSCallEventListener;
import java.util.Date;
import org.kxml2.kdom.Node;
import org.kxml2.wap.Wbxml;

public class Diagnostics extends Activity implements OnItemClickListener, IWSCallEventListener {
    private String BUILD_NUMBER;
    private final String[] LIST;
    private MyMealMateDatabase db_;
    private ProgressDialog mPleaseWait;
    private Prefs prefs_;

    public Diagnostics() {
        this.LIST = new String[]{"Upload Data", "Copy database to SD", "Build"};
        this.mPleaseWait = null;
        this.db_ = null;
        this.prefs_ = null;
    }

    private void showProgress() {
        if (this.mPleaseWait == null) {
            this.mPleaseWait = new ProgressDialog(this);
        }
        this.mPleaseWait.setMessage(getString(R.string.connecting_server));
        this.mPleaseWait.setIndeterminate(true);
        this.mPleaseWait.setCancelable(false);
        this.mPleaseWait.show();
    }

    private void tryToUploadData() {
        showProgress();
        WSHelper wSHelper = new WSHelper();
        wSHelper.setDataUploadedListener(this);
        wSHelper.uploadAllAsync(this.db_);
    }

    public void callFinished(boolean z) {
        this.mPleaseWait.dismiss();
        if (z) {
            Editor edit = getSharedPreferences("mmmpreferences", 0).edit();
            edit.putLong("LastUpload", new Date().getTime());
            edit.commit();
            Toast.makeText(getApplicationContext(), getString(R.string.uploaddata_succeed), 0).show();
            finish();
            return;
        }
        Toast.makeText(getApplicationContext(), getString(R.string.uploaddata_failed), 0).show();
    }

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        try {
            Context applicationContext = getApplicationContext();
            this.BUILD_NUMBER = applicationContext.getPackageManager().getPackageInfo(applicationContext.getPackageName(), 0).versionName;
        } catch (NameNotFoundException e) {
            this.BUILD_NUMBER = "unknown";
        }
        requestWindowFeature(1);
        setContentView(R.layout.diagnostics);
        ListView listView = (ListView) findViewById(R.id.diagnosticsListView);
        listView.setAdapter(new ArrayAdapter(this, 17367043, this.LIST));
        listView.setOnItemClickListener(this);
        this.db_ = new MyMealMateDatabase(this);
        this.prefs_ = new Prefs(this.db_, this);
    }

    public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
        switch (i) {
            case Node.DOCUMENT /*0*/:
                if (this.prefs_.getHolidayMode().booleanValue()) {
                    OnClickListener anonymousClass1 = new OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                            switch (i) {
                                case -1:
                                    Diagnostics.this.tryToUploadData();
                                default:
                            }
                        }
                    };
                    new Builder(this).setMessage(getString(R.string.holiday_mode_on_question)).setIcon(17301543).setPositiveButton("Yes", anonymousClass1).setNegativeButton("No", anonymousClass1).show();
                    return;
                }
                tryToUploadData();
            case Wbxml.END /*1*/:
                Toast.makeText(getApplicationContext(), this.db_.copyDBtoSD() ? getString(R.string.uploaddata_succeed) : getString(R.string.uploaddata_failed), 0).show();
            case Wbxml.ENTITY /*2*/:
                new Builder(this).setMessage(getString(R.string.diagnostics_build_text) + " " + this.BUILD_NUMBER).setIcon(17301659).setPositiveButton("OK", null).setTitle(R.string.diagnostics_build_title).show();
            default:
        }
    }

    public void onStop() {
        this.db_.close();
        super.onStop();
    }
}