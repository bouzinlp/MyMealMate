package com.mymealmate;

import android.database.SQLException;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteCursorDriver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteQuery;
import android.database.sqlite.SQLiteStatement;

public class TargetSettingsData extends BaseData {
    public long date;
    public long id;

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

        public Float getDailyDeficit() {
            return Float.valueOf(getFloat(getColumnIndexOrThrow("DailyDeficit")));
        }

        public long getDate() {
            return getLong(getColumnIndexOrThrow("Date"));
        }

        public long getEntryId() {
            return getLong(getColumnIndexOrThrow("Id"));
        }

        public Integer getTime() {
            return Integer.valueOf(getInt(getColumnIndexOrThrow("Time")));
        }

        public Float getWeightloss() {
            return Float.valueOf(getFloat(getColumnIndexOrThrow("Weightloss")));
        }
    }

    public TargetSettingsData(MyMealMateDatabase myMealMateDatabase) {
        super(myMealMateDatabase);
        this.id = 0;
        this.date = 0;
    }

    public static Cursor getByDate(MyMealMateDatabase myMealMateDatabase, long j) {
        String str;
        synchronized (TargetSettingsData.class) {
            try {
            } finally {
                str = TargetSettingsData.class;
            }
        }
        SQLiteDatabase readableDatabase = myMealMateDatabase.getReadableDatabase();
        str = String.format("SELECT Id, Date, DailyDeficit, Weightloss, Time FROM weightloss WHERE Date <= %s ORDER BY Date DESC", new Object[]{Long.valueOf(j)});
        Cursor cursor = (Cursor) readableDatabase.rawQueryWithFactory(new Factory(), str, null, null);
        cursor.moveToFirst();
        return cursor;
    }

    public static Cursor getByExactDate(MyMealMateDatabase myMealMateDatabase, long j) {
        String str;
        synchronized (TargetSettingsData.class) {
            try {
            } finally {
                str = TargetSettingsData.class;
            }
        }
        SQLiteDatabase readableDatabase = myMealMateDatabase.getReadableDatabase();
        str = String.format("SELECT Id, Date, DailyDeficit, Weightloss, Time FROM weightloss WHERE Date = %s ORDER BY Date", new Object[]{Long.valueOf(j)});
        Cursor cursor = (Cursor) readableDatabase.rawQueryWithFactory(new Factory(), str, null, null);
        cursor.moveToFirst();
        return cursor;
    }

    public static Cursor getNeedUpload(MyMealMateDatabase myMealMateDatabase) {
        CursorFactory cursorFactory;
        synchronized (TargetSettingsData.class) {
            try {
            } finally {
                cursorFactory = TargetSettingsData.class;
            }
        }
        SQLiteDatabase readableDatabase = myMealMateDatabase.getReadableDatabase();
        cursorFactory = new Factory();
        Cursor cursor = (Cursor) readableDatabase.rawQueryWithFactory(cursorFactory, "SELECT Id, Date, DailyDeficit, Weightloss, Time FROM weightloss WHERE NeedUpload = ? ORDER BY Date", new String[]{"1"}, null);
        cursor.moveToFirst();
        return cursor;
    }

    public static void setNeedUpload(MyMealMateDatabase myMealMateDatabase, long j, boolean z) {
        int i = 1;
        synchronized (TargetSettingsData.class) {
            try {
            } finally {
                Class cls = TargetSettingsData.class;
            }
        }
        SQLiteStatement compileStatement = myMealMateDatabase.getWritableDatabase().compileStatement("UPDATE weightloss SET NeedUpload = ? where Id = ?");
        if (!z) {
            i = 0;
        }
        compileStatement.bindLong(1, (long) i);
        compileStatement.bindLong(2, j);
        compileStatement.execute();
        myMealMateDatabase.close();
    }

    public static long updateWeightLoss(MyMealMateDatabase myMealMateDatabase, float f, float f2, int i) {
        long j = -1;
        synchronized (TargetSettingsData.class) {
            SQLiteStatement sQLiteStatement = true;
        }
        try {
            long dateTime = DateProvider.getInstance().getDateTime();
            Cursor byExactDate = getByExactDate(myMealMateDatabase, dateTime);
            SQLiteDatabase writableDatabase = myMealMateDatabase.getWritableDatabase();
            if (byExactDate.getCount() > 0) {
                SQLiteStatement compileStatement = writableDatabase.compileStatement("UPDATE weightloss SET DailyDeficit = ?, Weightloss = ?, Time = ?, NeedUpload = 1 WHERE Date = ?");
                compileStatement.bindDouble(1, (double) f);
                compileStatement.bindDouble(2, (double) f2);
                compileStatement.bindLong(3, (long) i);
                compileStatement.bindLong(4, dateTime);
                try {
                    compileStatement.execute();
                    j = 1;
                } catch (SQLException e) {
                    sQLiteStatement = e;
                }
            } else {
                sQLiteStatement = writableDatabase.compileStatement("INSERT INTO weightloss (Date, DailyDeficit, Weightloss, Time, NeedUpload) values (?, ?, ?, ?, 1)");
                sQLiteStatement.bindLong(1, dateTime);
                sQLiteStatement.bindDouble(2, (double) f);
                sQLiteStatement.bindDouble(3, (double) f2);
                sQLiteStatement.bindLong(4, (long) i);
                try {
                    j = sQLiteStatement.executeInsert();
                } catch (SQLException e2) {
                    sQLiteStatement = e2;
                }
            }
            byExactDate.close();
            return j;
        } finally {
            Class cls = TargetSettingsData.class;
        }
    }

    public void setNeedUpload(boolean z) {
        synchronized (this) {
            LogData.setNeedUpload(_DB, this.id, z);
        }
    }
}