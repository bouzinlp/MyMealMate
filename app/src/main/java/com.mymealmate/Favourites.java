package com.mymealmate;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.mymealmate.FavouritesDatabase.FavouritesCursor;

public class Favourites extends Activity implements OnItemClickListener {
    private FavouritesDatabase favDb_;
    private ArrayAdapter<String> favouriteAdapter_;
    private String favouriteName_;

    public Favourites() {
        this.favouriteAdapter_ = null;
        this.favDb_ = null;
        this.favouriteName_ = null;
    }

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        requestWindowFeature(1);
        setContentView(R.layout.favourites);
        this.favouriteAdapter_ = new ArrayAdapter(this, 17367043);
        ListView listView = (ListView) findViewById(R.id.favouriteListView);
        listView.setAdapter(this.favouriteAdapter_);
        listView.setOnItemClickListener(this);
        this.favDb_ = new FavouritesDatabase(this);
        this.favouriteAdapter_.clear();
        FavouritesCursor favourites = this.favDb_.getFavourites();
        for (int i = 0; i < favourites.getCount(); i++) {
            favourites.moveToPosition(i);
            this.favouriteAdapter_.add(favourites.getFavName());
        }
        favourites.close();
    }

    public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
        this.favouriteName_ = (String) this.favouriteAdapter_.getItem(i);
        OnClickListener anonymousClass1 = new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                switch (i) {
                    case -1:
                        Favourites.this.favouriteAdapter_.remove(Favourites.this.favouriteName_);
                        Favourites.this.favDb_.deleteFavourite(Favourites.this.favouriteName_);
                    default:
                }
            }
        };
        new Builder(this).setMessage("Whould you like to delete '" + this.favouriteName_ + "'?").setPositiveButton("Yes", anonymousClass1).setNegativeButton("No", anonymousClass1).show();
    }

    public void onStop() {
        this.favDb_.close();
        super.onStop();
    }
}