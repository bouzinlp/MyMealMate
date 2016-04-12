package com.mymealmate;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import com.mymealmate.FavouritesDatabase.FavouritesCursor;

public class SelectFavouriteDialog extends AlertDialog {
    private int currectMealSlot_;
    private View dialogView_;
    private FavouritesDatabase favDb_;
    private ArrayAdapter<String> favouriteAdapter_;
    private Spinner favouriteSpinner_;

    public SelectFavouriteDialog(Context context) {
        super(context);
        this.favouriteAdapter_ = null;
        this.dialogView_ = null;
        this.favDb_ = null;
        this.favouriteSpinner_ = null;
        this.currectMealSlot_ = -1;
        this.dialogView_ = LayoutInflater.from(context).inflate(R.layout.select_favourite_dialog, null);
        setView(this.dialogView_);
        setTitle(R.string.select_favourite_title);
        this.favouriteAdapter_ = new ArrayAdapter(context, 17367048);
        this.favouriteAdapter_.setDropDownViewResource(17367049);
        this.favouriteSpinner_ = (Spinner) this.dialogView_.findViewById(R.id.favouriteSpinner);
        this.favouriteSpinner_.setAdapter(this.favouriteAdapter_);
        setButton(-1, context.getResources().getString(R.string.alert_dialog_save), new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                if (SelectFavouriteDialog.this.currectMealSlot_ != -1) {
                    FoodLog foodLog = (FoodLog) SelectFavouriteDialog.this.dialogView_.getContext();
                    TextView textView = (TextView) SelectFavouriteDialog.this.favouriteSpinner_.getSelectedView();
                    if (textView != null) {
                        FavouritesCursor favouriteSlot = SelectFavouriteDialog.this.favDb_.getFavouriteSlot(textView.getText().toString());
                        for (int i2 = 0; i2 < favouriteSlot.getCount(); i2++) {
                            favouriteSlot.moveToPosition(i2);
                            FoodEntryData foodEntryData = new FoodEntryData(FoodLog._db);
                            foodEntryData.date = DateProvider.getInstance().getDateTime();
                            foodEntryData.foodItem = (long) favouriteSlot.getFoodId();
                            foodEntryData.amount = favouriteSlot.getAmount();
                            foodEntryData.mealSlot = SelectFavouriteDialog.this.currectMealSlot_;
                            foodEntryData.create();
                        }
                        favouriteSlot.close();
                        foodLog.populateLayout();
                    }
                }
            }
        });
        setButton(-2, context.getResources().getString(R.string.alert_dialog_cancel), new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        this.favDb_ = new FavouritesDatabase(context);
    }

    public void prepareDialog(int i) {
        this.currectMealSlot_ = i;
        this.favouriteAdapter_.clear();
        FavouritesCursor favourites = this.favDb_.getFavourites();
        for (int i2 = 0; i2 < favourites.getCount(); i2++) {
            favourites.moveToPosition(i2);
            this.favouriteAdapter_.add(favourites.getFavName());
        }
    }
}