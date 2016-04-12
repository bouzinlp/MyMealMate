package com.mymealmate;

import android.app.Dialog;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.os.Bundle;
import android.os.Environment;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import com.mymealmate.MyMealMateDatabase.MyMealMateCursor;
import java.util.ArrayList;
import java.util.Iterator;
import org.xmlpull.v1.XmlPullParser;

public class AnnotationActivity extends FoodSearchActivity {
    private static final int ANIM_DURATION = 400;
    private static final int WIDTH = 480;
    private int _currentView;
    private GestureDetector _detector;
    private LinearLayout _foodLayout;
    private ArrayList<FoodEntryData> _foodList;
    private int _mealSlot;
    private String _photoPath;
    private TranslateAnimation inLeft;
    private TranslateAnimation inRight;
    private TranslateAnimation outLeft;
    private TranslateAnimation outRight;

    public AnnotationActivity() {
        this._currentView = 0;
        this.inLeft = new TranslateAnimation(480.0f, 0.0f, 0.0f, 0.0f);
        this.outLeft = new TranslateAnimation(0.0f, -480.0f, 0.0f, 0.0f);
        this.inRight = new TranslateAnimation(-480.0f, 0.0f, 0.0f, 0.0f);
        this.outRight = new TranslateAnimation(0.0f, 480.0f, 0.0f, 0.0f);
        this._photoPath = XmlPullParser.NO_NAMESPACE;
        this._foodList = new ArrayList();
        this._foodLayout = null;
    }

    private void addItemToLayout(String str, float f, FoodEntryData foodEntryData) {
        View linearLayout = new LinearLayout(this);
        linearLayout.setTag(foodEntryData);
        View textView = new TextView(this);
        textView.setId(3);
        textView.setText(Long.toString(foodEntryData.foodItem));
        textView.setVisibility(8);
        linearLayout.addView(textView);
        linearLayout.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                AnnotationActivity._current_edit_view = view;
                AnnotationActivity.this.showDialog(11);
            }
        });
        linearLayout.setOrientation(0);
        View textView2 = new TextView(this);
        textView2.setId(1);
        View textView3 = new TextView(this);
        textView3.setId(2);
        int color = getResources().getColor(R.color.light_gray);
        textView2.setTextAppearance(this, R.style.TextStyle0);
        textView2.setTextColor(color);
        textView3.setTextAppearance(this, R.style.TextStyle0);
        textView3.setTextColor(color);
        textView2.setLayoutParams(new LayoutParams(-1, -2, 1.0f));
        textView2.setGravity(19);
        textView3.setLayoutParams(new LayoutParams(-1, -2, 1.0f));
        textView3.setGravity(21);
        linearLayout.addView(textView2);
        linearLayout.addView(textView3);
        if (this._foodLayout.getChildCount() != 0) {
            ((LinearLayout) this._foodLayout.getChildAt(this._foodLayout.getChildCount() - 1)).setBackgroundDrawable(getResources().getDrawable(R.drawable.list_separator));
        }
        linearLayout.setBackgroundDrawable(getResources().getDrawable(R.drawable.list_footer));
        linearLayout.setMinimumHeight(54);
        linearLayout.setGravity(16);
        this._foodLayout.addView(linearLayout);
        textView2.setText(str);
        textView3.setText(String.format("%.1f", new Object[]{Float.valueOf(f)}));
    }

    public void onAddClick(View view) {
        _current_edit_view = findViewById(R.id.foodListLinearLayout);
        showDialog(10);
    }

    public void onCancelClick(View view) {
        finish();
    }

    public void onClick(View view) {
    }

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.annotation);
        this.inLeft.setDuration(400);
        this.outLeft.setDuration(400);
        this.inRight.setDuration(400);
        this.outRight.setDuration(400);
        Bundle extras = getIntent().getExtras();
        this._photoPath = extras.getString("photoPath");
        String str = Environment.getExternalStorageDirectory() + "/MyMealMate/" + this._photoPath;
        Options options = new Options();
        options.inSampleSize = 4;
        ((ImageView) findViewById(R.id.pictureView)).setImageBitmap(BitmapFactory.decodeFile(str, options));
        this._mealSlot = extras.getInt("mealSlot");
        String[] strArr = new String[]{"Breakfast", "Lunch", "Evening Meal", "Snacks"};
        ((TextView) findViewById(R.id.breakfastLabelTextView)).setText(strArr[this._mealSlot - 1]);
        this._foodLayout = (LinearLayout) findViewById(R.id.foodListLinearLayout);
        this._detector = new GestureDetector(this, new SimpleOnGestureListener() {
            public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
                if (Math.abs((int) (motionEvent2.getX() - motionEvent.getX())) > 100 && Math.abs(f) > Math.abs(f2)) {
                    View findViewById = AnnotationActivity.this.findViewById(R.id.foodView);
                    View findViewById2 = AnnotationActivity.this.findViewById(R.id.pictureView);
                    if (f < 0.0f) {
                        if (AnnotationActivity.this._currentView != 1) {
                            findViewById2.setVisibility(0);
                            findViewById2.startAnimation(AnnotationActivity.this.inLeft);
                            findViewById.startAnimation(AnnotationActivity.this.outLeft);
                            findViewById.setVisibility(8);
                            AnnotationActivity.this._currentView = 1;
                            return true;
                        }
                    } else if (AnnotationActivity.this._currentView != 0) {
                        findViewById.setVisibility(0);
                        findViewById.startAnimation(AnnotationActivity.this.inRight);
                        findViewById2.startAnimation(AnnotationActivity.this.outRight);
                        findViewById2.setVisibility(8);
                        AnnotationActivity.this._currentView = 0;
                        return true;
                    }
                }
                return false;
            }
        });
    }

    protected Dialog onCreateDialog(int i) {
        return super.onCreateDialog(i);
    }

    public void onDoneClick(View view) {
        Iterator it = this._foodList.iterator();
        while (it.hasNext()) {
            ((FoodEntryData) it.next()).create();
        }
        FoodEntryData.getEntry(_db, getIntent().getExtras().getLong("entryId")).delete();
        finish();
    }

    protected void onPause() {
        super.onPause();
        System.gc();
    }

    protected void onPrepareDialog(int i, Dialog dialog) {
        super.onPrepareDialog(i, dialog);
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        this._detector.onTouchEvent(motionEvent);
        return super.onTouchEvent(motionEvent);
    }

    public void populateLayout() {
        _current_edit_view = null;
        this._foodLayout.removeAllViews();
        Iterator it = this._foodList.iterator();
        while (it.hasNext()) {
            FoodEntryData foodEntryData = (FoodEntryData) it.next();
            String str = XmlPullParser.NO_NAMESPACE;
            float f = 0.0f;
            if (foodEntryData.foodItem != 0) {
                MyMealMateCursor foodCursorById = _db.getFoodCursorById(foodEntryData.foodItem);
                str = foodCursorById.getFoodDescription();
                f = calculateCalories(foodCursorById.getSize(), foodCursorById.getCalories(), foodEntryData.amount);
                foodCursorById.close();
            }
            addItemToLayout(str, f, foodEntryData);
        }
    }

    public void storeFoodEntry(int i, float f, long j, float f2) {
        FoodEntryData foodEntryData = new FoodEntryData(_db);
        foodEntryData.mealSlot = this._mealSlot;
        foodEntryData.foodItem = j;
        foodEntryData.amount = f;
        foodEntryData.calories = f2;
        foodEntryData.photo = this._photoPath;
        this._foodList.add(foodEntryData);
    }
}