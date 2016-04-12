package com.mymealmate;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.mymealmate.ExerciseEntryData.Cursor;
import com.mymealmate.MyMealMateDatabase.MyMealMateCursor;
import java.util.Iterator;

public class Diary extends Activity implements OnClickListener {
    private OnDateSetListener dateSetListener_;
    private MyMealMateDatabase db_;

    public Diary() {
        this.db_ = null;
        this.dateSetListener_ = new OnDateSetListener() {
            public void onDateSet(DatePicker datePicker, int i, int i2, int i3) {
                TextView textView = (TextView) Diary.this.findViewById(R.id.dateTextView);
                DateProvider instance = DateProvider.getInstance();
                instance.setYear(i);
                instance.setMonth(i2);
                instance.setDate(i3);
                textView.setText(instance.getFormatedDate());
                Diary.this.updateScreen();
            }
        };
    }

    private void updateScreen() {
        Iterator it = FoodEntryData.getArrayByDate(this.db_, DateProvider.getInstance()).iterator();
        float f = 0.0f;
        while (it.hasNext()) {
            FoodEntryData foodEntryData = (FoodEntryData) it.next();
            if (foodEntryData.foodItem != 0) {
                MyMealMateCursor foodCursorById = this.db_.getFoodCursorById(foodEntryData.foodItem);
                float size = (foodEntryData.amount * ((foodCursorById.getSize() / 100.0f) * foodCursorById.getCalories())) + f;
                foodCursorById.close();
                f = size;
            }
        }
        Cursor byDate = ExerciseEntryData.getByDate(this.db_, DateProvider.getInstance());
        int i = 0;
        float f2 = 0.0f;
        while (i < byDate.getCount()) {
            byDate.moveToPosition(i);
            i++;
            f2 = byDate.getCals().floatValue() + f2;
        }
        byDate.close();
        TextView textView = (TextView) findViewById(R.id.calRemainingTextView);
        TextView textView2 = (TextView) findViewById(R.id.goalValueTextView);
        TextView textView3 = (TextView) findViewById(R.id.foodValueTextView);
        TextView textView4 = (TextView) findViewById(R.id.exerciseValueTextView);
        TextView textView5 = (TextView) findViewById(R.id.netValueTextView);
        TextView textView6 = (TextView) findViewById(R.id.labelTextView);
        textView.setText(String.format("%.0f", new Object[]{Float.valueOf(TargetSettings.getGoal(this.db_, DateProvider.getInstance().getDateTime()) - (f - f2))}));
        if (TargetSettings.getGoal(this.db_, DateProvider.getInstance().getDateTime()) - (f - f2) >= 0.0f) {
            textView.setText(String.format("%.0f", new Object[]{Float.valueOf(r10)}));
            textView.setTextColor(getResources().getColor(R.color.light_green));
            textView6.setText(getResources().getText(R.string.calor_remain_label));
        } else {
            textView.setText(String.format("%.0f", new Object[]{Float.valueOf(r10 * -1.0f)}));
            textView.setTextColor(getResources().getColor(R.color.light_red));
            textView6.setText(getResources().getText(R.string.calor_over_limit_label));
        }
        textView2.setText(String.format("%.0f", new Object[]{Float.valueOf(r8)}));
        textView3.setText("+" + String.format("%.0f", new Object[]{Float.valueOf(f)}));
        textView4.setText("-" + String.format("%.0f", new Object[]{Float.valueOf(f2)}));
        textView5.setText(String.format("%.0f", new Object[]{Float.valueOf(r9)}));
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.holidayModeIndicatorLinearLayout);
        if (new Prefs(this.db_, this).getHolidayMode().booleanValue()) {
            linearLayout.setVisibility(0);
        } else {
            linearLayout.setVisibility(8);
        }
    }

    public void onClick(View view) {
        TextView textView = (TextView) findViewById(R.id.dateTextView);
        DateProvider instance = DateProvider.getInstance();
        switch (view.getId()) {
            case R.id.prevDateNavButton /*2131361840*/:
                instance.prevDate();
                textView.setText(instance.getFormatedDate());
                break;
            case R.id.dateTextView /*2131361841*/:
                showDialog(-1);
                break;
            case R.id.nextDateNavButton /*2131361842*/:
                instance.nextDate();
                textView.setText(instance.getFormatedDate());
                break;
            case R.id.addToDiaryButton /*2131361856*/:
                startActivity(new Intent(this, FoodLog.class));
                break;
        }
        updateScreen();
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        requestWindowFeature(1);
        setContentView(R.layout.diary);
        findViewById(R.id.prevDateNavButton).setOnClickListener(this);
        findViewById(R.id.nextDateNavButton).setOnClickListener(this);
        findViewById(R.id.dateTextView).setOnClickListener(this);
        findViewById(R.id.addToDiaryButton).setOnClickListener(this);
        this.db_ = new MyMealMateDatabase(this);
        updateScreen();
    }

    protected Dialog onCreateDialog(int i) {
        DateProvider instance = DateProvider.getInstance();
        return new DatePickerDialog(this, this.dateSetListener_, instance.getYear(), instance.getMonth(), instance.getDate());
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.settings /*2131361951*/:
                startActivity(new Intent(this, SettingsList.class));
                return true;
            case R.id.help /*2131361952*/:
                startActivity(new Intent("android.intent.action.VIEW", Uri.parse(getString(R.string.youtube_channel))));
                return true;
            case R.id.food_log /*2131361953*/:
                startActivity(new Intent(this, FoodLog.class));
                return true;
            case R.id.progress_tracking /*2131361954*/:
                startActivity(new Intent(this, ProgressTracking.class));
                return true;
            case R.id.weight_entry /*2131361955*/:
                startActivity(new Intent(this, WeightEntry.class));
                return true;
            default:
                return false;
        }
    }

    protected void onPrepareDialog(int i, Dialog dialog) {
        DateProvider instance = DateProvider.getInstance();
        ((DatePickerDialog) dialog).updateDate(instance.getYear(), instance.getMonth(), instance.getDate());
    }

    protected void onResume() {
        super.onResume();
        ((TextView) findViewById(R.id.dateTextView)).setText(DateProvider.getInstance().getFormatedDate());
        updateScreen();
    }

    public void onStop() {
        this.db_.close();
        super.onStop();
    }
}