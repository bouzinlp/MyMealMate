package com.mymealmate;

import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteCursorDriver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteDoneException;
import android.database.sqlite.SQLiteQuery;
import android.database.sqlite.SQLiteStatement;
import java.util.Date;

public class WeightData extends BaseData {
    public long date;
    public long id;
    public boolean needUpload;
    public float weight;

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

        public long getDate() {
            return getLong(getColumnIndexOrThrow("Date"));
        }

        public long getEntryId() {
            return getLong(getColumnIndexOrThrow("Id"));
        }

        public Float getWeight() {
            return Float.valueOf(getFloat(getColumnIndexOrThrow("Weight")));
        }
    }

    public WeightData(MyMealMateDatabase myMealMateDatabase) {
        super(myMealMateDatabase);
        this.id = 0;
        this.date = 0;
        this.weight = 0.0f;
        this.needUpload = true;
    }

    public WeightData(MyMealMateDatabase myMealMateDatabase, float f) {
        super(myMealMateDatabase);
        this.id = 0;
        this.date = 0;
        this.weight = 0.0f;
        this.needUpload = true;
        this.weight = f;
    }

    public static float KgToLbs(float f) {
        return ((float) Math.round(220.5f * f)) / 100.0f;
    }

    public static float LbsToKg(float f) {
        return ((float) Math.round(45.36f * f)) / 100.0f;
    }

    public static float getActualWeight(MyMealMateDatabase myMealMateDatabase, Date date) {
        float parseFloat;
        synchronized (WeightData.class) {
            try {
                SQLiteDatabase readableDatabase = myMealMateDatabase.getReadableDatabase();
                SQLiteStatement compileStatement = readableDatabase.compileStatement("select Weight from  weight where (Deleted IS NULL OR Deleted = 0) AND Date <= ? order by Date desc limit 1");
                compileStatement.bindLong(1, date.getTime());
                parseFloat = Float.parseFloat(compileStatement.simpleQueryForString());
                readableDatabase.close();
            } catch (SQLiteDoneException e) {
                readableDatabase.close();
                parseFloat = 0.0f;
            } catch (Throwable th) {
                Class cls = WeightData.class;
            }
            if (parseFloat == 0.0f) {
                parseFloat = new Prefs(myMealMateDatabase, null).getWeight();
            }
        }
        return parseFloat;
    }

    public static long getEntryCount(MyMealMateDatabase myMealMateDatabase, Date date, Date date2) {
        long simpleQueryForLong;
        synchronized (WeightData.class) {
            SQLiteDatabase readableDatabase;
            try {
                readableDatabase = _DB.getReadableDatabase();
                SQLiteStatement compileStatement = readableDatabase.compileStatement("SELECT COUNT(*) FROM weight WHERE Date >= ? AND Date <= ?");
                compileStatement.bindLong(1, date.getTime());
                compileStatement.bindLong(2, date2.getTime());
                simpleQueryForLong = compileStatement.simpleQueryForLong();
                readableDatabase.close();
            } catch (SQLiteDoneException e) {
                simpleQueryForLong = 0;
                readableDatabase.close();
            } catch (Throwable th) {
                Class cls = WeightData.class;
            }
        }
        return simpleQueryForLong;
    }

    public static Cursor getNeedUpload(MyMealMateDatabase myMealMateDatabase) {
        CursorFactory cursorFactory;
        synchronized (WeightData.class) {
            try {
            } finally {
                cursorFactory = WeightData.class;
            }
        }
        SQLiteDatabase readableDatabase = myMealMateDatabase.getReadableDatabase();
        cursorFactory = new Factory();
        Cursor cursor = (Cursor) readableDatabase.rawQueryWithFactory(cursorFactory, "SELECT Id, Date, Weight FROM weight WHERE NeedUpload = ? AND (Deleted IS NULL OR Deleted = 0) ORDER BY Date", new String[]{"1"}, null);
        cursor.moveToFirst();
        return cursor;
    }

    public static void setNeedUpload(MyMealMateDatabase myMealMateDatabase, long j, boolean z) {
        int i = 1;
        synchronized (WeightData.class) {
            try {
            } finally {
                Class cls = WeightData.class;
            }
        }
        SQLiteDatabase writableDatabase = myMealMateDatabase.getWritableDatabase();
        SQLiteStatement compileStatement = writableDatabase.compileStatement("update foodentry set NeedUpload = ? where Id = ?");
        if (!z) {
            i = 0;
        }
        compileStatement.bindLong(1, (long) i);
        compileStatement.bindLong(2, j);
        compileStatement.execute();
        writableDatabase.close();
    }

    public static void setWeight(MyMealMateDatabase myMealMateDatabase, DateProvider dateProvider, float f) {
        synchronized (WeightData.class) {
            WeightData weightData;
            try {
                SQLiteDatabase readableDatabase = myMealMateDatabase.getReadableDatabase();
                weightData = new WeightData(myMealMateDatabase);
                SQLiteStatement compileStatement = readableDatabase.compileStatement("select Id from  weight where (Deleted IS NULL OR Deleted = 0) AND Date <= ? AND Date >= ? order by Date desc limit 1");
                compileStatement.bindLong(1, dateProvider.getEndOfDay().getTime());
                compileStatement.bindLong(2, dateProvider.getStartOfDay().getTime());
                weightData.id = (long) Integer.parseInt(compileStatement.simpleQueryForString());
                readableDatabase.close();
            } catch (SQLiteDoneException e) {
                weightData.id = 0;
                readableDatabase.close();
            } catch (Throwable th) {
                Class cls = WeightData.class;
            }
            weightData.weight = f;
            if (weightData.id != 0) {
                weightData.delete();
            }
            weightData.create();
        }
    }

    public long create() {
        long executeInsert;
        synchronized (this) {
            SQLiteDatabase writableDatabase = _DB.getWritableDatabase();
            SQLiteStatement compileStatement = writableDatabase.compileStatement("insert into weight (Date, Weight, NeedUpload) values (?, ?, 1)");
            compileStatement.bindLong(1, BaseData.getNow());
            compileStatement.bindDouble(2, (double) this.weight);
            executeInsert = compileStatement.executeInsert();
            writableDatabase.close();
        }
        return executeInsert;
    }

    public void delete() {
        synchronized (this) {
            SQLiteDatabase writableDatabase = _DB.getWritableDatabase();
            SQLiteStatement compileStatement = writableDatabase.compileStatement("update weight set Deleted = 1 where Id = ?");
            compileStatement.bindLong(1, this.id);
            compileStatement.execute();
            writableDatabase.close();
        }
    }

    public void setNeedUpload(boolean z) {
        synchronized (this) {
            FoodEntryData.setNeedUpload(_DB, this.id, z);
        }
    }
}