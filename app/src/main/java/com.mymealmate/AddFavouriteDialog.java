package com.mymealmate;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import com.mymealmate.FoodEntryData.Cursor;
import com.mymealmate.MyMealMateDatabase.MyMealMateCursor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.xmlpull.v1.XmlPullParser;

public class AddFavouriteDialog extends AlertDialog {
    private View dialogView_;
    private FavouritesDatabase favDb_;
    private boolean hasMealToStore_;
    private Map<Integer, List<Slot>> mealSlot_;
    private RadioGroup mealSlots_;
    private MyMealMateDatabase mmmDb_;

    private class Slot {
        public float amount;
        public long foodId;

        public Slot(long j, float f) {
            this.foodId = j;
            this.amount = f;
        }
    }

    public AddFavouriteDialog(Context context) {
        super(context);
        this.dialogView_ = null;
        this.mmmDb_ = null;
        this.favDb_ = null;
        this.mealSlot_ = null;
        this.hasMealToStore_ = false;
        this.mealSlots_ = null;
        this.dialogView_ = LayoutInflater.from(context).inflate(R.layout.add_favourite_dialog, null);
        setView(this.dialogView_);
        setTitle(R.string.create_favourite_title);
        setButton(-1, context.getResources().getString(R.string.alert_dialog_save), new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                String editable = ((EditText) ((Dialog) dialogInterface).findViewById(R.id.favouriteName)).getText().toString();
                if (!AddFavouriteDialog.this.hasMealToStore_ || (AddFavouriteDialog.this.mealSlots_ != null && AddFavouriteDialog.this.mealSlots_.getCheckedRadioButtonId() == -1)) {
                    new Builder(AddFavouriteDialog.this.getContext()).setMessage("There are no meal slots to store.").setPositiveButton("Ok", new OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    }).show();
                } else if (editable.equals(XmlPullParser.NO_NAMESPACE)) {
                    new Builder(AddFavouriteDialog.this.getContext()).setMessage("You have to enter favourite's name").setPositiveButton("Ok", new OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    }).show();
                } else {
                    long insertNewFavourite;
                    if (AddFavouriteDialog.this.favDb_.isFavouriteInDB(editable)) {
                        AddFavouriteDialog.this.favDb_.deleteFavourite(editable);
                        insertNewFavourite = AddFavouriteDialog.this.favDb_.insertNewFavourite(editable);
                    } else {
                        insertNewFavourite = AddFavouriteDialog.this.favDb_.insertNewFavourite(editable);
                    }
                    Integer num = new Integer(AddFavouriteDialog.this.mealSlots_.getCheckedRadioButtonId());
                    List list = (List) AddFavouriteDialog.this.mealSlot_.get(num);
                    for (int i2 = 0; i2 < list.size(); i2++) {
                        Slot slot = (Slot) list.get(i2);
                        AddFavouriteDialog.this.favDb_.insertIntoFavourite(insertNewFavourite, num.intValue(), slot.foodId, slot.amount);
                    }
                }
            }
        });
        setButton(-2, context.getResources().getString(R.string.alert_dialog_cancel), new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        this.mmmDb_ = new MyMealMateDatabase(context);
        this.favDb_ = new FavouritesDatabase(context);
        this.mealSlot_ = new HashMap();
    }

    public void addMealSlots(long j) {
        this.mealSlots_ = (RadioGroup) this.dialogView_.findViewById(R.id.mealSlotsRadioGroup);
        Context context = this.dialogView_.getContext();
        this.mealSlots_.removeAllViews();
        this.mealSlot_.clear();
        ((EditText) this.dialogView_.findViewById(R.id.favouriteName)).setText(XmlPullParser.NO_NAMESPACE);
        this.hasMealToStore_ = false;
        String str = new String();
        for (int i = 1; i <= 4; i++) {
            View radioButton = new RadioButton(this.dialogView_.getContext());
            radioButton.setTextAppearance(context, R.style.TextStyle0);
            if (i == 1) {
                radioButton.setText(context.getResources().getString(R.string.title_breakfast));
                radioButton.setId(i);
                this.mealSlots_.addView(radioButton);
            } else if (i == 2) {
                radioButton.setText(context.getResources().getString(R.string.title_lunch));
                radioButton.setId(i);
                this.mealSlots_.addView(radioButton);
            } else if (i == 3) {
                radioButton.setText(context.getResources().getString(R.string.title_supper));
                radioButton.setId(i);
                this.mealSlots_.addView(radioButton);
            } else if (i == 4) {
                radioButton.setText(context.getResources().getString(R.string.title_snacks));
                radioButton.setId(i);
                this.mealSlots_.addView(radioButton);
            }
            List arrayList = new ArrayList();
            Cursor byDateAndMealSlot = FoodEntryData.getByDateAndMealSlot(this.mmmDb_, DateProvider.getInstance(), i);
            for (int i2 = 0; i2 < byDateAndMealSlot.getCount(); i2++) {
                byDateAndMealSlot.moveToPosition(i2);
                long foodItem = byDateAndMealSlot.getFoodItem();
                if (foodItem != 0) {
                    this.hasMealToStore_ = true;
                    float floatValue = byDateAndMealSlot.getAmount().floatValue();
                    MyMealMateCursor foodCursorById = this.mmmDb_.getFoodCursorById(foodItem);
                    String foodDescription = foodCursorById.getFoodDescription();
                    float calories = foodCursorById.getCalories();
                    float size = foodCursorById.getSize() / 100.0f;
                    foodCursorById.close();
                    arrayList.add(new Slot(foodItem, floatValue));
                    View textView = new TextView(this.dialogView_.getContext());
                    textView.setText(new StringBuilder(String.valueOf(foodDescription)).append(" - ").append(String.format("%.1f", new Object[]{Float.valueOf(floatValue * (calories * size))})).toString());
                    this.mealSlots_.addView(textView);
                }
            }
            byDateAndMealSlot.close();
            this.mealSlot_.put(new Integer(i), arrayList);
        }
    }
}