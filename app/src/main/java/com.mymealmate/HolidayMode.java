package com.mymealmate;

import android.app.Activity;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ToggleButton;

public class HolidayMode extends Activity {
    MyMealMateDatabase db_;
    Prefs prefs_;

    public HolidayMode() {
        this.db_ = null;
        this.prefs_ = null;
    }

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        requestWindowFeature(1);
        setContentView(R.layout.holiday_mode);
        this.db_ = new MyMealMateDatabase(this);
        this.prefs_ = new Prefs(this.db_, this);
        ToggleButton toggleButton = (ToggleButton) findViewById(R.id.holidayModeToggleButton);
        if (this.prefs_.getRunStandalone().booleanValue()) {
            toggleButton.setEnabled(false);
        } else {
            toggleButton.setChecked(this.prefs_.getHolidayMode().booleanValue());
        }
        toggleButton.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                HolidayMode.this.prefs_.setHolidayMode(Boolean.valueOf(z));
            }
        });
    }

    public void onStop() {
        this.db_.close();
        super.onStop();
    }
}