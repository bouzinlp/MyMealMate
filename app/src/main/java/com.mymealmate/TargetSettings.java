package com.mymealmate;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Pair;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TableRow;
import android.widget.TextView;
import com.mymealmate.Prefs.Unit;
import com.mymealmate.TargetSettingsData.Cursor;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import org.xmlpull.v1.XmlPullParser;

public class TargetSettings extends Activity {
    private TextView bmiTextView_;
    private int currentGoal;
    private float dailyDeficit_;
    private MyMealMateDatabase db;
    private float maximumMassLoss_;
    private TextView neededCaloriesTextView_;
    private Prefs preferences_;
    private Map<String, Pair<Integer, Float>> targetWeeksAndNecessaryCalories_;
    private ArrayAdapter<String> targetWeightAdapter_;
    private Spinner targetWeightSpinner_;
    private Spinner weeklyTargetSpinner_;
    private final String[] weeklyTargets_;
    private Pair<Integer, Float> weeksWeightLoss_;
    private Map<String, Float> weightLossMap_;

    public TargetSettings() {
        this.db = null;
        this.preferences_ = null;
        this.bmiTextView_ = null;
        this.neededCaloriesTextView_ = null;
        this.weeklyTargetSpinner_ = null;
        this.targetWeightSpinner_ = null;
        this.targetWeightAdapter_ = null;
        this.targetWeeksAndNecessaryCalories_ = new HashMap();
        this.weightLossMap_ = new HashMap();
        this.weeksWeightLoss_ = null;
        this.maximumMassLoss_ = 0.5f;
        this.dailyDeficit_ = 500.0f;
        this.weeklyTargets_ = new String[]{"1lb/0.5kg per week", "1.5lb/0.75kg per week", "2lb/1kg per week"};
        this.currentGoal = 0;
    }

    public static float getGoal(MyMealMateDatabase myMealMateDatabase, long j) {
        float f = 0.0f;
        synchronized (TargetSettings.class) {
            try {
                Prefs prefs = new Prefs(myMealMateDatabase, null);
                float weight = prefs.getWeight();
                float height = prefs.getHeight();
                float palValue = prefs.getPalValue();
                String dateOfBirth = prefs.getDateOfBirth();
                if (!(weight == 0.0f || height == 0.0f || palValue == 0.0f || dateOfBirth.equals(XmlPullParser.NO_NAMESPACE))) {
                    f = (float) (DateProvider.getInstance().getYear() - (new SimpleDateFormat("dd-MMM-yyyy").parse(dateOfBirth, new ParsePosition(0)).getYear() + 1900));
                    boolean equals = prefs.getGender().equals("M");
                    f = equals ? (((weight * 13.7f) + 66.0f) + (height * 5.0f)) - (f * 6.76f) : (((weight * 9.6f) + 655.0f) + (height * 1.8f)) - (f * 4.7f);
                    Cursor byDate = TargetSettingsData.getByDate(myMealMateDatabase, j);
                    if (byDate.getCount() == 0) {
                        byDate.close();
                        f *= palValue;
                    } else {
                        height = byDate.getDailyDeficit().floatValue();
                        byDate.close();
                        f = (f * palValue) - height;
                        if (equals) {
                            if (f < 1500.0f) {
                                f = 1500.0f;
                            }
                        } else if (f < 1200.0f) {
                            f = 1200.0f;
                        }
                    }
                }
            } catch (Throwable th) {
                Class cls = TargetSettings.class;
            }
        }
        return f;
    }

