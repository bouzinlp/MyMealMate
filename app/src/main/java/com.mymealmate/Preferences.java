package com.mymealmate;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import com.mymealmate.Prefs.Unit;

public class Preferences extends Activity {
    private static final String[] HEIGHT_FORMATS;
    private static final String[] WEIGHT_FORMATS;
    private MyMealMateDatabase db;

    static {
        HEIGHT_FORMATS = new String[]{"Metric - metres", "Imperial - feet & inches"};
        WEIGHT_FORMATS = new String[]{"Metric - kg", "Imperial - lbs", "Imperial - st & lbs"};
    }

    public Preferences() {
        this.db = new MyMealMateDatabase(this);
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        requestWindowFeature(1);
        setContentView(R.layout.preferences);
        Spinner spinner = (Spinner) findViewById(R.id.heightFormat);
        SpinnerAdapter arrayAdapter = new ArrayAdapter(this, 17367048, HEIGHT_FORMATS);
        arrayAdapter.setDropDownViewResource(17367049);
        spinner.setAdapter(arrayAdapter);
        Spinner spinner2 = (Spinner) findViewById(R.id.weightFormat);
        SpinnerAdapter arrayAdapter2 = new ArrayAdapter(this, 17367048, WEIGHT_FORMATS);
        arrayAdapter2.setDropDownViewResource(17367049);
        spinner2.setAdapter(arrayAdapter2);
        Prefs prefs = new Prefs(this.db, this);
        spinner.setSelection(prefs.getHeightUnit().ordinal());
        spinner2.setSelection(prefs.getWeightUnit().ordinal());
    }

    protected void onPause() {
        super.onPause();
        Prefs prefs = new Prefs(this.db, this);
        Unit[] values = Unit.values();
        prefs.setHeightUnit(values[((Spinner) findViewById(R.id.heightFormat)).getSelectedItemPosition()]);
        prefs.setWeightUnit(values[((Spinner) findViewById(R.id.weightFormat)).getSelectedItemPosition()]);
    }

    public void onStop() {
        this.db.close();
        super.onStop();
    }
}