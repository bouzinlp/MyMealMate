package com.mymealmate;

import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteCursorDriver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteQuery;
import android.database.sqlite.SQLiteStatement;
import java.util.ArrayList;
import java.util.Date;
import org.xmlpull.v1.XmlPullParser;

public class FoodEntryData extends BaseData {
    public float amount;
    public float calories;
    public long createdOn;
    public long date;
    public boolean deleted;
    public long foodItem;
    public long id;
    public int mealSlot;
    public boolean needUpload;
    public String photo;

    public static class Cursor extends SQLiteCursor {

        private static class Factory implements CursorFactory {
            private Factory() {
            }

            public Cursor newCursor(SQLiteDatabase sQLiteDatabase, SQLiteCursorDriver sQLiteCursorDriver, String str, SQLiteQuery sQLiteQuery) {
                return new Cursor(sQLiteCursorDriver, str, sQLiteQuery, null);
            }
        }

        private Cursor(SQLiteDatabase sQLiteDatabase, SQLiteCursorDriver sQLiteCursorDriver, String str, SQLiteQuery sQLiteQuery) {
            super(sQLiteDatabase, sQLiteCursorDriver, str, sQLiteQuery);
        }

        public Float getAmount() {
            return Float.valueOf(getFloat(getColumnIndexOrThrow("Amount")));
        }

        public Float getCals() {
            return Float.valueOf(getFloat(getColumnIndexOrThrow("Calories")));
        }

        public long getCreatedOn() {
            return getLong(getColumnIndexOrThrow("CreatedOn"));
        }

        public long getDate() {
            return getLong(getColumnIndexOrThrow("Date"));
        }

        public boolean getDeleted() {
            return getInt(getColumnIndexOrThrow("Deleted")) != 0;
        }

        public long getEntryId() {
            return getLong(getColumnIndexOrThrow("Id"));
        }

        public long getFoodItem() {
            return getLong(getColumnIndexOrThrow("FoodItem"));
        }

        public int getMealSlot() {
            return getInt(getColumnIndexOrThrow("MealSlot"));
        }

        public String getPhoto() {
            String string = getString(getColumnIndexOrThrow("Photo"));
            return string != null ? string : XmlPullParser.NO_NAMESPACE;
        }
    }

    public FoodEntryData(MyMealMateDatabase myMealMateDatabase) {
        super(myMealMateDatabase);
        this.id = 0;
        this.date = 0;
        this.mealSlot = 0;
        this.foodItem = 0;
        this.amount = 0.0f;
        this.calories = 0.0f;
        this.createdOn = 0;
        this.deleted = false;
        this.needUpload = true;
    }

    public FoodEntryData(MyMealMateDatabase myMealMateDatabase, int i, long j, float f) {
        this(myMealMateDatabase);
        this.mealSlot = i;
        this.foodItem = j;
        this.amount = f;
    }

    public static ArrayList<FoodEntryData> getArrayByDate(MyMealMateDatabase myMealMateDatabase, DateProvider dateProvider) {
        Date date;
        synchronized (FoodEntryData.class) {
            try {
            } finally {
                date = FoodEntryData.class;
            }
        }
        Date startOfDay = dateProvider.getStartOfDay();
        date = dateProvider.getEndOfDay();
        ArrayList<FoodEntryData> arrayByDate = getArrayByDate(myMealMateDatabase, startOfDay, date);
        return arrayByDate;
    }

    public static ArrayList<FoodEntryData> getArrayByDate(MyMealMateDatabase myMealMateDatabase, Date date, Date date2) {
        ArrayList<FoodEntryData> arrayList;
        synchronized (FoodEntryData.class) {
            try {
            } finally {
                arrayList = FoodEntryData.class;
            }
        }
        arrayList = new ArrayList();
        Cursor byDate = getByDate(myMealMateDatabase, date, date2);
        int i = 0;
        while (i < byDate.getCount()) {
            byDate.moveToPosition(i);
            FoodEntryData foodEntryData = new FoodEntryData(myMealMateDatabase);
            foodEntryData.id = byDate.getEntryId();
            foodEntryData.date = byDate.getDate();
            foodEntryData.mealSlot = byDate.getMealSlot();
            foodEntryData.foodItem = byDate.getFoodItem();
            foodEntryData.amount = byDate.getAmount().floatValue();
            foodEntryData.calories = byDate.getCals().floatValue();
            foodEntryData.photo = byDate.getPhoto();
            foodEntryData.createdOn = byDate.getCreatedOn();
            foodEntryData.deleted = byDate.getDeleted();
            arrayList.add(foodEntryData);
            i++;
        }
        byDate.close();
        return arrayList;
    }