    private void populateData() {
        Float valueOf = Float.valueOf(this.preferences_.getHeight());
        Float valueOf2 = Float.valueOf(this.preferences_.getWeight());
        Unit heightUnit = this.preferences_.getHeightUnit();
        if (heightUnit == Unit.METRIC) {
            findViewById(R.id.heightLabel1).setVisibility(8);
            findViewById(R.id.heightValue1).setVisibility(8);
            ((TextView) findViewById(R.id.heightLabel2)).setText("cm");
            findViewById(R.id.heightLabel2).setVisibility(0);
            findViewById(R.id.heightValue2).setVisibility(0);
            ((TextView) findViewById(R.id.heightValue2)).setText(valueOf.toString());
        } else if (heightUnit == Unit.IMPERIAL) {
            ((TextView) findViewById(R.id.heightLabel1)).setText("ft");
            findViewById(R.id.heightLabel1).setVisibility(0);
            findViewById(R.id.heightValue1).setVisibility(0);
            ((TextView) findViewById(R.id.heightValue1)).setText(Prefs.cmToFtString(valueOf.floatValue()));
            ((TextView) findViewById(R.id.heightLabel2)).setText("in");
            findViewById(R.id.heightLabel2).setVisibility(0);
            findViewById(R.id.heightValue2).setVisibility(0);
            ((TextView) findViewById(R.id.heightValue2)).setText(Prefs.cmToInchString(valueOf.floatValue()));
        } else {
            ((TextView) findViewById(R.id.heightLabel1)).setText("ft");
            findViewById(R.id.heightLabel1).setVisibility(0);
            findViewById(R.id.heightValue1).setVisibility(0);
            ((TextView) findViewById(R.id.heightValue1)).setText(Prefs.cmToFtString(valueOf.floatValue()));
            ((TextView) findViewById(R.id.heightLabel2)).setText("in");
            findViewById(R.id.heightLabel2).setVisibility(0);
            findViewById(R.id.heightValue2).setVisibility(0);
            ((TextView) findViewById(R.id.heightValue2)).setText(Prefs.cmToInchString(valueOf.floatValue()));
        }
        heightUnit = this.preferences_.getWeightUnit();
        if (heightUnit == Unit.METRIC) {
            ((TextView) findViewById(R.id.weightLabel1)).setText("kg");
            findViewById(R.id.weightLabel1).setVisibility(0);
            findViewById(R.id.weightValue1).setVisibility(0);
            ((TextView) findViewById(R.id.weightValue1)).setText(valueOf2.toString());
            findViewById(R.id.weightLabel2).setVisibility(4);
            findViewById(R.id.weightValue2).setVisibility(4);
        } else if (heightUnit == Unit.IMPERIAL) {
            findViewById(R.id.weightLabel1).setVisibility(8);
            findViewById(R.id.weightValue1).setVisibility(8);
            ((TextView) findViewById(R.id.weightLabel2)).setText("lbs");
            findViewById(R.id.weightLabel2).setVisibility(0);
            findViewById(R.id.weightValue2).setVisibility(0);
            ((TextView) findViewById(R.id.weightValue2)).setText(String.valueOf(Prefs.kgToLbsString(valueOf2.floatValue())));
        } else {
            ((TextView) findViewById(R.id.weightLabel1)).setText("st");
            findViewById(R.id.weightLabel1).setVisibility(0);
            findViewById(R.id.weightValue1).setVisibility(0);
            ((TextView) findViewById(R.id.weightValue1)).setText(String.valueOf(Prefs.kgToStString(valueOf2.floatValue())));
            ((TextView) findViewById(R.id.weightLabel2)).setText("lbs");
            findViewById(R.id.weightLabel2).setVisibility(0);
            findViewById(R.id.weightValue2).setVisibility(0);
            ((TextView) findViewById(R.id.weightValue2)).setText(String.valueOf(Prefs.kgToStLbsString(valueOf2.floatValue())));
        }
        this.bmiTextView_.setText(Prefs.getBmiString(valueOf.floatValue(), valueOf2.floatValue()));
        String string = getString(R.string.needed_calories_text);
        this.neededCaloriesTextView_.setText(Html.fromHtml(String.format(string, new Object[]{Float.valueOf(getGoal(this.db, 0))})));
    }

    private void updateTarget() {
        if (this.preferences_.getWeight() != 0.0f && this.preferences_.getHeight() != 0.0f && this.preferences_.getPalValue() != 0.0f && !this.preferences_.getDateOfBirth().equals(XmlPullParser.NO_NAMESPACE)) {
            float year = (float) (DateProvider.getInstance().getYear() - (new SimpleDateFormat("dd-MMM-yyyy").parse(this.preferences_.getDateOfBirth(), new ParsePosition(0)).getYear() + 1900));
            float palValue = this.preferences_.getPalValue();
            float weight = this.preferences_.getWeight();
            float height = this.preferences_.getHeight();
            float f = (height * height) / 10000.0f;
            float f2 = this.maximumMassLoss_;
            if (this.preferences_.getGender().equals("M")) {
                year = ((height * 5.0f) + (66.0f + (13.7f * weight))) - (year * 6.76f);
            } else {
                year = ((height * 1.8f) + (655.0f + (9.6f * weight))) - (year * 4.7f);
            }
            this.targetWeightAdapter_.clear();
            this.weightLossMap_.clear();
            this.targetWeeksAndNecessaryCalories_.clear();
            int i = 0;
            while (i <= 20) {
                float f3 = (((float) (100 - i)) * weight) / 100.0f;
                if (f3 / f >= 18.5f) {
                    Object format;
                    if (this.preferences_.getWeightUnit() == Unit.METRIC) {
                        format = String.format("%.1f kg (%d%% loss)", new Object[]{Float.valueOf(f3), Integer.valueOf(i)});
                    } else if (this.preferences_.getWeightUnit() == Unit.IMPERIAL) {
                        format = String.format("%.0f lbs (%d%% loss)", new Object[]{Float.valueOf(WeightData.KgToLbs(f3)), Integer.valueOf(i)});
                    } else {
                        int intValue = new Float(Float.valueOf(WeightData.KgToLbs(f3)).floatValue() / 14.0f).intValue();
                        format = String.format("%d st %.0f lbs (%d%% loss)", new Object[]{Integer.valueOf(intValue), Float.valueOf(r1.floatValue() - (((float) intValue) * 14.0f)), Integer.valueOf(i)});
                    }
                    this.targetWeightAdapter_.add(format);
                    float f4 = weight - f3;
                    Float valueOf = Float.valueOf(f4 / f2);
                    if (i != 0 && valueOf.floatValue() < 1.0f) {
                        valueOf = Float.valueOf(1.0f);
                    }
                    float f5 = this.dailyDeficit_;
                    float KgToLbs = WeightData.KgToLbs(f4 / valueOf.floatValue());
                    if (i == 0) {
                        this.weightLossMap_.put(format, Float.valueOf(0.0f));
                    } else {
                        this.weightLossMap_.put(format, Float.valueOf(f4 / valueOf.floatValue()));
                    }
                    this.targetWeeksAndNecessaryCalories_.put(format, Pair.create(Integer.valueOf(valueOf.intValue()), Float.valueOf((palValue * year) - (f5 * KgToLbs))));
                    i++;
                } else {
                    return;
                }
            }
        }
    }

