package com.mymealmate;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;

public class ExerciseSettings extends Activity {
    private String leisureItem;
    private float[] leisureScore;
    private String workItem;
    private float[] workScore;

    public ExerciseSettings() {
        this.workScore = new float[]{1.45f, 1.65f, 1.85f, 2.2f};
        this.leisureScore = new float[]{0.0f, 0.06f, 0.15f, 0.29f};
    }

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        requestWindowFeature(1);
        setContentView(R.layout.exercise_settings);
        SharedPreferences preferences = getPreferences(0);
        this.workItem = preferences.getString("ExerciseWork", "1");
        RadioButton radioButton = (RadioButton) findViewById(getResources().getIdentifier("workRadio" + this.workItem, "id", getPackageName()));
        if (radioButton != null) {
            radioButton.setChecked(true);
        }
        this.leisureItem = preferences.getString("ExerciseLeisure", "1");
        radioButton = (RadioButton) findViewById(getResources().getIdentifier("leisureRadio" + this.leisureItem, "id", getPackageName()));
        if (radioButton != null) {
            radioButton.setChecked(true);
        }
    }

    public void onLeisureClick(View view) {
        this.leisureItem = view.getTag().toString();
    }

    protected void onPause() {
        super.onPause();
        SharedPreferences preferences = getPreferences(0);
        if (preferences.getString("ExerciseWork", "0") != this.workItem || preferences.getString("ExerciseLeisure", "0") != this.leisureItem) {
            MyMealMateDatabase myMealMateDatabase = new MyMealMateDatabase(this);
            if (preferences.getString("ExerciseWork", "0") != this.workItem) {
                LogData.message(myMealMateDatabase, "Work activity changed to: " + ((RadioButton) findViewById(getResources().getIdentifier("workRadio" + this.workItem, "id", getPackageName()))).getText());
            }
            if (preferences.getString("ExerciseLeisure", "0") != this.leisureItem) {
                LogData.message(myMealMateDatabase, "Leisure activity changed to: " + ((RadioButton) findViewById(getResources().getIdentifier("leisureRadio" + this.leisureItem, "id", getPackageName()))).getText());
            }
            Editor edit = preferences.edit();
            edit.putString("ExerciseWork", String.valueOf(this.workItem));
            edit.putString("ExerciseLeisure", String.valueOf(this.leisureItem));
            edit.commit();
            new Prefs(myMealMateDatabase, this).setPalValue(Float.valueOf(this.workScore[Integer.parseInt(this.workItem) - 1] + this.leisureScore[Integer.parseInt(this.leisureItem) - 1]));
            myMealMateDatabase.close();
        }
    }

    public void onWorkClick(View view) {
        this.workItem = view.getTag().toString();
    }
}