    public static Cursor getByDate(MyMealMateDatabase myMealMateDatabase, Date date, Date date2) {
        CursorFactory cursorFactory;
        synchronized (FoodEntryData.class) {
            try {
            } finally {
                cursorFactory = FoodEntryData.class;
            }
        }
        SQLiteDatabase readableDatabase = myMealMateDatabase.getReadableDatabase();
        long time = date.getTime();
        long time2 = date2.getTime();
        cursorFactory = new Factory();
        Cursor cursor = (Cursor) readableDatabase.rawQueryWithFactory(cursorFactory, "SELECT Id, Date, MealSlot, FoodItem, Amount, Calories, Photo, CreatedOn, Deleted FROM foodentry WHERE (Deleted IS NULL OR Deleted = 0) AND Date <= ? AND Date >= ?", new String[]{String.valueOf(time2), String.valueOf(time)}, null);
        cursor.moveToFirst();
        return cursor;
    }

    public static Cursor getByDateAndMealSlot(MyMealMateDatabase myMealMateDatabase, DateProvider dateProvider, int i) {
        CursorFactory cursorFactory;
        synchronized (FoodEntryData.class) {
            try {
            } finally {
                cursorFactory = FoodEntryData.class;
            }
        }
        SQLiteDatabase readableDatabase = myMealMateDatabase.getReadableDatabase();
        long time = dateProvider.getStartOfDay().getTime();
        long time2 = dateProvider.getEndOfDay().getTime();
        cursorFactory = new Factory();
        Cursor cursor = (Cursor) readableDatabase.rawQueryWithFactory(cursorFactory, "SELECT Id, Date, MealSlot, FoodItem, Amount, Calories, Photo, CreatedOn, Deleted FROM foodentry WHERE (Deleted IS NULL OR Deleted = 0) AND Date <= ? AND Date >= ? AND MealSlot = ?", new String[]{String.valueOf(time2), String.valueOf(time), String.valueOf(i)}, null);
        cursor.moveToFirst();
        return cursor;
    }

    public static FoodEntryData getEntry(MyMealMateDatabase myMealMateDatabase, long j) {
        Cursor cursor = (Cursor) myMealMateDatabase.getReadableDatabase().rawQueryWithFactory(new Factory(), "SELECT Id, Date, MealSlot, FoodItem, Amount, Calories, Photo, CreatedOn, Deleted FROM foodentry WHERE (Deleted IS NULL OR Deleted = 0) AND Id = ?", new String[]{String.valueOf(j)}, null);
        cursor.moveToFirst();
        FoodEntryData foodEntryData = new FoodEntryData(myMealMateDatabase);
        if (cursor.getCount() == 0) {
            return foodEntryData;
        }
        foodEntryData.id = cursor.getEntryId();
        foodEntryData.date = cursor.getDate();
        foodEntryData.mealSlot = cursor.getMealSlot();
        foodEntryData.foodItem = cursor.getFoodItem();
        foodEntryData.amount = cursor.getAmount().floatValue();
        foodEntryData.calories = cursor.getCals().floatValue();
        foodEntryData.photo = cursor.getPhoto();
        foodEntryData.createdOn = cursor.getCreatedOn();
        foodEntryData.deleted = cursor.getDeleted();
        cursor.close();
        return foodEntryData;
    }

    public static Cursor getNeedUpload(MyMealMateDatabase myMealMateDatabase) {
        CursorFactory cursorFactory;
        synchronized (FoodEntryData.class) {
            try {
            } finally {
                cursorFactory = FoodEntryData.class;
            }
        }
        SQLiteDatabase readableDatabase = myMealMateDatabase.getReadableDatabase();
        cursorFactory = new Factory();
        Cursor cursor = (Cursor) readableDatabase.rawQueryWithFactory(cursorFactory, "SELECT Id, Date, MealSlot, FoodItem, Amount, Calories, Photo, CreatedOn, Deleted FROM foodentry WHERE NeedUpload = 1 ORDER BY Date", null, null);
        cursor.moveToFirst();
        return cursor;
    }

