package com.mymealmate;

import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteCursorDriver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteQuery;
import android.database.sqlite.SQLiteStatement;
import org.xmlpull.v1.XmlPullParser;

public class ExerciseEntryData extends BaseData {
    public float calories;
    public long category;
    public long code;
    public long createdOn;
    public long date;
    public boolean deleted;
    public long id;
    public boolean needUpload;
    public int time;

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

        public Float getCals() {
            return Float.valueOf(getFloat(getColumnIndexOrThrow("Calories")));
        }

        public long getCategory() {
            return getLong(getColumnIndexOrThrow("Category"));
        }

        public long getCode() {
            return getLong(getColumnIndexOrThrow("Code"));
        }

        public long getCreatedOn() {
            return getLong(getColumnIndexOrThrow("CreatedOn"));
        }

        public long getDate() {
            return getLong(getColumnIndexOrThrow("Date"));
        }

        public Integer getDeleted() {
            return Integer.valueOf(getInt(getColumnIndexOrThrow("Deleted")));
        }

        public String getDescription() {
            return getColumnIndex("Description") != -1 ? getString(getColumnIndexOrThrow("Description")) : XmlPullParser.NO_NAMESPACE;
        }

        public long getEntryId() {
            return getLong(getColumnIndexOrThrow("Id"));
        }

        public int getTime() {
            return getInt(getColumnIndexOrThrow("Time"));
        }
    }

    public ExerciseEntryData(MyMealMateDatabase myMealMateDatabase) {
        super(myMealMateDatabase);
        this.id = 0;
        this.date = 0;
        this.code = 0;
        this.time = 0;
        this.calories = 0.0f;
        this.category = 0;
        this.createdOn = 0;
        this.deleted = false;
        this.needUpload = true;
    }

    public static Cursor getByDate(MyMealMateDatabase myMealMateDatabase, DateProvider dateProvider) {
        CursorFactory cursorFactory;
        synchronized (ExerciseEntryData.class) {
            try {
            } finally {
                cursorFactory = ExerciseEntryData.class;
            }
        }
        SQLiteDatabase readableDatabase = myMealMateDatabase.getReadableDatabase();
        long time = dateProvider.getStartOfDay().getTime();
        long time2 = dateProvider.getEndOfDay().getTime();
        cursorFactory = new Factory();
        Cursor cursor = (Cursor) readableDatabase.rawQueryWithFactory(cursorFactory, "SELECT Id, Date, exerciseentry.Code, Time, Calories, Description FROM exerciseentry, exercisedata WHERE exerciseentry.Code = exercisedata.Code AND (Deleted IS NULL OR Deleted = 0) AND Date <= ? AND Date >= ?", new String[]{String.valueOf(time2), String.valueOf(time)}, null);
        cursor.moveToFirst();
        return cursor;
    }

    public static ExerciseEntryData getEntry(MyMealMateDatabase myMealMateDatabase, long j) {
        Cursor cursor = (Cursor) myMealMateDatabase.getReadableDatabase().rawQueryWithFactory(new Factory(), "SELECT ee.Id, Date, ee.Code, Time, Calories, ed.Category, ee.CreatedOn FROM exerciseentry ee, exercisedata ed WHERE ee.Code = ed.Code AND (Deleted IS NULL OR Deleted = 0) AND ee.Id = ?", new String[]{String.valueOf(j)}, null);
        cursor.moveToFirst();
        ExerciseEntryData exerciseEntryData = new ExerciseEntryData(myMealMateDatabase);
        if (cursor.getCount() == 0) {
            return null;
        }
        exerciseEntryData.id = cursor.getEntryId();
        exerciseEntryData.date = cursor.getDate();
        exerciseEntryData.code = cursor.getCode();
        exerciseEntryData.time = cursor.getTime();
        exerciseEntryData.calories = cursor.getCals().floatValue();
        exerciseEntryData.category = cursor.getCategory();
        exerciseEntryData.createdOn = cursor.getCreatedOn();
        cursor.close();
        return exerciseEntryData;
    }

    public static Cursor getNeedUpload(MyMealMateDatabase myMealMateDatabase) {
        CursorFactory cursorFactory;
        synchronized (ExerciseEntryData.class) {
            try {
            } finally {
                cursorFactory = ExerciseEntryData.class;
            }
        }
        SQLiteDatabase readableDatabase = myMealMateDatabase.getReadableDatabase();
        cursorFactory = new Factory();
        Cursor cursor = (Cursor) readableDatabase.rawQueryWithFactory(cursorFactory, "SELECT Id, Date, Code, Time, Calories, CreatedOn, Deleted FROM exerciseentry WHERE NeedUpload = 1 ORDER BY Date", null, null);
        cursor.moveToFirst();
        return cursor;
    }

    public static void setNeedUpload(MyMealMateDatabase myMealMateDatabase, long j, boolean z) {
        int i = 1;
        synchronized (ExerciseEntryData.class) {
            try {
            } finally {
                Class cls = ExerciseEntryData.class;
            }
        }
        SQLiteDatabase writableDatabase = myMealMateDatabase.getWritableDatabase();
        SQLiteStatement compileStatement = writableDatabase.compileStatement("UPDATE exerciseentry SET NeedUpload = ? WHERE Id = ?");
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
        SQLiteStatement compileStatement = writableDatabase.compileStatement("INSERT INTO exerciseentry (Date, Code, Time, Calories, CreatedOn, NeedUpload) VALUES (?, ?, ?, ?, ?, 1)");
        long now = BaseData.getNow();
        compileStatement.bindLong(1, this.date == 0 ? now : this.date);
        compileStatement.bindLong(2, this.code);
        compileStatement.bindLong(3, (long) this.time);
        compileStatement.bindDouble(4, (double) this.calories);
        compileStatement.bindLong(5, now);
        long executeInsert = compileStatement.executeInsert();
        writableDatabase.close();
        return executeInsert;
    }

    public void delete() {
        SQLiteDatabase writableDatabase = _DB.getWritableDatabase();
        SQLiteStatement compileStatement = writableDatabase.compileStatement("UPDATE exerciseentry SET Deleted = 1, NeedUpload = 1 WHERE Id = ?");
        compileStatement.bindLong(1, this.id);
        compileStatement.execute();
        writableDatabase.close();
    }

    public void setNeedUpload(boolean z) {
        FoodEntryData.setNeedUpload(_DB, this.id, z);
    }

    public void update() {
        SQLiteDatabase writableDatabase = _DB.getWritableDatabase();
        SQLiteStatement compileStatement = writableDatabase.compileStatement("UPDATE exerciseentry SET Code = ?, Time = ?, Calories = ?, CreatedOn = ?, NeedUpload = 1 WHERE Id = ?");
        compileStatement.bindLong(1, this.code);
        compileStatement.bindLong(2, (long) this.time);
        compileStatement.bindDouble(3, (double) this.calories);
        compileStatement.bindLong(4, BaseData.getNow());
        compileStatement.bindLong(5, this.id);
        compileStatement.execute();
        writableDatabase.close();
    }
}