    private void updateTargetLabel(int i) {
        TableRow tableRow = (TableRow) findViewById(R.id.targetTableRow);
        if (i <= 0 || this.targetWeightAdapter_.getCount() <= i) {
            this.weeksWeightLoss_ = null;
            tableRow.setVisibility(4);
            return;
        }
        String str = (String) this.targetWeightAdapter_.getItem(i);
        Pair pair = (Pair) this.targetWeeksAndNecessaryCalories_.get(str);
        this.weeksWeightLoss_ = Pair.create((Integer) pair.first, (Float) this.weightLossMap_.get(str));
        String string = getString(R.string.target_text1);
        String string2 = getString(R.string.target_text2);
        TextView textView = (TextView) findViewById(R.id.targetTextView);
        tableRow.setVisibility(0);
        int intValue = ((Integer) this.weeksWeightLoss_.first).intValue();
        TargetSettingsData.updateWeightLoss(this.db, this.dailyDeficit_, ((Float) this.weeksWeightLoss_.second).floatValue(), intValue);
        textView.setText(Html.fromHtml(String.format(new StringBuilder(String.valueOf(string)).append(((Integer) pair.first).toString()).append(string2).toString(), new Object[]{Float.valueOf(getGoal(this.db, DateProvider.getInstance().getDateTime()))})));
    }

    private void updateWeeklyTarget() {
        Cursor byDate = TargetSettingsData.getByDate(this.db, DateProvider.getInstance().getDateTime());
        if (byDate.getCount() != 0) {
            float floatValue = byDate.getDailyDeficit().floatValue();
            if (floatValue == 500.0f) {
                this.weeklyTargetSpinner_.setSelection(0);
                this.maximumMassLoss_ = 0.5f;
                this.dailyDeficit_ = 500.0f;
            } else if (floatValue == 750.0f) {
                this.weeklyTargetSpinner_.setSelection(1);
                this.maximumMassLoss_ = 0.75f;
                this.dailyDeficit_ = 750.0f;
            } else if (floatValue == 1000.0f) {
                this.weeklyTargetSpinner_.setSelection(2);
                this.maximumMassLoss_ = 1.0f;
                this.dailyDeficit_ = 1000.0f;
            }
        }
        byDate.close();
    }

    private void updateWeightTarget() {
        Cursor byDate = TargetSettingsData.getByDate(this.db, DateProvider.getInstance().getDateTime());
        if (byDate.getCount() != 0) {
            float floatValue = byDate.getWeightloss().floatValue();
            int intValue = byDate.getTime().intValue();
            int count = this.targetWeightAdapter_.getCount();
            for (int i = 0; i < count; i++) {
                String str = (String) this.targetWeightAdapter_.getItem(i);
                float floatValue2 = ((Float) this.weightLossMap_.get(str)).floatValue();
                int intValue2 = ((Integer) ((Pair) this.targetWeeksAndNecessaryCalories_.get(str)).first).intValue();
                if (floatValue <= floatValue2 && intValue <= intValue2) {
                    this.targetWeightSpinner_.setSelection(i);
                    break;
                }
            }
        }
        byDate.close();
    }

