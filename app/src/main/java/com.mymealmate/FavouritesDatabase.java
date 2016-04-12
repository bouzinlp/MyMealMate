package com.mymealmate;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteCursorDriver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQuery;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

public class FavouritesDatabase extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "FavouritesDB";
    private static final int DATABASE_VERSION = 1;
    private final Context context_;

    public static class FavouritesCursor extends SQLiteCursor {
        private static final String QUERY = "SELECT favname, mealslot, foodid, amount FROM favs, favdetails WHERE favs.favname = '%s' AND favs.id = favdetails.favid";
        private static final String QUERY_FAVS = "SELECT id, favname FROM favs";

        private static class Factory implements CursorFactory {
            private Factory() {
            }

            public Cursor newCursor(SQLiteDatabase sQLiteDatabase, SQLiteCursorDriver sQLiteCursorDriver, String str, SQLiteQuery sQLiteQuery) {
                return new FavouritesCursor(sQLiteCursorDriver, str, sQLiteQuery, null);
            }
        }

        private FavouritesCursor(SQLiteDatabase sQLiteDatabase, SQLiteCursorDriver sQLiteCursorDriver, String str, SQLiteQuery sQLiteQuery) {
            super(sQLiteDatabase, sQLiteCursorDriver, str, sQLiteQuery);
        }

        public float getAmount() {
            return getFloat(getColumnIndexOrThrow("amount"));
        }

        public String getFavName() {
            return getString(getColumnIndexOrThrow("favname"));
        }

        public int getFoodId() {
            return getInt(getColumnIndexOrThrow("foodid"));
        }

        public int getId() {
            return getInt(getColumnIndexOrThrow("id"));
        }

        public long getMealSlot() {
            return getLong(getColumnIndexOrThrow("mealslot"));
        }
    }

    public FavouritesDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context_ = context;
    }

    private void execMultipleSQL(SQLiteDatabase sQLiteDatabase, String[] strArr) {
        int length = strArr.length;
        for (int i = 0; i < length; i += DATABASE_VERSION) {
            String str = strArr[i];
            if (str.trim().length() > 0) {
                sQLiteDatabase.execSQL(str);
            }
        }
    }

    public void deleteFavourite(String str) {
        SQLiteDatabase writableDatabase = getWritableDatabase();
        SQLiteStatement compileStatement = writableDatabase.compileStatement("SELECT id FROM favs WHERE favname = ?");
        compileStatement.bindString(DATABASE_VERSION, str);
        long simpleQueryForLong = compileStatement.simpleQueryForLong();
        if (simpleQueryForLong > 0) {
            compileStatement = writableDatabase.compileStatement("DELETE FROM favs WHERE favname = ?");
            compileStatement.bindString(DATABASE_VERSION, str);
            compileStatement.execute();
            SQLiteStatement compileStatement2 = writableDatabase.compileStatement("DELETE FROM favdetails WHERE favid = ?");
            compileStatement2.bindLong(DATABASE_VERSION, simpleQueryForLong);
            compileStatement2.execute();
        }
    }

    public FavouritesCursor getFavouriteSlot(String str) {
        SQLiteDatabase readableDatabase = getReadableDatabase();
        Object[] objArr = new Object[DATABASE_VERSION];
        objArr[0] = str;
        try {
            FavouritesCursor favouritesCursor = (FavouritesCursor) readableDatabase.rawQueryWithFactory(new Factory(), String.format("SELECT favname, mealslot, foodid, amount FROM favs, favdetails WHERE favs.favname = '%s' AND favs.id = favdetails.favid", objArr), null, null);
            favouritesCursor.moveToFirst();
            return favouritesCursor;
        } catch (SQLException e) {
            String message = e.getMessage();
            Log.e(message, message);
            return null;
        }
    }

    public FavouritesCursor getFavourites() {
        FavouritesCursor favouritesCursor = (FavouritesCursor) getReadableDatabase().rawQueryWithFactory(new Factory(), "SELECT id, favname FROM favs", null, null);
        favouritesCursor.moveToFirst();
        return favouritesCursor;
    }

    public long insertIntoFavourite(long j, int i, long j2, float f) {
        SQLiteStatement compileStatement = getWritableDatabase().compileStatement("INSERT INTO favdetails (favid , mealslot , foodid , amount) values (?, ?, ?, ?)");
        compileStatement.bindLong(DATABASE_VERSION, j);
        compileStatement.bindLong(2, (long) i);
        compileStatement.bindLong(3, j2);
        compileStatement.bindDouble(4, (double) f);
        return compileStatement.executeInsert();
    }

    public long insertNewFavourite(String str) {
        SQLiteStatement compileStatement = getWritableDatabase().compileStatement("INSERT INTO favs (favname) values (?)");
        compileStatement.bindString(DATABASE_VERSION, str);
        return compileStatement.executeInsert();
    }

    public boolean isFavouriteInDB(String str) {
        SQLiteStatement compileStatement = getWritableDatabase().compileStatement("SELECT COUNT(*) FROM favs WHERE favname = ?");
        compileStatement.bindString(DATABASE_VERSION, str);
        return compileStatement.simpleQueryForLong() > 0;
    }

    public void onCreate(SQLiteDatabase sQLiteDatabase) {
        String[] split = this.context_.getString(R.string.FavouritesDatabase_onCreate).split("\n");
        sQLiteDatabase.beginTransaction();
        try {
            execMultipleSQL(sQLiteDatabase, split);
            sQLiteDatabase.setTransactionSuccessful();
            sQLiteDatabase.endTransaction();
        } catch (SQLException e) {
            Log.e("Error creating tables", e.toString());
            throw e;
        } catch (Throwable th) {
            sQLiteDatabase.endTransaction();
        }
    }

    public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
    }
}