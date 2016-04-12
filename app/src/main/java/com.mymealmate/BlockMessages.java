package com.mymealmate;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TimePicker;

public class BlockMessages extends Activity {
    MyMealMateDatabase db_;
    Prefs prefs_;
    private TimePickerDialog timePickDialog;

    /* renamed from: com.mymealmate.BlockMessages.1 */
    class AnonymousClass1 implements OnCheckedChangeListener {
        private final /* synthetic */ Button val$fb;
        private final /* synthetic */ Button val$tb;

        AnonymousClass1(Button button, Button button2) {
            this.val$fb = button;
            this.val$tb = button2;
        }

        public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
            boolean z2 = false;
            BlockMessages.this.prefs_.setMessageBlockAlways(z);
            this.val$fb.setEnabled(!z);
            Button button = this.val$tb;
            if (!z) {
                z2 = true;
            }
            button.setEnabled(z2);
        }
    }

    private class TimePickHandler implements OnTimeSetListener {
        boolean startFlag;

        TimePickHandler(boolean z) {
            this.startFlag = z;
        }

        public void onTimeSet(TimePicker timePicker, int i, int i2) {
            if (this.startFlag) {
                BlockMessages.this.prefs_.setHourStartMessageBlock(i);
                BlockMessages.this.prefs_.setMinuteStartMessageBlock(i2);
                ((Button) BlockMessages.this.findViewById(R.id.BlockMessageFrom)).setText(new StringBuilder(String.valueOf(String.format("%02d", new Object[]{Integer.valueOf(i)}))).append(":").append(String.format("%02d", new Object[]{Integer.valueOf(i2)})).toString());
            } else {
                BlockMessages.this.prefs_.setHourEndMessageBlock(i);
                BlockMessages.this.prefs_.setMinuteEndMessageBlock(i2);
                ((Button) BlockMessages.this.findViewById(R.id.BlockMessageTo)).setText(new StringBuilder(String.valueOf(String.format("%02d", new Object[]{Integer.valueOf(i)}))).append(":").append(String.format("%02d", new Object[]{Integer.valueOf(i2)})).toString());
            }
            BlockMessages.this.timePickDialog.hide();
        }
    }

    public BlockMessages() {
        this.timePickDialog = null;
        this.db_ = null;
        this.prefs_ = null;
    }

    protected void getDialog(int i, int i2, boolean z) {
        this.timePickDialog = new TimePickerDialog(this, new TimePickHandler(z), i, i2, true);
        this.timePickDialog.show();
    }

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        requestWindowFeature(1);
        setContentView(R.layout.block_messages);
        this.db_ = new MyMealMateDatabase(this);
        this.prefs_ = new Prefs(this.db_, this);
        Button button = (Button) findViewById(R.id.BlockMessageFrom);
        Button button2 = (Button) findViewById(R.id.BlockMessageTo);
        CheckBox checkBox = (CheckBox) findViewById(R.id.blockAlwaysCheck);
        boolean messageBlockAlways = this.prefs_.getMessageBlockAlways();
        checkBox.setChecked(messageBlockAlways);
        button.setEnabled(!messageBlockAlways);
        button2.setEnabled(!messageBlockAlways);
        checkBox.setOnCheckedChangeListener(new AnonymousClass1(button, button2));
        button.setText(new StringBuilder(String.valueOf(String.format("%02d", new Object[]{Integer.valueOf(this.prefs_.getHourStartMessageBlock())}))).append(":").append(String.format("%02d", new Object[]{Integer.valueOf(this.prefs_.getMinuteStartMessageBlock())})).toString());
        button2.setText(new StringBuilder(String.valueOf(String.format("%02d", new Object[]{Integer.valueOf(this.prefs_.getHourEndMessageBlock())}))).append(":").append(String.format("%02d", new Object[]{Integer.valueOf(this.prefs_.getMinuteEndMessageBlock())})).toString());
        button.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                BlockMessages.this.getDialog(BlockMessages.this.prefs_.getHourStartMessageBlock(), BlockMessages.this.prefs_.getMinuteStartMessageBlock(), true);
            }
        });
        button2.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                BlockMessages.this.getDialog(BlockMessages.this.prefs_.getHourEndMessageBlock(), BlockMessages.this.prefs_.getMinuteEndMessageBlock(), false);
            }
        });
    }

    public void onStop() {
        this.db_.close();
        super.onStop();
    }
}