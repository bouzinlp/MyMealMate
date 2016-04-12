package com.mymealmate;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import com.mymealmate.Prefs.Unit;

public class WeightEntry extends Activity implements OnClickListener {
    private OnDateSetListener _dateSetListener;
    private boolean _kg;
    private Prefs _prefs;
    private Float _weight;
    MyMealMateDatabase db;

    /* renamed from: com.mymealmate.WeightEntry.5 */
    class AnonymousClass5 implements DialogInterface.OnClickListener {
        private final /* synthetic */ DateProvider val$dp;
        private final /* synthetic */ EditText val$input0;
        private final /* synthetic */ EditText val$input1;

        AnonymousClass5(EditText editText, EditText editText2, DateProvider dateProvider) {
            this.val$input1 = editText;
            this.val$input0 = editText2;
            this.val$dp = dateProvider;
        }

        public void onClick(DialogInterface dialogInterface, int i) {
            String editable = this.val$input1.getText().toString();
            if (editable.length() == 0) {
                return;
            }
            if (!editable.equals("0") || WeightEntry.this._prefs.getWeightUnit() == Unit.IMPERIAL_ST) {
                if (!editable.equals(WeightEntry.this._kg ? WeightEntry.this._weight.toString() : String.valueOf(WeightData.KgToLbs(WeightEntry.this._weight.floatValue())))) {
                    float parseFloat = Float.parseFloat(editable);
                    if (!WeightEntry.this._kg) {
                        if (WeightEntry.this._prefs.getWeightUnit() == Unit.IMPERIAL_ST) {
                            editable = this.val$input0.getText().toString();
                            if (editable.length() != 0 && !editable.equals("0")) {
                                parseFloat += Float.parseFloat(editable) * 14.0f;
                            } else {
                                return;
                            }
                        }
                        parseFloat = WeightData.LbsToKg(parseFloat);
                    }
                    WeightData.setWeight(WeightEntry.this.db, this.val$dp, parseFloat);
                    dialogInterface.dismiss();
                    WeightEntry.this.finish();
                }
            }
        }
    }

    public WeightEntry() {
        this._dateSetListener = new OnDateSetListener() {
            public void onDateSet(DatePicker datePicker, int i, int i2, int i3) {
                DateProvider instance = DateProvider.getInstance();
                instance.setYear(i);
                instance.setMonth(i2);
                instance.setDate(i3);
                WeightEntry.this.setWeightText();
            }
        };
    }

    private void setWeightText() {
        CharSequence stringBuilder;
        DateProvider instance = DateProvider.getInstance();
        ((TextView) findViewById(R.id.dateTextView)).setText(instance.getFormatedDate());
        ((TextView) findViewById(R.id.touchText)).setVisibility(instance.isToday() ? 0 : 4);
        this._weight = Float.valueOf(WeightData.getActualWeight(this.db, instance.getEndOfDay()));
        TextView textView = (TextView) findViewById(R.id.weightText);
        if (this._kg) {
            stringBuilder = new StringBuilder(String.valueOf(this._weight.toString())).append(" kg").toString();
        } else if (this._prefs.getWeightUnit() == Unit.IMPERIAL_ST) {
            int intValue = new Float(Float.valueOf(WeightData.KgToLbs(this._weight.floatValue())).floatValue() / 14.0f).intValue();
            stringBuilder = String.format("%d st %.0f lbs", new Object[]{Integer.valueOf(intValue), Float.valueOf(r1.floatValue() - (((float) intValue) * 14.0f))});
        } else {
            stringBuilder = new StringBuilder(String.valueOf(String.format("%.0f", new Object[]{Float.valueOf(WeightData.KgToLbs(this._weight.floatValue()))}))).append(" lbs").toString();
        }
        textView.setText(stringBuilder);
    }

