package com.mymealmate;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher.ViewFactory;
import com.mymealmate.MyMealMateDatabase.MyMealMateCursor;
import java.util.HashMap;
import java.util.Map;
import org.xmlpull.v1.XmlPullParser;

public abstract class FoodSearchActivity extends Activity implements ViewFactory, OnClickListener {
    protected static final int DIALOG_ADD_FOOD = 10;
    protected static final int DIALOG_EDIT_FOOD = 11;
    protected static final int DIALOG_SEARCH_BRAND = 12;
    protected static final int DIALOG_SELECT_FAVOURITE = 13;
    protected static View _current_edit_view;
    public static MyMealMateDatabase _db;
    protected final int CAL_ID;
    protected final int FOODID_ID;
    protected final int MEAL_ID;
    protected Dialog _addEditDialog;
    protected ArrayAdapter<String> _brandAdapter;
    protected Dialog _brandDialog;
    protected EditText _brandEdit;
    protected Map<String, Integer> _brandIdMap;
    protected Spinner _brandSpinner;
    protected final String _caloriesOutputFormat;
    protected TextSwitcher _caloriesTextSwitcher;
    protected TextView _caloriesTextView;
    private DialogInterface.OnClickListener _favouriteClickListener;
    protected ArrayAdapter<String> _foodAdapter;
    protected Map<String, Float> _foodCaloriesMap;
    protected MyMealMateCursor _foodCursor;
    protected EditText _foodFilterEdit;
    protected Map<String, Integer> _foodIdMap;
    protected Map<String, String> _foodServingMap;
    protected Map<String, Float> _foodSizeMap;
    protected TextView _foodSizeTextView;
    protected Spinner _foodSpinner;
    protected final String _foundItemsFormat;
    protected TextView _foundItemsTextView;
    protected final String _outputFormat;
    protected EditText _portionEdit;
    protected EditText _portionInGramsEdit;
    private TextWatcher _portionInGramsTextWatcher;
    private TextWatcher _portionTextWatcher;
    protected EditText _searchBrandEdit;
    protected Button _searchFoodButton;
    protected String _selectedBrand;
    protected int _selectedBrandId;
    protected TextSwitcher _servingTextSwitcher;
    protected Button _showButton;
    protected ArrayAdapter<String> _subBrandAdapter;
    protected Map<String, Integer> _subBrandIdMap;
    protected Spinner _subBrandSpinner;
    private DialogInterface.OnClickListener changeFoodClickListener;
    private DialogInterface.OnClickListener deleteFoodClickListener;
    protected FoodEntryData foodEntry;
    private Builder manyResultsDialog_;
    protected final int maxRecordsForPopulating_;
    private DialogInterface.OnClickListener saveClickListener;

    static {
        _current_edit_view = null;
        _db = null;
    }

    public FoodSearchActivity() {
        this.MEAL_ID = 1;
        this.CAL_ID = 2;
        this.FOODID_ID = 3;
        this.maxRecordsForPopulating_ = 2000;
        this._caloriesOutputFormat = "%.0f kcal";
        this._outputFormat = "%.0f";
        this._foundItemsFormat = "%s results found";
        this._addEditDialog = null;
        this._brandDialog = null;
        this._brandEdit = null;
        this._foodFilterEdit = null;
        this._portionEdit = null;
        this._portionInGramsEdit = null;
        this._foundItemsTextView = null;
        this._caloriesTextView = null;
        this._caloriesTextSwitcher = null;
        this._servingTextSwitcher = null;
        this._foodSizeTextView = null;
        this._searchBrandEdit = null;
        this._subBrandSpinner = null;
        this._foodSpinner = null;
        this._searchFoodButton = null;
        this._showButton = null;
        this._brandSpinner = null;
        this._selectedBrand = XmlPullParser.NO_NAMESPACE;
        this._selectedBrandId = -1;
        this._subBrandAdapter = null;
        this._foodAdapter = null;
        this._brandAdapter = null;
        this._brandIdMap = new HashMap();
        this._foodCaloriesMap = new HashMap();
        this._foodSizeMap = new HashMap();
        this._foodServingMap = new HashMap();
        this._subBrandIdMap = new HashMap();
        this._foodIdMap = new HashMap();
        this._foodCursor = null;
        this.saveClickListener = null;
        this.changeFoodClickListener = null;
        this.deleteFoodClickListener = null;
        this._favouriteClickListener = null;
        this._portionTextWatcher = null;
        this._portionInGramsTextWatcher = null;
        this.manyResultsDialog_ = null;
        this.foodEntry = null;
    }

    private void performStartFoodSearchClick() {
        if (this._searchFoodButton != null) {
            this._searchFoodButton.performClick();
        }
    }

