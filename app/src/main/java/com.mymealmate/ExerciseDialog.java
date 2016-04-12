package com.mymealmate;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

public class ExerciseDialog extends AlertDialog implements OnClickListener {
    private Spinner category;
    private MyMealMateDatabase db;
    private ExerciseEntryData entry;
    private Spinner exercise;
    private FoodLog foodLogActivity;
    private LinearLayout layout;
    private EditText length;

    public ExerciseDialog(FoodLog foodLog) {
        this(foodLog, 0);
    }

    public ExerciseDialog(FoodLog foodLog, long j) {
        super(foodLog);
        this.foodLogActivity = foodLog;
        this.db = new MyMealMateDatabase(foodLog);
        if (j > 0) {
            this.entry = ExerciseEntryData.getEntry(this.db, j);
            setButton(-3, "Delete", new OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    ExerciseDialog.this.entry.delete();
                    ExerciseDialog.this.foodLogActivity.populateLayout();
                }
            });
        } else {
            this.entry = new ExerciseEntryData(this.db);
        }
        setTitle("Exercise");
        setButton(-1, "Save", this);
        setButton(-2, "Cancel", new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        this.layout = new LinearLayout(getContext());
        this.layout.setOrientation(1);
        this.layout.setHorizontalGravity(3);
        this.layout.setPadding(5, 0, 5, 0);
        setView(this.layout);
        View textView = new TextView(getContext());
        textView.setPadding(10, 10, 0, 0);
        textView.setText(R.string.select_category);
        this.layout.addView(textView, new LayoutParams(-2, -2));
        this.category = new Spinner(getContext());
        this.category.setOnItemSelectedListener(new OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long j) {
                ExerciseDialog.this.setExerciseAdapter(((SimpleCursorAdapter) ExerciseDialog.this.category.getAdapter()).getItemId(i));
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        SpinnerAdapter simpleCursorAdapter = new SimpleCursorAdapter(getContext(), 17367048, ExerciseData.getCategories(this.db), new String[]{"Category"}, new int[]{16908308});
        simpleCursorAdapter.setDropDownViewResource(17367049);
        this.category.setAdapter(simpleCursorAdapter);
        this.layout.addView(this.category, new LayoutParams(-1, -2));
        textView = new TextView(getContext());
        textView.setPadding(10, 10, 0, 0);
        textView.setText(R.string.select_exercise);
        this.layout.addView(textView, new LayoutParams(-2, -2));
        this.exercise = new Spinner(getContext());
        this.layout.addView(this.exercise, new LayoutParams(-1, -2));
        textView = new TextView(getContext());
        textView.setPadding(10, 10, 0, 0);
        textView.setText(R.string.exercise_minutes);
        this.layout.addView(textView, new LayoutParams(-2, -2));
        this.length = new EditText(getContext());
        this.length.setInputType(2);
        this.length.setText("30");
        this.length.setCompoundDrawablePadding(10);
        this.layout.addView(this.length, new LayoutParams(150, -2));
    }

    private void setExerciseAdapter(long j) {
        SimpleCursorAdapter simpleCursorAdapter = new SimpleCursorAdapter(getContext(), 17367048, ExerciseData.getExercises(this.db, Long.valueOf(j)), new String[]{"Description"}, new int[]{16908308});
        simpleCursorAdapter.setDropDownViewResource(R.layout.spinner_drop_down);
        this.exercise.setAdapter(simpleCursorAdapter);
        for (int i = 0; i < simpleCursorAdapter.getCount(); i++) {
            if (simpleCursorAdapter.getItemId(i) == this.entry.code) {
                this.exercise.setSelection(i);
                return;
            }
        }
    }

    private void updateControls() {
        if (this.entry != null && this.entry.id != 0) {
            for (int i = 0; i < this.category.getAdapter().getCount(); i++) {
                if (this.category.getAdapter().getItemId(i) == this.entry.category) {
                    this.category.setSelection(i);
                    setExerciseAdapter(this.entry.category);
                    break;
                }
            }
            this.length.setText(String.valueOf(this.entry.time));
        }
    }

    public void onClick(DialogInterface dialogInterface, int i) {
        long selectedItemId = this.exercise.getSelectedItemId();
        int parseInt = Integer.parseInt(this.length.getText().toString());
        float round = ((float) Math.round((float) ((parseInt * 100) / 60))) / 100.0f;
        float actualWeight = WeightData.getActualWeight(this.db, DateProvider.getInstance().getEndOfDay());
        float mETsByCode = ExerciseData.getMETsByCode(this.db, selectedItemId);
        this.entry.code = selectedItemId;
        this.entry.time = parseInt;
        if (this.entry.code == 2222) {
            this.entry.calories = (float) this.entry.time;
        } else {
            this.entry.calories = (mETsByCode * actualWeight) * round;
        }
        this.entry.date = DateProvider.getInstance().getDateTime();
        if (this.entry.id == 0) {
            this.entry.create();
        } else {
            this.entry.update();
        }
        this.foodLogActivity.populateLayout();
    }

    public void onStop() {
        this.db.close();
        super.onStop();
    }

    public void setId(long j) {
        if (j > 0) {
            this.entry = ExerciseEntryData.getEntry(this.db, j);
        } else {
            this.entry = new ExerciseEntryData(this.db);
        }
        updateControls();
    }
}