    public void onClick(View view) {
        DateProvider instance = DateProvider.getInstance();
        if (instance.isToday()) {
            View linearLayout = new LinearLayout(this);
            View editText = new EditText(this);
            if (this._kg) {
                editText.setInputType(8194);
            } else {
                editText.setInputType(2);
            }
            View editText2 = new EditText(this);
            editText2.setInputType(2);
            editText2.setVisibility(8);
            linearLayout.addView(editText2, new LayoutParams(150, -2));
            View textView = new TextView(this);
            textView.setText("st");
            textView.setPadding(10, 0, 10, 0);
            textView.setVisibility(8);
            linearLayout.addView(textView, new LayoutParams(-2, -2));
            if (this._prefs.getWeightUnit() == Unit.IMPERIAL_ST) {
                editText2.setVisibility(0);
                textView.setVisibility(0);
                Float valueOf = Float.valueOf(WeightData.KgToLbs(this._weight.floatValue()));
                int intValue = new Float(valueOf.floatValue() / 14.0f).intValue();
                float floatValue = valueOf.floatValue();
                float f = (float) intValue;
                editText2.setText(Integer.toString(intValue));
                editText.setText(String.format("%.0f", new Object[]{Float.valueOf(floatValue - (f * 14.0f))}));
            } else {
                editText.setText(this._kg ? this._weight.toString() : String.format("%.0f", new Object[]{Float.valueOf(WeightData.KgToLbs(this._weight.floatValue()))}));
            }
            View textView2 = new TextView(this);
            textView2.setText(this._kg ? "kg" : "lbs");
            textView2.setPadding(10, 0, 10, 0);
            linearLayout.addView(editText, new LayoutParams(150, -2));
            linearLayout.addView(textView2, new LayoutParams(-2, -2));
            linearLayout.setHorizontalGravity(1);
            new Builder(this).setView(linearLayout).setTitle("Enter Weight").setPositiveButton(17039370, new AnonymousClass5(editText, editText2, instance)).setNegativeButton(17039360, null).create().show();
        }
    }

    protected void onCreate(Bundle bundle) {
        boolean z = true;
        super.onCreate(bundle);
        requestWindowFeature(1);
        setContentView(R.layout.weight_entry);
        this.db = new MyMealMateDatabase(this);
        this._prefs = new Prefs(this.db, this);
        if (this._prefs.getWeight() == 0.0f) {
            new Builder(this).setTitle(R.string.weight_entry_title).setMessage(getString(R.string.enter_profile_weight_message)).setIcon(17301543).setPositiveButton(17039370, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    WeightEntry.this.startActivity(new Intent(WeightEntry.this, Profile.class));
                }
            }).setNegativeButton(17039360, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    WeightEntry.this.finish();
                }
            }).show();
        }
        if (this._prefs.getWeightUnit() != Unit.METRIC) {
            z = false;
        }
        this._kg = z;
        ((TextView) findViewById(R.id.dateTextView)).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                WeightEntry.this.showDialog(0);
            }
        });
        findViewById(R.id.weightText).setOnClickListener(this);
    }

    protected Dialog onCreateDialog(int i) {
        DateProvider instance = DateProvider.getInstance();
        if (i != 0) {
            return null;
        }
        return new DatePickerDialog(this, this._dateSetListener, instance.getYear(), instance.getMonth(), instance.getDate());
    }

    public void onDateButtonClick(View view) {
        DateProvider instance = DateProvider.getInstance();
        switch (view.getId()) {
            case R.id.prevDateNavButton /*2131361840*/:
                instance.prevDate();
                break;
            case R.id.nextDateNavButton /*2131361842*/:
                instance.nextDate();
                break;
        }
        setWeightText();
    }

    protected void onPrepareDialog(int i, Dialog dialog) {
        DateProvider instance = DateProvider.getInstance();
        ((DatePickerDialog) dialog).updateDate(instance.getYear(), instance.getMonth(), instance.getDate());
    }

    protected void onResume() {
        super.onResume();
        setWeightText();
    }

    public void onStop() {
        this.db.close();
        super.onStop();
    }
}