    public void onBackPressed() {
        if (this.weeksWeightLoss_ == null) {
            TargetSettingsData.updateWeightLoss(this.db, 0.0f, 0.0f, 0);
        }
        super.onBackPressed();
    }

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        requestWindowFeature(1);
        setContentView(R.layout.target_settings);
        this.bmiTextView_ = (TextView) findViewById(R.id.bmiTextView);
        this.neededCaloriesTextView_ = (TextView) findViewById(R.id.neededCaloriesTextView);
        this.weeklyTargetSpinner_ = (Spinner) findViewById(R.id.weeklyTargetSpinner);
        SpinnerAdapter arrayAdapter = new ArrayAdapter(this, 17367048, this.weeklyTargets_);
        arrayAdapter.setDropDownViewResource(17367049);
        this.weeklyTargetSpinner_.setAdapter(arrayAdapter);
        this.weeklyTargetSpinner_.setOnItemSelectedListener(new OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long j) {
                ArrayAdapter arrayAdapter = (ArrayAdapter) TargetSettings.this.weeklyTargetSpinner_.getAdapter();
                if (arrayAdapter.getCount() > i) {
                    String str = (String) arrayAdapter.getItem(i);
                    if (str == TargetSettings.this.weeklyTargets_[0]) {
                        TargetSettings.this.maximumMassLoss_ = 0.5f;
                        TargetSettings.this.dailyDeficit_ = 500.0f;
                    } else if (str == TargetSettings.this.weeklyTargets_[1]) {
                        TargetSettings.this.maximumMassLoss_ = 0.75f;
                        TargetSettings.this.dailyDeficit_ = 750.0f;
                    } else if (str == TargetSettings.this.weeklyTargets_[2]) {
                        TargetSettings.this.maximumMassLoss_ = 1.0f;
                        TargetSettings.this.dailyDeficit_ = 1000.0f;
                    }
                    TargetSettings.this.updateTarget();
                    TargetSettings.this.updateTargetLabel(TargetSettings.this.targetWeightSpinner_.getSelectedItemPosition());
                }
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        this.weeklyTargetSpinner_.setSelection(0);
        this.targetWeightSpinner_ = (Spinner) findViewById(R.id.targetWeightSpinner);
        this.targetWeightAdapter_ = new ArrayAdapter(this, 17367048);
        this.targetWeightAdapter_.setDropDownViewResource(17367049);
        this.targetWeightSpinner_.setAdapter(this.targetWeightAdapter_);
        this.targetWeightSpinner_.setOnItemSelectedListener(new OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long j) {
                TargetSettings.this.updateTargetLabel(i);
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        ((ImageButton) findViewById(R.id.bmiInfoButton)).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                DialogActivity.setLayoutId(R.layout.dialog_activity_1);
                TargetSettings.this.startActivity(new Intent(TargetSettings.this, DialogActivity.class));
                TargetSettings.this.overridePendingTransition(R.anim.fade, R.anim.hold);
            }
        });
        ((ImageButton) findViewById(R.id.weightLossGoalButton)).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                DialogActivity.setLayoutId(R.layout.dialog_activity_2);
                TargetSettings.this.startActivity(new Intent(TargetSettings.this, DialogActivity.class));
                TargetSettings.this.overridePendingTransition(R.anim.fade, R.anim.hold);
            }
        });
        ((ImageButton) findViewById(R.id.targetDescriptionButton)).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                DialogActivity.setLayoutId(R.layout.dialog_activity_3);
                TargetSettings.this.startActivity(new Intent(TargetSettings.this, DialogActivity.class));
                TargetSettings.this.overridePendingTransition(R.anim.fade, R.anim.hold);
            }
        });
        this.db = new MyMealMateDatabase(this);
        this.preferences_ = new Prefs(this.db, this);
    }

    public void onPause() {
        if (this.currentGoal != Math.round(getGoal(this.db, DateProvider.getInstance().getDateTime()))) {
            LogData.message(this.db, String.format(getString(R.string.new_target_log_message), new Object[]{Integer.valueOf(r0)}));
        }
        super.onPause();
    }

    protected void onResume() {
        super.onResume();
        if (this.preferences_.getWeight() == 0.0f || this.preferences_.getHeight() == 0.0f || this.preferences_.getPalValue() == 0.0f || this.preferences_.getDateOfBirth().equals(XmlPullParser.NO_NAMESPACE)) {
            new Builder(this).setTitle(R.string.weight_entry_title).setMessage(getString(R.string.enter_profile_weight_message)).setIcon(17301543).setPositiveButton(17039370, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    TargetSettings.this.startActivity(new Intent(TargetSettings.this, Profile.class));
                }
            }).show();
            return;
        }
        this.currentGoal = Math.round(getGoal(this.db, DateProvider.getInstance().getDateTime()));
        populateData();
        updateWeeklyTarget();
        updateTarget();
        updateWeightTarget();
    }

    public void onStop() {
        this.db.close();
        super.onStop();
    }
}