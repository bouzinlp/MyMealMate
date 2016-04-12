package com.mymealmate;

import android.app.AlertDialog.Builder;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import com.mymealmate.MyMealMateDatabase.MyMealMateCursor;
import java.io.File;
import java.util.Iterator;
import org.xmlpull.v1.XmlPullParser;

public class FoodLog extends FoodSearchActivity {
    private static final int DIALOG_ADD_EXERCISE = 190;
    private static final int DIALOG_ADD_FAVOURITE = 192;
    private static final int DIALOG_ADD_PHOTO = 194;
    private static final int DIALOG_COMPLETE_DAY = 195;
    private static final int DIALOG_EDIT_EXERCISE = 191;
    private static final int DIALOG_PICKUP_DATE = 1;
    private static final int DIALOG_SELECT_RECENT = 196;
    public boolean _dayCompleted;
    private ExerciseDialog _exerciseDialog;
    private long _exerciseId;
    private Dialog _takePhotoDialog;
    private int _takePhotoMealslot;
    private String _takePhotoPath;
    private AddFavouriteDialog addFavouriteDialog_;
    private OnDateSetListener dateSetListener_;
    public long recentItemId;
    public int recentItemMealslot;
    private Dialog recentItemsDialog;
    public Spinner recentItemsSpinner;
    private SelectFavouriteDialog selectFavouriteDialog_;

    /* renamed from: com.mymealmate.FoodLog.3 */
    class AnonymousClass3 implements OnClickListener {
        private final /* synthetic */ LinearLayout val$list;

        AnonymousClass3(LinearLayout linearLayout) {
            this.val$list = linearLayout;
        }

        public void onClick(View view) {
            if (!FoodLog.this._dayCompleted) {
                if (this.val$list.getId() == R.id.exerciseListLinearLayout) {
                    FoodLog.this._exerciseId = (long) Integer.parseInt(((TextView) ((LinearLayout) view).findViewById(3)).getText().toString());
                    FoodLog.this.showDialog(FoodLog.DIALOG_EDIT_EXERCISE);
                    return;
                }
                FoodLog._current_edit_view = view;
                FoodLog.this.showDialog(11);
            }
        }
    }

    public FoodLog() {
        this._exerciseId = 0;
        this._exerciseDialog = null;
        this.addFavouriteDialog_ = null;
        this.selectFavouriteDialog_ = null;
        this._dayCompleted = false;
        this.recentItemsDialog = null;
        this.recentItemsSpinner = null;
        this.recentItemId = 0;
        this.recentItemMealslot = 0;
        this.dateSetListener_ = new OnDateSetListener() {
            public void onDateSet(DatePicker datePicker, int i, int i2, int i3) {
                TextView textView = (TextView) FoodLog.this.findViewById(R.id.dateTextView);
                DateProvider instance = DateProvider.getInstance();
                instance.setYear(i);
                instance.setMonth(i2);
                instance.setDate(i3);
                textView.setText(instance.getFormatedDate());
                FoodLog.this.populateLayout();
            }
        };
        this._takePhotoDialog = null;
    }

