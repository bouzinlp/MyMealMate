package com.mymealmate;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import com.mymealmate.Prefs.Unit;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Profile extends Activity {
    private static final String[] _gender;
    private Button _birthDate;
    private OnDateSetListener _dateSetListener;
    private int _day;
    private int _month;
    private int _year;
    MyMealMateDatabase db;

    static {
        _gender = new String[]{"Female", "Male"};
    }

    public Profile() {
        this._year = 1978;
        this._month = 1;
        this._day = 1;
        this._birthDate = null;
        this.db = new MyMealMateDatabase(this);
        this._dateSetListener = new OnDateSetListener() {
            public void onDateSet(DatePicker datePicker, int i, int i2, int i3) {
                Date date = new Date(i - 1900, i2, i3, 0, 0, 0);
                Profile.this._birthDate.setText(new SimpleDateFormat("dd-MMM-yyyy").format(date));
            }
        };
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        requestWindowFeature(1);
        setContentView(R.layout.profile);
        Prefs prefs = new Prefs(this.db, this);
        Spinner spinner = (Spinner) findViewById(R.id.genderSpinner);
        SpinnerAdapter arrayAdapter = new ArrayAdapter(this, 17367048, _gender);
        arrayAdapter.setDropDownViewResource(17367049);
        spinner.setAdapter(arrayAdapter);
        if (prefs.getGender().equals("M")) {
            spinner.setSelection(1);
        } else {
            spinner.setSelection(0);
        }
        this._birthDate = (Button) findViewById(R.id.dateOfBirth);
        this._birthDate.setText(prefs.getDateOfBirth());
        Float valueOf = Float.valueOf(prefs.getHeight());
        if (prefs.getHeightUnit() == Unit.METRIC) {
            ((TextView) findViewById(R.id.height1Label)).setText("cm");
            findViewById(R.id.height1Label).setVisibility(0);
            findViewById(R.id.height1).setVisibility(0);
            findViewById(R.id.height2Label).setVisibility(4);
            findViewById(R.id.height2).setVisibility(4);
            if (valueOf.floatValue() != 0.0f) {
                ((EditText) findViewById(R.id.height1)).setText(valueOf.toString());
            }
        } else {
            ((TextView) findViewById(R.id.height1Label)).setText("ft");
            ((TextView) findViewById(R.id.height2Label)).setText("in");
            findViewById(R.id.height1Label).setVisibility(0);
            findViewById(R.id.height1).setVisibility(0);
            findViewById(R.id.height2Label).setVisibility(0);
            findViewById(R.id.height2).setVisibility(0);
            if (valueOf.floatValue() != 0.0f) {
                ((EditText) findViewById(R.id.height1)).setText(Prefs.cmToFtString(valueOf.floatValue()));
                ((EditText) findViewById(R.id.height2)).setText(Prefs.cmToInchString(valueOf.floatValue()));
            }
        }
        valueOf = Float.valueOf(prefs.getWeight());
        Unit weightUnit = prefs.getWeightUnit();
        EditText editText;
        if (weightUnit == Unit.METRIC) {
            ((TextView) findViewById(R.id.weight1Label)).setText("kg");
            findViewById(R.id.weight1Label).setVisibility(0);
            findViewById(R.id.weight1).setVisibility(0);
            findViewById(R.id.weight2Label).setVisibility(4);
            findViewById(R.id.weight2).setVisibility(4);
            editText = (EditText) findViewById(R.id.weight1);
            editText.setInputType(8194);
            if (valueOf.floatValue() != 0.0f) {
                editText.setText(valueOf.toString());
            }
        } else if (weightUnit == Unit.IMPERIAL) {
            ((TextView) findViewById(R.id.weight1Label)).setText("lbs");
            findViewById(R.id.weight1Label).setVisibility(0);
            findViewById(R.id.weight1).setVisibility(0);
            findViewById(R.id.weight2Label).setVisibility(4);
            findViewById(R.id.weight2).setVisibility(4);
            editText = (EditText) findViewById(R.id.weight1);
            editText.setInputType(2);
            if (valueOf.floatValue() != 0.0f) {
                editText.setText(String.format("%.0f", new Object[]{Float.valueOf(WeightData.KgToLbs(valueOf.floatValue()))}));
            }
        } else {
            ((TextView) findViewById(R.id.weight1Label)).setText("st");
            ((TextView) findViewById(R.id.weight2Label)).setText("lbs");
            findViewById(R.id.weight1Label).setVisibility(0);
            findViewById(R.id.weight1).setVisibility(0);
            findViewById(R.id.weight2Label).setVisibility(0);
            findViewById(R.id.weight2).setVisibility(0);
            editText = (EditText) findViewById(R.id.weight1);
            EditText editText2 = (EditText) findViewById(R.id.weight2);
            editText.setInputType(2);
            editText2.setInputType(2);
            if (valueOf.floatValue() != 0.0f) {
                valueOf = Float.valueOf(WeightData.KgToLbs(valueOf.floatValue()));
                int intValue = new Float(valueOf.floatValue() / 14.0f).intValue();
                float floatValue = valueOf.floatValue();
                float f = (float) intValue;
                editText.setText(String.valueOf(intValue));
                editText2.setText(String.format("%.0f", new Object[]{Float.valueOf(floatValue - (f * 14.0f))}));
            }
        }
        ((ImageButton) findViewById(R.id.infoAboutMetric)).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                DialogActivity.setLayoutId(R.layout.dialog_activity_4);
                Profile.this.startActivity(new Intent(Profile.this, DialogActivity.class));
                Profile.this.overridePendingTransition(R.anim.fade, R.anim.hold);
            }
        });
    }

    protected Dialog onCreateDialog(int i) {
        return i != 0 ? null : new DatePickerDialog(this, this._dateSetListener, this._year, this._month, this._day);
    }

    public void onDateClick(View view) {
        showDialog(0);
    }

    public void onSaveClick(View view) {
        Prefs prefs = new Prefs(this.db, this);
        prefs.setDateOfBirth(((Button) findViewById(R.id.dateOfBirth)).getText().toString());
        if (((Spinner) findViewById(R.id.genderSpinner)).getSelectedItemPosition() == 0) {
            prefs.setGender("F");
        } else {
            prefs.setGender("M");
        }
        String editable = ((EditText) findViewById(R.id.height1)).getText().toString();
        String editable2 = ((EditText) findViewById(R.id.height2)).getText().toString();
        if (prefs.getHeightUnit() == Unit.METRIC) {
            if (editable.length() > 0) {
                prefs.setHeight(Float.valueOf(Float.parseFloat(editable)));
            } else {
                prefs.setHeight(Float.valueOf(0.0f));
            }
        } else if (editable.length() > 0) {
            prefs.setHeight(Float.valueOf(((editable2.length() > 0 ? Float.parseFloat(editable2) : 0.0f) + (Float.parseFloat(editable) * 12.0f)) * 2.54f));
        } else {
            prefs.setHeight(Float.valueOf(0.0f));
        }
        Unit weightUnit = prefs.getWeightUnit();
        String editable3 = ((EditText) findViewById(R.id.weight1)).getText().toString();
        String editable4 = ((EditText) findViewById(R.id.weight2)).getText().toString();
        if (weightUnit == Unit.METRIC) {
            if (editable3.length() > 0) {
                prefs.setWeight(Float.valueOf(Float.parseFloat(editable3)));
            } else {
                prefs.setWeight(Float.valueOf(0.0f));
            }
        } else if (weightUnit == Unit.IMPERIAL) {
            if (editable3.length() > 0) {
                prefs.setWeight(Float.valueOf(WeightData.LbsToKg(Float.parseFloat(editable3))));
            } else {
                prefs.setWeight(Float.valueOf(0.0f));
            }
        } else if (editable3.length() > 0) {
            float parseFloat = Float.parseFloat(editable3);
            Float valueOf = Float.valueOf(0.0f);
            if (editable4.length() > 0) {
                valueOf = Float.valueOf(Float.parseFloat(editable4));
            }
            prefs.setWeight(Float.valueOf(WeightData.LbsToKg(valueOf.floatValue() + (Float.valueOf(parseFloat).floatValue() * 14.0f))));
        } else {
            prefs.setWeight(Float.valueOf(0.0f));
        }
        finish();
    }

    public void onStop() {
        this.db.close();
        super.onStop();
    }
}