    private void resetLabels(int i) {
        this._foodAdapter.clear();
        this._servingTextSwitcher.setText(XmlPullParser.NO_NAMESPACE);
        this._foodSizeTextView.setText("0.0");
        this._caloriesTextView.setText(String.format("%.0f", new Object[]{Float.valueOf(0.0f)}));
        this._portionInGramsEdit.setText("0");
        this._portionEdit.setText("1.0");
        this._foundItemsTextView.setText(String.format("%s results found", new Object[]{Integer.toString(i)}));
        updateCaloriesUpToPortion();
    }

    private void resetLabes() {
        resetLabels(0);
    }

    private void updateCaloriesText() {
        float floatValue;
        CharSequence charSequence;
        float f = 0.0f;
        String str = new String();
        if (((TextView) this._foodSpinner.getSelectedView()) != null) {
            String str2 = (String) this._foodAdapter.getItem(this._foodSpinner.getSelectedItemPosition());
            f = ((Float) this._foodCaloriesMap.get(str2)).floatValue();
            floatValue = ((Float) this._foodSizeMap.get(str2)).floatValue();
            charSequence = (String) this._foodServingMap.get(str2);
        } else {
            Object obj = str;
            floatValue = 0.0f;
        }
        this._servingTextSwitcher.setText(charSequence);
        this._foodSizeTextView.setText(String.valueOf(floatValue));
        try {
            float parseFloat = this._portionEdit.getTag() == null ? Float.parseFloat(this._portionEdit.getText().toString()) : ((Float) this._portionEdit.getTag()).floatValue();
            this._portionInGramsEdit.setText(String.format("%.0f", new Object[]{Float.valueOf(parseFloat * floatValue)}));
        } catch (NumberFormatException e) {
            this._portionInGramsEdit.setText(String.format("%.0f", new Object[]{Float.valueOf(floatValue)}));
        }
        this._caloriesTextView.setText(String.format("%.0f", new Object[]{Float.valueOf(f)}));
        updateCaloriesUpToPortion();
    }

    private void updateCaloriesUpToPortion() {
        String charSequence = this._caloriesTextView.getText().toString();
        try {
            float calculateCalories = calculateCalories(Float.parseFloat(this._foodSizeTextView.getText().toString()), Float.parseFloat(charSequence), Float.parseFloat(this._portionEdit.getTag() == null ? this._portionEdit.getText().toString() : String.valueOf((Float) this._portionEdit.getTag())));
            if (Float.isNaN(calculateCalories)) {
                calculateCalories = 0.0f;
            }
            this._caloriesTextSwitcher.setText(String.format("%.0f kcal", new Object[]{Float.valueOf(calculateCalories)}));
            this._caloriesTextSwitcher.setTag(Float.valueOf(calculateCalories));
        } catch (NumberFormatException e) {
            this._caloriesTextSwitcher.setText(String.format("%.0f kcal", new Object[]{Float.valueOf(0.0f)}));
            this._caloriesTextSwitcher.setTag(Float.valueOf(0.0f));
        }
    }

    private void updateFoodSpinner() {
        if (this._foodCursor != null) {
            this._foodAdapter.clear();
            this._foodIdMap.clear();
            this._foodCaloriesMap.clear();
            this._foodSizeMap.clear();
            this._foodServingMap.clear();
            if (this._foodCursor.getCount() <= 2000 || this._showButton.getText().toString() != getResources().getString(R.string.more)) {
                for (int i = 0; i < this._foodCursor.getCount(); i++) {
                    this._foodCursor.moveToPosition(i);
                    MyMealMateCursor brandCursorById = _db.getBrandCursorById(this._foodCursor.getBrandId());
                    String str = "Generic";
                    if (brandCursorById.getCount() != 0) {
                        str = brandCursorById.getBrandName();
                    }
                    str = new StringBuilder(String.valueOf(this._foodCursor.getFoodDescription())).append(" (").append(str).append(")").toString();
                    this._foodAdapter.add(str);
                    this._foodIdMap.put(str, new Integer(this._foodCursor.getFoodId()));
                    this._foodCaloriesMap.put(str, new Float(this._foodCursor.getCalories()));
                    this._foodSizeMap.put(str, new Float(this._foodCursor.getSize()));
                    this._foodServingMap.put(str, this._foodCursor.getServing());
                }
                if (this._foodAdapter.getCount() > 0) {
                    this._foodSpinner.setEnabled(true);
                } else {
                    this._foodSpinner.setEnabled(false);
                }
                this._foodSpinner.setSelection(0);
                updateCaloriesText();
                return;
            }
            this._showButton.setText(getResources().getString(R.string.show));
            this.manyResultsDialog_.show();
        }
    }