    private View addItemToLayout(int i, String str, float f, long j, long j2, BaseData baseData) {
        LinearLayout linearLayout = (LinearLayout) findViewById(i);
        View linearLayout2 = new LinearLayout(this);
        linearLayout2.setTag(baseData);
        View textView = new TextView(this);
        textView.setId(3);
        textView.setText(Long.toString(j));
        textView.setVisibility(8);
        linearLayout2.addView(textView);
        if (j > 0) {
            linearLayout2.setOnClickListener(new AnonymousClass3(linearLayout));
        } else {
            linearLayout2.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    if (!FoodLog.this._dayCompleted) {
                        FoodEntryData foodEntryData = (FoodEntryData) view.getTag();
                        Intent intent = new Intent(FoodLog.this, AnnotationActivity.class);
                        intent.putExtra("photoPath", foodEntryData.photo);
                        intent.putExtra("mealSlot", foodEntryData.mealSlot);
                        intent.putExtra("entryId", foodEntryData.id);
                        FoodLog.this.startActivity(intent);
                    }
                }
            });
        }
        if (j > 0) {
            linearLayout2.setOrientation(0);
            View textView2 = new TextView(this);
            textView2.setId(DIALOG_PICKUP_DATE);
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
            linearLayout2.addView(textView2);
            linearLayout2.addView(textView3);
            if (linearLayout.getChildCount() != 0) {
                ((LinearLayout) linearLayout.getChildAt(linearLayout.getChildCount() - 1)).setBackgroundDrawable(getResources().getDrawable(R.drawable.list_separator));
            }
            linearLayout2.setBackgroundDrawable(getResources().getDrawable(R.drawable.list_footer));
            linearLayout2.setMinimumHeight(54);
            linearLayout2.setGravity(16);
            linearLayout.addView(linearLayout2);
            textView2.setText(str);
            Object[] objArr = new Object[DIALOG_PICKUP_DATE];
            objArr[0] = Float.valueOf(f);
            textView3.setText(String.format("%.0f", objArr));
            textView3.setTag(Float.valueOf(f));
            return linearLayout2;
        }
        textView = new ImageView(this);
        textView.setImageResource(R.drawable.food_photo);
        linearLayout2.addView(textView);
        linearLayout.addView(linearLayout2);
        return linearLayout2;
    }

    private void setTitle() {
        TextView textView = (TextView) findViewById(R.id.TitleTextView);
        if (this._dayCompleted) {
            textView.setText(R.string.food_log_complete_title);
        } else {
            textView.setText(R.string.food_log_title);
        }
    }

    private void updateTotalCalories() {
        float f;
        Object[] objArr;
        TextView textView;
        int[][] iArr = new int[][]{new int[]{R.id.breakfastListLinearLayout, R.id.breakfastTitleLinearLayout, R.id.breakfastTotalTextView}, new int[]{R.id.lunchListLinearLayout, R.id.lunchTitleLinearLayout, R.id.lunchTotalTextView}, new int[]{R.id.supperListLinearLayout, R.id.supperTitleLinearLayout, R.id.supperTotalTextView}, new int[]{R.id.snacksListLinearLayout, R.id.snacksTitleLinearLayout, R.id.snacksTotalTextView}, new int[]{R.id.exerciseListLinearLayout, R.id.exerciseTitleLinearLayout, R.id.exerciseTotalTextView}};
        TextView textView2 = (TextView) findViewById(R.id.totalExerciseTextView);
        int length = iArr.length;
        int i = 0;
        float f2 = 0.0f;
        while (i < length) {
            int[] iArr2 = iArr[i];
            LinearLayout linearLayout = (LinearLayout) findViewById(iArr2[0]);
            float f3 = 0.0f;
            for (int i2 = 0; i2 < linearLayout.getChildCount(); i2 += DIALOG_PICKUP_DATE) {
                TextView textView3 = (TextView) linearLayout.getChildAt(i2).findViewById(2);
                if (textView3 != null) {
                    f3 += ((Float) textView3.getTag()).floatValue();
                }
            }
            if (iArr2[0] != R.id.exerciseListLinearLayout) {
                f = f2 + f3;
            } else {
                StringBuilder stringBuilder = new StringBuilder("-");
                objArr = new Object[DIALOG_PICKUP_DATE];
                objArr[0] = Float.valueOf(f3);
                textView2.setText(stringBuilder.append(String.format("%.0f", objArr)).toString());
                f = f2;
            }
            textView = (TextView) ((LinearLayout) findViewById(iArr2[DIALOG_PICKUP_DATE])).findViewById(iArr2[2]);
            Object[] objArr2 = new Object[DIALOG_PICKUP_DATE];
            objArr2[0] = Float.valueOf(f3);
            textView.setText(String.format("%.0f kcal", objArr2));
            i += DIALOG_PICKUP_DATE;
            f2 = f;
        }
        textView = (TextView) findViewById(R.id.totalFoodTextView);
        objArr = new Object[DIALOG_PICKUP_DATE];
        objArr[0] = Float.valueOf(f2);
        textView.setText(String.format("%.0f", objArr));
        float parseFloat = Float.parseFloat(((TextView) findViewById(R.id.goalTextView)).getText().toString());
        f = f2 + Float.parseFloat(textView2.getText().toString());
        textView2 = (TextView) findViewById(R.id.netTextView);
        Object[] objArr3 = new Object[DIALOG_PICKUP_DATE];
        objArr3[0] = Float.valueOf(f);
        textView2.setText(String.format("%.0f", objArr3));
        textView2 = (TextView) findViewById(R.id.remainingTextView);
        objArr3 = new Object[DIALOG_PICKUP_DATE];
        objArr3[0] = Float.valueOf(parseFloat - f);
        textView2.setText(String.format("%.0f", objArr3));
    }

    protected void onActivityResult(int i, int i2, Intent intent) {
        switch (i2) {
            case -1:
                FoodEntryData.insertFoodPhoto(this._takePhotoMealslot, this._takePhotoPath, DateProvider.getInstance().getStartOfDay());
                populateLayout();
                break;
        }
        System.gc();
    }

    public void onClick(View view) {
        TextView textView = (TextView) findViewById(R.id.dateTextView);
        DateProvider instance = DateProvider.getInstance();
        switch (view.getId()) {
            case R.id.breakfastTitleLinearLayout /*2131361823*/:
            case R.id.lunchTitleLinearLayout /*2131361875*/:
            case R.id.supperTitleLinearLayout /*2131361880*/:
            case R.id.snacksTitleLinearLayout /*2131361885*/:
                if (!this._dayCompleted) {
                    _current_edit_view = view;
                    showDialog(10);
                }
            case R.id.prevDateNavButton /*2131361840*/:
                instance.prevDate();
                textView.setText(instance.getFormatedDate());
                populateLayout();
            case R.id.dateTextView /*2131361841*/:
                showDialog(DIALOG_PICKUP_DATE);
            case R.id.nextDateNavButton /*2131361842*/:
                instance.nextDate();
                textView.setText(instance.getFormatedDate());
                populateLayout();
            case R.id.exerciseTitleLinearLayout /*2131361889*/:
                if (!this._dayCompleted) {
                    if (new Prefs(_db, this).getWeight() == 0.0f) {
                        new Builder(this).setTitle(R.string.weight_entry_title).setMessage(getString(R.string.enter_profile_weight_message)).setIcon(17301543).setPositiveButton(17039370, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {
                                FoodLog.this.startActivity(new Intent(FoodLog.this, Profile.class));
                            }
                        }).setNegativeButton(17039360, null).show();
                    } else {
                        showDialog(DIALOG_ADD_EXERCISE);
                    }
                }
            default:
        }
    }

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.food_log);
        findViewById(R.id.prevDateNavButton).setOnClickListener(this);
        findViewById(R.id.nextDateNavButton).setOnClickListener(this);
        TextView textView = (TextView) findViewById(R.id.dateTextView);
        textView.setOnClickListener(this);
        textView.setText(DateProvider.getInstance().getFormatedDate());
        findViewById(R.id.breakfastTitleLinearLayout).setOnClickListener(this);
        findViewById(R.id.lunchTitleLinearLayout).setOnClickListener(this);
        findViewById(R.id.supperTitleLinearLayout).setOnClickListener(this);
        findViewById(R.id.snacksTitleLinearLayout).setOnClickListener(this);
        findViewById(R.id.exerciseTitleLinearLayout).setOnClickListener(this);
    }

    protected Dialog onCreateDialog(int i) {
        int i2 = 0;
        super.onCreateDialog(i);
        if (i == 10 || i == 11) {
            if (this._addEditDialog != null) {
                return this._addEditDialog;
            }
        } else if (i == 12) {
            if (this._brandDialog != null) {
                return this._brandDialog;
            }
        } else if (i == DIALOG_PICKUP_DATE) {
            DateProvider instance = DateProvider.getInstance();
            return new DatePickerDialog(this, this.dateSetListener_, instance.getYear(), instance.getMonth(), instance.getDate());
        } else if (i == DIALOG_ADD_PHOTO) {
            r1 = new LinearLayout(this);
            r1.setOrientation(DIALOG_PICKUP_DATE);
            r1.setHorizontalGravity(3);
            r1.setPadding(5, 0, 5, 0);
            r2 = new TextView(this);
            r2.setPadding(10, 10, 0, 0);
            r2.setText(R.string.select_mealslot);
            r1.addView(r2, new LayoutParams(-2, -2));
            View spinner = new Spinner(this);
            SpinnerAdapter arrayAdapter = new ArrayAdapter(this, 17367048, getResources().getStringArray(R.array.mealslots_array));
            arrayAdapter.setDropDownViewResource(17367049);
            spinner.setAdapter(arrayAdapter);
            spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long j) {
                    FoodLog.this._takePhotoMealslot = i + FoodLog.DIALOG_PICKUP_DATE;
                }

                public void onNothingSelected(AdapterView<?> adapterView) {
                }
            });
            r1.addView(spinner, new LayoutParams(-1, -2));
            this._takePhotoDialog = new Builder(this).setTitle("Add Photo").setNegativeButton(17039360, null).setPositiveButton("Take photo", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    String str = Environment.getExternalStorageDirectory() + "/MyMealMate";
                    File file = new File(str);
                    if (!file.exists()) {
                        file.mkdir();
                    }
                    FoodLog.this._takePhotoPath = System.currentTimeMillis() + ".jpg";
                    Parcelable fromFile = Uri.fromFile(new File(new StringBuilder(String.valueOf(str)).append("/").append(FoodLog.this._takePhotoPath).toString()));
                    Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                    intent.putExtra("output", fromFile);
                    FoodLog.this.startActivityForResult(intent, 0);
                }
            }).setView(r1).create();
            return this._takePhotoDialog;
        } else if (i == DIALOG_ADD_EXERCISE) {
            this._exerciseDialog = new ExerciseDialog(this);
            return this._exerciseDialog;
        } else if (i == DIALOG_EDIT_EXERCISE) {
            this._exerciseDialog = new ExerciseDialog(this, this._exerciseId);
            return this._exerciseDialog;
        } else if (i == DIALOG_ADD_FAVOURITE) {
            this.addFavouriteDialog_ = new AddFavouriteDialog(this);
            return this.addFavouriteDialog_;
        } else if (i == 13) {
            this.selectFavouriteDialog_ = new SelectFavouriteDialog(this);
            return this.selectFavouriteDialog_;
        } else if (i == DIALOG_COMPLETE_DAY) {
            return new Builder(this).setTitle(R.string.complete_day_menu).setMessage(getString(R.string.complete_day_text)).setIcon(17301543).setPositiveButton(17039370, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    DateFlagsData dateFlagsData = new DateFlagsData(FoodLog._db);
                    dateFlagsData.date = DateProvider.getInstance().getStartOfDay().getTime();
                    dateFlagsData.complete = true;
                    dateFlagsData.create();
                    FoodLog.this._dayCompleted = true;
                    FoodLog.this.setTitle();
                    new WSHelper().uploadAllAsync(FoodLog._db);
                }
            }).setNegativeButton(17039360, null).show();
        } else {
            if (i == DIALOG_SELECT_RECENT) {
                r2 = new LinearLayout(this);
                r2.setOrientation(DIALOG_PICKUP_DATE);
                r2.setHorizontalGravity(3);
                r2.setPadding(5, 0, 5, 0);
                this.recentItemsSpinner = new Spinner(this);
                this.recentItemsSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long j) {
                        FoodLog.this.recentItemId = j;
                    }

                    public void onNothingSelected(AdapterView<?> adapterView) {
                    }
                });
                r2.addView(this.recentItemsSpinner, new LayoutParams(-1, -2));
                r1 = new TextView(this);
                r1.setPadding(10, 10, 0, 0);
                r1.setText(R.string.select_mealslot);
                r2.addView(r1, new LayoutParams(-2, -2));
                this.recentItemMealslot = DIALOG_PICKUP_DATE;
                View radioGroup = new RadioGroup(this);
                String[] stringArray = getResources().getStringArray(R.array.mealslots_array);
                int length = stringArray.length;
                for (int i3 = 0; i3 < length; i3 += DIALOG_PICKUP_DATE) {
                    CharSequence charSequence = stringArray[i3];
                    View radioButton = new RadioButton(this);
                    radioButton.setText(charSequence);
                    i2 += DIALOG_PICKUP_DATE;
                    radioButton.setId(i2);
                    radioGroup.addView(radioButton);
                }
                radioGroup.check(DIALOG_PICKUP_DATE);
                radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
                    public void onCheckedChanged(RadioGroup radioGroup, int i) {
                        FoodLog.this.recentItemMealslot = i;
                    }
                });
                r2.addView(radioGroup, new LayoutParams(-2, -2));
                this.recentItemsDialog = new Builder(this).setTitle("Select Item").setNegativeButton(17039360, null).setPositiveButton("Select", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (FoodLog.this.recentItemId != 0) {
                            FoodLog.this.foodEntry = new FoodEntryData(FoodLog._db);
                            FoodLog._current_edit_view = null;
                            FoodLog.this.storeFoodEntry(FoodLog.this.recentItemMealslot, 1.0f, FoodLog.this.recentItemId, 0.0f);
                            FoodLog.this.populateLayout();
                            FoodLog.this.recentItemId = 0;
                            FoodLog.this.showDialog(11);
                        }
                    }
                }).setView(r2).create();
                return this.recentItemsDialog;
            }
        }
        return null;
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.food_log_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.favouriteItem /*2131361947*/:
                showDialog(DIALOG_ADD_FAVOURITE);
                return true;
            case R.id.recentItem /*2131361948*/:
                showDialog(DIALOG_SELECT_RECENT);
                break;
            case R.id.addPhoto /*2131361949*/:
                showDialog(DIALOG_ADD_PHOTO);
                return true;
            case R.id.completeDay /*2131361950*/:
                showDialog(DIALOG_COMPLETE_DAY);
                return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    protected void onPrepareDialog(int i, Dialog dialog) {
        super.onPrepareDialog(i, dialog);
        if (i == DIALOG_PICKUP_DATE) {
            DateProvider instance = DateProvider.getInstance();
            ((DatePickerDialog) dialog).updateDate(instance.getYear(), instance.getMonth(), instance.getDate());
        } else if (i == DIALOG_EDIT_EXERCISE) {
            this._exerciseDialog.setId(this._exerciseId);
        } else if (i == DIALOG_ADD_FAVOURITE) {
            this.addFavouriteDialog_.addMealSlots(DateProvider.getInstance().getDateTime());
        } else if (i == 13) {
            this.selectFavouriteDialog_.prepareDialog(this.foodEntry.mealSlot);
        } else if (i == DIALOG_SELECT_RECENT) {
            Cursor recentEnries = FoodEntryData.getRecentEnries(_db);
            String[] strArr = new String[DIALOG_PICKUP_DATE];
            strArr[0] = "Description";
            int[] iArr = new int[DIALOG_PICKUP_DATE];
            iArr[0] = 16908308;
            SpinnerAdapter simpleCursorAdapter = new SimpleCursorAdapter(this, 17367048, recentEnries, strArr, iArr);
            simpleCursorAdapter.setDropDownViewResource(R.layout.spinner_drop_down);
            this.recentItemsSpinner.setAdapter(simpleCursorAdapter);
        }
    }

    public boolean onPrepareOptionsMenu(Menu menu) {
        boolean z = false;
        menu.getItem(DIALOG_PICKUP_DATE).setEnabled(!this._dayCompleted);
        MenuItem item = menu.getItem(2);
        if (!this._dayCompleted) {
            z = true;
        }
        item.setEnabled(z);
        return true;
    }

    public void onResume() {
        super.onResume();
        Prefs prefs = new Prefs(_db, this);
        if (prefs.getWeight() == 0.0f || prefs.getHeight() == 0.0f || prefs.getPalValue() == 0.0f || prefs.getDateOfBirth().equals(XmlPullParser.NO_NAMESPACE)) {
            new Builder(this).setTitle(R.string.weight_entry_title).setMessage(getString(R.string.enter_profile_weight_message)).setIcon(17301543).setPositiveButton(17039370, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    FoodLog.this.startActivity(new Intent(FoodLog.this, Profile.class));
                }
            }).show();
        } else {
            populateLayout();
        }
    }

    public void populateLayout() {
        int[] iArr = new int[]{R.id.breakfastListLinearLayout, R.id.lunchListLinearLayout, R.id.supperListLinearLayout, R.id.snacksListLinearLayout, R.id.exerciseListLinearLayout};
        _current_edit_view = null;
        DateFlagsData.Cursor byDate = DateFlagsData.getByDate(_db, DateProvider.getInstance().getStartOfDay());
        this._dayCompleted = byDate.getCount() > 0;
        byDate.close();
        TextView textView = (TextView) findViewById(R.id.goalTextView);
        Object[] objArr = new Object[DIALOG_PICKUP_DATE];
        objArr[0] = Float.valueOf(TargetSettings.getGoal(_db, DateProvider.getInstance().getDateTime()));
        textView.setText(String.format("%.0f", objArr));
        int length = iArr.length;
        for (int i = 0; i < length; i += DIALOG_PICKUP_DATE) {
            ((LinearLayout) findViewById(iArr[i])).removeAllViews();
        }
        Iterator it = FoodEntryData.getArrayByDate(_db, DateProvider.getInstance()).iterator();
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
            if (foodEntryData.mealSlot > 0) {
                View addItemToLayout = addItemToLayout(iArr[foodEntryData.mealSlot - 1], str, f, foodEntryData.foodItem, foodEntryData.id, foodEntryData);
                if (this.recentItemId > 0 && this.foodEntry.id == foodEntryData.id && foodEntryData.calories == 0.0f) {
                    _current_edit_view = addItemToLayout;
                }
            }
        }
        ExerciseEntryData.Cursor byDate2 = ExerciseEntryData.getByDate(_db, DateProvider.getInstance());
        for (int i2 = 0; i2 < byDate2.getCount(); i2 += DIALOG_PICKUP_DATE) {
            byDate2.moveToPosition(i2);
            addItemToLayout(R.id.exerciseListLinearLayout, byDate2.getDescription(), byDate2.getCals().floatValue(), byDate2.getEntryId(), 0, null);
        }
        byDate2.close();
        updateTotalCalories();
        setTitle();
    }

    public void storeFoodEntry(int i, float f, long j, float f2) {
        this.foodEntry.foodItem = j;
        this.foodEntry.date = DateProvider.getInstance().getDateTime();
        this.foodEntry.amount = f;
        this.foodEntry.calories = f2;
        this.foodEntry.mealSlot = i;
        if (this.foodEntry.id == 0) {
            this.foodEntry.id = this.foodEntry.create();
            return;
        }
        this.foodEntry.update();
    }
}