    public static android.database.Cursor getRecentEnries(MyMealMateDatabase myMealMateDatabase) {
        String str;
        synchronized (FoodEntryData.class) {
            try {
            } finally {
                str = FoodEntryData.class;
            }
        }
        str = "SELECT DISTINCT f.foodid AS _id, f.desc AS Description FROM foodentry fe INNER JOIN food f ON fe.FoodItem = f.foodid ORDER BY fe.CreatedOn DESC LIMIT 10";
        android.database.Cursor rawQuery = myMealMateDatabase.getReadableDatabase().rawQuery(str, null);
        rawQuery.moveToFirst();
        return rawQuery;
    }

    public static void insertFoodPhoto(int i, String str, Date date) {
        synchronized (FoodEntryData.class) {
            try {
            } finally {
                Class cls = FoodEntryData.class;
            }
        }
        Object foodEntryData = new FoodEntryData(_DB);
        foodEntryData.mealSlot = i;
        foodEntryData.photo = str;
        foodEntryData.date = date.getTime();
        foodEntryData.create();
    }

    public static void setNeedUpload(MyMealMateDatabase myMealMateDatabase, long j, boolean z) {
        int i = 1;
        synchronized (FoodEntryData.class) {
            try {
            } finally {
                Class cls = FoodEntryData.class;
            }
        }
        SQLiteDatabase writableDatabase = myMealMateDatabase.getWritableDatabase();
        SQLiteStatement compileStatement = writableDatabase.compileStatement("UPDATE foodentry SET NeedUpload = ? where Id = ?");
        if (!z) {
            i = 0;
        }
        compileStatement.bindLong(1, (long) i);
        compileStatement.bindLong(2, j);
        compileStatement.execute();
        writableDatabase.close();
    }

    public long create() {
        SQLiteDatabase writableDatabase = _DB.getWritableDatabase();
        SQLiteStatement compileStatement = writableDatabase.compileStatement("INSERT INTO foodentry (Date, MealSlot, FoodItem, Amount, Calories, Photo, CreatedOn, NeedUpload) VALUES (?, ?, ?, ?, ?, ?, ?, 1)");
        long now = BaseData.getNow();
        compileStatement.bindLong(1, this.date == 0 ? now : this.date);
        compileStatement.bindLong(2, (long) this.mealSlot);
        compileStatement.bindLong(3, this.foodItem);
        compileStatement.bindDouble(4, (double) this.amount);
        compileStatement.bindDouble(5, (double) this.calories);
        if (this.photo != null) {
            compileStatement.bindString(6, this.photo);
        } else {
            compileStatement.bindNull(6);
        }
        compileStatement.bindLong(7, now);
        long executeInsert = compileStatement.executeInsert();
        writableDatabase.close();
        return executeInsert;
    }

    public void delete() {
        SQLiteDatabase writableDatabase = _DB.getWritableDatabase();
        SQLiteStatement compileStatement = writableDatabase.compileStatement("UPDATE foodentry SET Deleted = 1, NeedUpload = 1 WHERE Id = ?");
        compileStatement.bindLong(1, this.id);
        compileStatement.execute();
        writableDatabase.close();
    }

    public void setNeedUpload(boolean z) {
        synchronized (this) {
            setNeedUpload(_DB, this.id, z);
        }
    }

    public void update() {
        SQLiteDatabase writableDatabase = _DB.getWritableDatabase();
        SQLiteStatement compileStatement = writableDatabase.compileStatement("UPDATE foodentry SET FoodItem = ?, Amount = ?, Calories = ?, CreatedOn = ?,  NeedUpload = 1 WHERE Id = ?");
        compileStatement.bindLong(1, this.foodItem);
        compileStatement.bindDouble(2, (double) this.amount);
        compileStatement.bindDouble(3, (double) this.calories);
        compileStatement.bindLong(4, BaseData.getNow());
        compileStatement.bindLong(5, this.id);
        compileStatement.execute();
        writableDatabase.close();
    }
}