    private void updateSubBrandSpinner() {
        this._subBrandSpinner.setEnabled(false);
        this._subBrandAdapter.clear();
        this._subBrandIdMap.clear();
        MyMealMateCursor subBrandCursor = _db.getSubBrandCursor(this._selectedBrandId);
        this._subBrandAdapter.add("All");
        this._subBrandIdMap.put("All", new Integer(-1));
        this._subBrandSpinner.setSelection(0);
        if (subBrandCursor.getCount() == 0) {
            this._subBrandSpinner.setEnabled(false);
        } else {
            this._subBrandSpinner.setEnabled(true);
            for (int i = 0; i < subBrandCursor.getCount(); i++) {
                subBrandCursor.moveToPosition(i);
                this._subBrandAdapter.add(subBrandCursor.getSubbrandName());
                this._subBrandIdMap.put(subBrandCursor.getSubbrandName(), new Integer(subBrandCursor.getSubbrandId()));
            }
        }
        subBrandCursor.close();
        this._subBrandSpinner.setSelection(0);
    }

    protected float calculateCalories(float f, float f2, float f3) {
        return ((f / 100.0f) * f2) * f3;
    }

    public View makeView() {
        View textView = new TextView(this);
        textView.setGravity(3);
        textView.setTextColor(getResources().getColor(R.color.solid_white));
        return textView;
    }

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        requestWindowFeature(1);
        this._brandAdapter = new ArrayAdapter(this, 17367048);
        this._foodAdapter = new ArrayAdapter(this, 17367048);
        this._subBrandAdapter = new ArrayAdapter(this, 17367048);
        this._foodAdapter.setDropDownViewResource(R.layout.spinner_drop_down);
        this._subBrandAdapter.setDropDownViewResource(17367049);
        this._brandAdapter.setDropDownViewResource(17367049);
        _db = new MyMealMateDatabase(this);
        this.manyResultsDialog_ = new Builder(this).setTitle(R.string.food_search_warning_title).setMessage(getString(R.string.food_search_warning_message)).setIcon(17301543).setPositiveButton(17039370, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                FoodSearchActivity.this._showButton.setText(FoodSearchActivity.this.getString(R.string.show));
            }
        });
    }

    protected Dialog onCreateDialog(int i) {
        if (i != DIALOG_ADD_FOOD && i != DIALOG_EDIT_FOOD) {
            if (i == DIALOG_SEARCH_BRAND) {
                View inflate = LayoutInflater.from(this).inflate(R.layout.brand_search, null);
                this._searchBrandEdit = (EditText) inflate.findViewById(R.id.brandEditText);
                this._brandSpinner = (Spinner) inflate.findViewById(R.id.brandSpinner);
                this._brandSpinner.setAdapter(this._brandAdapter);
                ((Button) inflate.findViewById(R.id.searchBrandButton)).setOnClickListener(new OnClickListener() {
                    public void onClick(View view) {
                        if (FoodSearchActivity.this._searchBrandEdit == null || FoodSearchActivity.this._searchBrandEdit.getText().length() == 0) {
                            ((InputMethodManager) FoodSearchActivity.this.getSystemService("input_method")).showSoftInput(FoodSearchActivity.this._searchBrandEdit, 0);
                            return;
                        }
                        MyMealMateCursor searchBrandCursor = FoodSearchActivity._db.getSearchBrandCursor(FoodSearchActivity.this._searchBrandEdit.getText().toString().replaceAll("'", "''"));
                        int count = searchBrandCursor.getCount();
                        if (count == 1) {
                            FoodSearchActivity.this._selectedBrand = searchBrandCursor.getBrandName();
                            FoodSearchActivity.this._selectedBrandId = searchBrandCursor.getBrandId();
                            FoodSearchActivity.this._brandEdit.setText(FoodSearchActivity.this._selectedBrand);
                            FoodSearchActivity.this.resetLabes();
                            FoodSearchActivity.this.updateSubBrandSpinner();
                            searchBrandCursor.close();
                            FoodSearchActivity.this.dismissDialog(FoodSearchActivity.DIALOG_SEARCH_BRAND);
                            FoodSearchActivity.this.performStartFoodSearchClick();
                        } else {
                            FoodSearchActivity.this._brandAdapter.add("All brands");
                            FoodSearchActivity.this._brandIdMap.put("All brands", new Integer(-1));
                            for (int i = 0; i < count; i++) {
                                searchBrandCursor.moveToPosition(i);
                                FoodSearchActivity.this._brandAdapter.add(searchBrandCursor.getBrandName());
                                FoodSearchActivity.this._brandIdMap.put(searchBrandCursor.getBrandName(), new Integer(searchBrandCursor.getBrandId()));
                            }
                        }
                        searchBrandCursor.close();
                        ((InputMethodManager) FoodSearchActivity.this.getSystemService("input_method")).hideSoftInputFromWindow(FoodSearchActivity.this._searchBrandEdit.getWindowToken(), 0);
                    }
                });
                this._brandDialog = new Builder(this).setTitle("Search brand").setView(inflate).setPositiveButton(R.string.alert_dialog_save, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        TextView textView = (TextView) FoodSearchActivity.this._brandSpinner.getSelectedView();
                        if (textView != null) {
                            FoodSearchActivity.this._selectedBrand = textView.getText().toString();
                            FoodSearchActivity.this._selectedBrandId = ((Integer) FoodSearchActivity.this._brandIdMap.get(FoodSearchActivity.this._selectedBrand)).intValue();
                            FoodSearchActivity.this._brandEdit.setText(FoodSearchActivity.this._selectedBrand);
                            FoodSearchActivity.this.resetLabes();
                            FoodSearchActivity.this.updateSubBrandSpinner();
                            FoodSearchActivity.this.performStartFoodSearchClick();
                        }
                    }
                }).setNegativeButton(R.string.alert_dialog_cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                }).create();
            }
            return null;
        } else if (this._addEditDialog != null) {
            return this._addEditDialog;
        } else {
            View inflate2 = LayoutInflater.from(this).inflate(R.layout.add_food_form, null);
            this._brandEdit = (EditText) inflate2.findViewById(R.id.brandEditText);
            this._brandEdit.setInputType(0);
            this._portionTextWatcher = new TextWatcher() {
                public void afterTextChanged(Editable editable) {
                }

                public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                }

                public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                    FoodSearchActivity.this.updateCaloriesUpToPortion();
                    FoodSearchActivity.this._portionInGramsEdit.removeTextChangedListener(FoodSearchActivity.this._portionInGramsTextWatcher);
                    FoodSearchActivity.this._portionEdit.setTag(null);
                    try {
                        String charSequence2 = FoodSearchActivity.this._foodSizeTextView.getText().toString();
                        String editable = FoodSearchActivity.this._portionEdit.getText().toString();
                        float parseFloat = Float.parseFloat(charSequence2);
                        float parseFloat2 = Float.parseFloat(editable);
                        FoodSearchActivity.this._portionInGramsEdit.setText(String.format("%.0f", new Object[]{Float.valueOf(parseFloat * parseFloat2)}));
                    } catch (NumberFormatException e) {
                        FoodSearchActivity.this._portionInGramsEdit.setText(XmlPullParser.NO_NAMESPACE);
                    }
                    FoodSearchActivity.this._portionInGramsEdit.addTextChangedListener(FoodSearchActivity.this._portionInGramsTextWatcher);
                }
            };
            this._portionInGramsTextWatcher = new TextWatcher() {
                public void afterTextChanged(Editable editable) {
                }

                public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                }

                public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                    FoodSearchActivity.this._portionEdit.removeTextChangedListener(FoodSearchActivity.this._portionTextWatcher);
                    try {
                        float parseFloat = Float.parseFloat(FoodSearchActivity.this._portionInGramsEdit.getText().toString()) / Float.parseFloat(FoodSearchActivity.this._foodSizeTextView.getText().toString());
                        FoodSearchActivity.this._portionEdit.setText(String.format("%.1f", new Object[]{Float.valueOf(parseFloat)}));
                        FoodSearchActivity.this._portionEdit.setTag(Float.valueOf(parseFloat));
                    } catch (NumberFormatException e) {
                        FoodSearchActivity.this._portionEdit.setText(XmlPullParser.NO_NAMESPACE);
                    }
                    FoodSearchActivity.this.updateCaloriesUpToPortion();
                    FoodSearchActivity.this._portionEdit.addTextChangedListener(FoodSearchActivity.this._portionTextWatcher);
                }
            };
            this._portionInGramsEdit = (EditText) inflate2.findViewById(R.id.gramsEditText);
            this._portionInGramsEdit.addTextChangedListener(this._portionInGramsTextWatcher);
            this._portionEdit = (EditText) inflate2.findViewById(R.id.numberEditText);
            this._portionEdit.addTextChangedListener(this._portionTextWatcher);
            this._foodFilterEdit = (EditText) inflate2.findViewById(R.id.foodFilterEditText);
            this._foundItemsTextView = (TextView) inflate2.findViewById(R.id.foundItemsText);
            Animation loadAnimation = AnimationUtils.loadAnimation(this, 17432576);
            Animation loadAnimation2 = AnimationUtils.loadAnimation(this, 17432577);
            this._caloriesTextSwitcher = (TextSwitcher) inflate2.findViewById(R.id.caloriesTextSwitcher);
            this._caloriesTextSwitcher.setFactory(this);
            this._caloriesTextSwitcher.setInAnimation(loadAnimation);
            this._caloriesTextSwitcher.setOutAnimation(loadAnimation2);
            this._servingTextSwitcher = (TextSwitcher) inflate2.findViewById(R.id.servingTextSwitcher);
            this._servingTextSwitcher.setFactory(this);
            this._servingTextSwitcher.setInAnimation(loadAnimation);
            this._servingTextSwitcher.setOutAnimation(loadAnimation2);
            this._foodSizeTextView = (TextView) inflate2.findViewById(R.id.sizeTextView);
            this._caloriesTextView = (TextView) inflate2.findViewById(R.id.caloriesTextView);
            this._subBrandSpinner = (Spinner) inflate2.findViewById(R.id.subBrandSpinner);
            this._subBrandSpinner.setEnabled(false);
            this._subBrandSpinner.setAdapter(this._subBrandAdapter);
            this._subBrandSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long j) {
                    FoodSearchActivity.this._foodAdapter.clear();
                    FoodSearchActivity.this._foodCaloriesMap.clear();
                    FoodSearchActivity.this._foodSizeMap.clear();
                    FoodSearchActivity.this._foodServingMap.clear();
                    FoodSearchActivity.this._foodSpinner.setEnabled(false);
                    FoodSearchActivity.this.resetLabes();
                    FoodSearchActivity.this.performStartFoodSearchClick();
                }

                public void onNothingSelected(AdapterView<?> adapterView) {
                }
            });
            this._foodSpinner = (Spinner) inflate2.findViewById(R.id.foodSpinner);
            this._foodSpinner.setEnabled(false);
            this._foodSpinner.setAdapter(this._foodAdapter);
            this._foodSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long j) {
                    FoodSearchActivity.this.updateCaloriesText();
                }

                public void onNothingSelected(AdapterView<?> adapterView) {
                }
            });
            this._searchFoodButton = (Button) inflate2.findViewById(R.id.searchFoodButton);
            this._searchFoodButton.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    InputMethodManager inputMethodManager = (InputMethodManager) FoodSearchActivity.this.getSystemService("input_method");
                    if (FoodSearchActivity.this._foodFilterEdit.getText().toString().length() == 0 && FoodSearchActivity.this._selectedBrandId == -1) {
                        inputMethodManager.showSoftInput(FoodSearchActivity.this._foodFilterEdit, 0);
                        return;
                    }
                    int intValue;
                    inputMethodManager.hideSoftInputFromWindow(FoodSearchActivity.this._foodFilterEdit.getWindowToken(), 0);
                    if (FoodSearchActivity.this._selectedBrandId != -1) {
                        String str = (String) FoodSearchActivity.this._subBrandAdapter.getItem(FoodSearchActivity.this._subBrandSpinner.getSelectedItemPosition());
                        if (str.length() != 0) {
                            intValue = ((Integer) FoodSearchActivity.this._subBrandIdMap.get(str)).intValue();
                        } else {
                            return;
                        }
                    }
                    intValue = -1;
                    if (FoodSearchActivity.this._foodCursor != null) {
                        FoodSearchActivity.this._foodCursor.close();
                    }
                    String replaceAll = FoodSearchActivity.this._foodFilterEdit.getText().toString().trim().replaceAll("'", "''");
                    String str2 = "desc LIKE '%%'";
                    String str3 = "desc LIKE '%%'";
                    if (!replaceAll.equals(XmlPullParser.NO_NAMESPACE)) {
                        str2 = XmlPullParser.NO_NAMESPACE;
                        str3 = XmlPullParser.NO_NAMESPACE;
                        for (String str4 : replaceAll.split(" ")) {
                            if (str4.trim().length() > 0) {
                                if (!str2.equals(XmlPullParser.NO_NAMESPACE)) {
                                    str2 = new StringBuilder(String.valueOf(new StringBuilder(String.valueOf(str2)).append(" AND ").toString())).append("desc LIKE '%").append(str4.trim()).append("%'").toString();
                                    str3 = new StringBuilder(String.valueOf(new StringBuilder(String.valueOf(str3)).append(" AND ").toString())).append("desc LIKE '%").append(str4.trim()).append("%'").toString();
                                } else if (FoodSearchActivity.this._searchFoodButton.getTag() == null || FoodSearchActivity.this._showButton.getText().toString() == FoodSearchActivity.this.getResources().getString(R.string.show)) {
                                    str2 = "desc LIKE '" + str4.trim() + "%'";
                                    str3 = "desc LIKE '" + str4.trim() + "%,%'";
                                } else {
                                    str2 = "desc LIKE '%" + str4.trim() + "%'";
                                    str3 = str2;
                                }
                            }
                        }
                    }
                    FoodSearchActivity.this._searchFoodButton.setTag(null);
                    FoodSearchActivity.this._foodSpinner.setTag(null);
                    FoodSearchActivity.this._foodCursor = FoodSearchActivity._db.getFoodCursor(FoodSearchActivity.this._selectedBrandId, intValue, str2);
                    if (FoodSearchActivity.this._foodCursor.getCount() == 0) {
                        FoodSearchActivity.this._foodCursor.close();
                        FoodSearchActivity.this._foodCursor = FoodSearchActivity._db.getFoodCursor(FoodSearchActivity.this._selectedBrandId, intValue, str3);
                    }
                    intValue = FoodSearchActivity.this._foodCursor.getCount();
                    if (intValue > 0) {
                        FoodSearchActivity.this._showButton.setEnabled(true);
                        FoodSearchActivity.this._showButton.setText(FoodSearchActivity.this.getResources().getString(R.string.show));
                        FoodSearchActivity.this._foodAdapter.clear();
                        FoodSearchActivity.this._foodCaloriesMap.clear();
                        FoodSearchActivity.this._foodSizeMap.clear();
                        FoodSearchActivity.this._foodServingMap.clear();
                        FoodSearchActivity.this._foodSpinner.setEnabled(false);
                    } else {
                        FoodSearchActivity.this._showButton.setEnabled(true);
                        FoodSearchActivity.this._showButton.setText(FoodSearchActivity.this.getResources().getString(R.string.more));
                        FoodSearchActivity.this._foodAdapter.clear();
                        FoodSearchActivity.this._foodCaloriesMap.clear();
                        FoodSearchActivity.this._foodSizeMap.clear();
                        FoodSearchActivity.this._foodServingMap.clear();
                        FoodSearchActivity.this._foodSpinner.setEnabled(false);
                    }
                    FoodSearchActivity.this.resetLabels(intValue);
                }
            });
            this._showButton = (Button) inflate2.findViewById(R.id.showButton);
            this._showButton.setEnabled(false);
            this._showButton.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    if (FoodSearchActivity.this._showButton.getText().toString() != FoodSearchActivity.this.getResources().getString(R.string.show)) {
                        FoodSearchActivity.this._searchFoodButton.setTag(new Object());
                        FoodSearchActivity.this._searchFoodButton.performClick();
                        FoodSearchActivity.this._showButton.setText(FoodSearchActivity.this.getResources().getString(R.string.show));
                        FoodSearchActivity.this._foodSpinner.setTag(new Object());
                        FoodSearchActivity.this.updateFoodSpinner();
                        FoodSearchActivity.this._portionEdit.setText("1.0");
                    } else if (FoodSearchActivity.this._foodSpinner.getTag() == null) {
                        FoodSearchActivity.this._showButton.setText(FoodSearchActivity.this.getResources().getString(R.string.more));
                        FoodSearchActivity.this.updateFoodSpinner();
                        FoodSearchActivity.this._portionEdit.setText("1.0");
                    } else {
                        FoodSearchActivity.this._foodSpinner.performClick();
                    }
                }
            });
            this._brandEdit.setOnTouchListener(new OnTouchListener() {
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    FoodSearchActivity.this.showDialog(FoodSearchActivity.DIALOG_SEARCH_BRAND);
                    return false;
                }
            });
            this.saveClickListener = new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    Spinner spinner = (Spinner) ((Dialog) dialogInterface).findViewById(R.id.foodSpinner);
                    float floatValue = ((Float) FoodSearchActivity.this._caloriesTextSwitcher.getTag()).floatValue();
                    if (spinner.getSelectedItem() != null) {
                        FoodSearchActivity.this.storeFoodEntry(FoodSearchActivity.this.foodEntry.mealSlot, FoodSearchActivity.this._portionEdit.getTag() == null ? Float.parseFloat(FoodSearchActivity.this._portionEdit.getText().toString()) : ((Float) FoodSearchActivity.this._portionEdit.getTag()).floatValue(), (long) ((Integer) FoodSearchActivity.this._foodIdMap.get((String) spinner.getSelectedItem())).intValue(), floatValue);
                        FoodSearchActivity.this.populateLayout();
                    }
                }
            };
            this.changeFoodClickListener = new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    String str = (String) ((Spinner) ((Dialog) dialogInterface).findViewById(R.id.foodSpinner)).getSelectedItem();
                    float floatValue = ((Float) FoodSearchActivity.this._caloriesTextSwitcher.getTag()).floatValue();
                    LinearLayout linearLayout = (LinearLayout) FoodSearchActivity._current_edit_view;
                    if (((int) floatValue) != 0) {
                        int parseInt = Integer.parseInt(((TextView) linearLayout.findViewById(3)).getText().toString());
                        if (FoodSearchActivity.this._foodIdMap.containsKey(str)) {
                            parseInt = ((Integer) FoodSearchActivity.this._foodIdMap.get(str)).intValue();
                        }
                        ((TextView) linearLayout.findViewById(3)).setText(Integer.toString(parseInt));
                        float parseFloat = FoodSearchActivity.this._portionEdit.getTag() == null ? Float.parseFloat(FoodSearchActivity.this._portionEdit.getText().toString()) : ((Float) FoodSearchActivity.this._portionEdit.getTag()).floatValue();
                        FoodSearchActivity.this.foodEntry.foodItem = (long) parseInt;
                        FoodSearchActivity.this.foodEntry.amount = parseFloat;
                        FoodSearchActivity.this.foodEntry.calories = floatValue;
                        FoodSearchActivity.this.foodEntry.update();
                        FoodSearchActivity.this.populateLayout();
                    }
                }
            };
            this.deleteFoodClickListener = new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    new Builder(FoodSearchActivity.this).setMessage("Continue deleting?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                            FoodSearchActivity.this.foodEntry.delete();
                            FoodSearchActivity._current_edit_view = null;
                            FoodSearchActivity.this.populateLayout();
                        }
                    }).setNegativeButton("No", null).show();
                }
            };
            this._favouriteClickListener = new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    FoodSearchActivity.this.showDialog(FoodSearchActivity.DIALOG_SELECT_FAVOURITE);
                }
            };
            String str = new String();
            this._addEditDialog = new Builder(this).setTitle("-").setView(inflate2).setPositiveButton(R.string.alert_dialog_save, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            }).setNeutralButton(R.string.alert_dialog_delete, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            }).setNegativeButton(R.string.alert_dialog_cancel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            }).create();
            return this._addEditDialog;
        }
    }

    protected void onPrepareDialog(int i, Dialog dialog) {
        float f = 0.0f;
        if (i == DIALOG_SEARCH_BRAND) {
            this._brandAdapter.clear();
            ((EditText) ((AlertDialog) dialog).findViewById(R.id.brandEditText)).setText(XmlPullParser.NO_NAMESPACE);
            this._brandIdMap.clear();
        } else if (i == DIALOG_EDIT_FOOD && _current_edit_view != null) {
            float floatValue;
            float size;
            this.foodEntry = (FoodEntryData) _current_edit_view.getTag();
            this._foundItemsTextView.setText(String.format("%s results found", new Object[]{"0"}));
            this._foodAdapter.clear();
            this._subBrandAdapter.clear();
            this._foodCaloriesMap.clear();
            this._foodSizeMap.clear();
            this._foodServingMap.clear();
            this._showButton.setEnabled(false);
            this._showButton.setText(getResources().getString(R.string.show));
            this._foodSpinner.setEnabled(false);
            this._subBrandSpinner.setEnabled(false);
            this._brandEdit.setText(XmlPullParser.NO_NAMESPACE);
            this._foodFilterEdit.setText(XmlPullParser.NO_NAMESPACE);
            LinearLayout linearLayout = (LinearLayout) _current_edit_view;
            int parseInt = Integer.parseInt(((TextView) linearLayout.findViewById(3)).getText().toString());
            if (parseInt != 0) {
                MyMealMateCursor foodCursorById = _db.getFoodCursorById((long) parseInt);
                floatValue = ((Float) ((TextView) linearLayout.findViewById(2)).getTag()).floatValue();
                float calories = foodCursorById.getCalories();
                size = foodCursorById.getSize();
                f = floatValue / ((size / 100.0f) * calories);
                CharSequence serving = foodCursorById.getServing();
                MyMealMateCursor brandCursorById = _db.getBrandCursorById(foodCursorById.getBrandId());
                r0 = "Generic";
                if (brandCursorById.getCount() != 0) {
                    r0 = brandCursorById.getBrandName();
                }
                r0 = foodCursorById.getFoodDescription() + " (" + r0 + ")";
                this._foodFilterEdit.setText(foodCursorById.getFoodDescription());
                this._foodCaloriesMap.put(r0, new Float(calories));
                this._foodServingMap.put(r0, serving);
                this._foodSizeMap.put(r0, new Float(size));
                this._servingTextSwitcher.setText(serving);
                this._foodAdapter.add(r0);
                foodCursorById.close();
                floatValue = size;
                size = f;
                f = calories;
            } else {
                floatValue = 1.0f;
                size = 0.0f;
            }
            this._caloriesTextView.setText(String.valueOf(f));
            this._foodSizeTextView.setText(String.valueOf(floatValue));
            this._portionEdit.setText(String.valueOf(size));
            this._portionEdit.removeTextChangedListener(this._portionTextWatcher);
            this._portionEdit.setText(String.format("%.1f", new Object[]{Float.valueOf(size)}));
            this._portionEdit.addTextChangedListener(this._portionTextWatcher);
            this._portionEdit.setTag(Float.valueOf(size));
            updateCaloriesUpToPortion();
            ((AlertDialog) dialog).setTitle("Edit");
            ((AlertDialog) dialog).setButton(-1, getString(R.string.alert_dialog_change), this.changeFoodClickListener);
            ((AlertDialog) dialog).getButton(-1).setText(getString(R.string.alert_dialog_change));
            r0 = ((AlertDialog) dialog).getButton(-3);
            r0.setVisibility(0);
            ((AlertDialog) dialog).setButton(-3, getResources().getString(R.string.alert_dialog_delete), this.deleteFoodClickListener);
            r0.setText(getResources().getString(R.string.alert_dialog_delete));
        } else if (i == DIALOG_ADD_FOOD) {
            CharSequence charSequence;
            this._foundItemsTextView.setText(String.format("%s results found", new Object[]{"0"}));
            this._brandEdit.setText(XmlPullParser.NO_NAMESPACE);
            this._foodFilterEdit.setText(XmlPullParser.NO_NAMESPACE);
            this._subBrandAdapter.clear();
            this._foodAdapter.clear();
            this._foodCaloriesMap.clear();
            this._foodSizeMap.clear();
            this._foodServingMap.clear();
            this._showButton.setEnabled(false);
            this._showButton.setText(getString(R.string.show));
            this._subBrandSpinner.setEnabled(false);
            this._foodSpinner.setEnabled(false);
            this._caloriesTextView.setText(String.format("%.0f", new Object[]{Float.valueOf(0.0f)}));
            this._foodSizeTextView.setText("0");
            updateCaloriesUpToPortion();
            this._servingTextSwitcher.setText(XmlPullParser.NO_NAMESPACE);
            this._selectedBrandId = -1;
            this._portionInGramsEdit.setText("0");
            this._portionEdit.setText("1.0");
            this.foodEntry = new FoodEntryData(_db);
            this.foodEntry.date = DateProvider.getInstance().getDateTime();
            Object obj;
            switch (_current_edit_view.getId()) {
                case R.id.breakfastTitleLinearLayout /*2131361823*/:
                case R.id.foodListLinearLayout /*2131361825*/:
                    r0 = XmlPullParser.NO_NAMESPACE + getString(R.string.title_breakfast);
                    this.foodEntry.mealSlot = 1;
                    charSequence = r0;
                    break;
                case R.id.lunchTitleLinearLayout /*2131361875*/:
                    r0 = XmlPullParser.NO_NAMESPACE + getString(R.string.title_lunch);
                    this.foodEntry.mealSlot = 2;
                    obj = r0;
                    break;
                case R.id.supperTitleLinearLayout /*2131361880*/:
                    r0 = XmlPullParser.NO_NAMESPACE + getString(R.string.title_supper);
                    this.foodEntry.mealSlot = 3;
                    obj = r0;
                    break;
                case R.id.snacksTitleLinearLayout /*2131361885*/:
                    r0 = XmlPullParser.NO_NAMESPACE + getString(R.string.title_snacks);
                    this.foodEntry.mealSlot = 4;
                    obj = r0;
                    break;
                case R.id.exerciseTitleLinearLayout /*2131361889*/:
                    obj = XmlPullParser.NO_NAMESPACE + getString(R.string.title_exercise);
                    break;
                default:
                    return;
            }
            ((AlertDialog) dialog).setTitle(charSequence);
            ((AlertDialog) dialog).setButton(-1, getString(R.string.alert_dialog_save), this.saveClickListener);
            ((AlertDialog) dialog).getButton(-1).setText(getResources().getString(R.string.alert_dialog_save));
            r0 = ((AlertDialog) dialog).getButton(-3);
            r0.setVisibility(0);
            ((AlertDialog) dialog).setButton(-3, getString(R.string.alert_dialog_favourite), this._favouriteClickListener);
            r0.setText(getResources().getString(R.string.alert_dialog_favourite));
        }
    }

    protected void onStop() {
        _db.close();
        super.onStop();
    }

    public abstract void populateLayout();

    public abstract void storeFoodEntry(int i, float f, long j, float f2);
}