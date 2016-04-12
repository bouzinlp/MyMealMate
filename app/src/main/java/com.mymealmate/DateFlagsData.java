package com.mymealmate;

import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteCursorDriver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteDoneException;
import android.database.sqlite.SQLiteQuery;
import android.database.sqlite.SQLiteStatement;
import java.util.Date;

public class DateFlagsData extends BaseData {
    public boolean complete;
    public long date;
    public boolean needUpload;
    public long timestamp;

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

        public boolean getComplete() {
            return getInt(getColumnIndexOrThrow("Complete")) == 1;
        }

        public long getDate() {
            return getLong(getColumnIndexOrThrow("Date"));
        }

        public boolean getDeleted() {
            return getInt(getColumnIndexOrThrow("Deleted")) == 1;
        }

        public long getTimestamp() {
            return getLong(getColumnIndexOrThrow("Timestamp"));
        }
    }

    public DateFlagsData(MyMealMateDatabase myMealMateDatabase) {
        super(myMealMateDatabase);
        this.date = 0;
        this.complete = false;
        this.needUpload = true;
        this.timestamp = 0;
    }

    public static Cursor getByDate(MyMealMateDatabase myMealMateDatabase, Date date) {
        CursorFactory cursorFactory;
        synchronized (DateFlagsData.class) {
            try {
            } finally {
                cursorFactory = DateFlagsData.class;
            }
        }
        SQLiteDatabase readableDatabase = myMealMateDatabase.getReadableDatabase();
        cursorFactory = new Factory();
        Cursor cursor = (Cursor) readableDatabase.rawQueryWithFactory(cursorFactory, "SELECT Date, Complete, Deleted, NeedUpload, Timestamp FROM dateflags WHERE Date = ?", new String[]{Long.toString(date.getTime())}, null);
        cursor.moveToFirst();
        return cursor;
    }

    public static long getCompletedDatesCount(MyMealMateDatabase myMealMateDatabase, Date date, Date date2) {
        long simpleQueryForLong;
        synchronized (DateFlagsData.class) {
            SQLiteDatabase readableDatabase;
            try {
                readableDatabase = _DB.getReadableDatabase();
                SQLiteStatement compileStatement = readableDatabase.compileStatement("SELECT COUNT(*) FROM dateflags WHERE Date >= ? AND Date <= ?");
                compileStatement.bindLong(1, date.getTime());
                compileStatement.bindLong(2, date2.getTime());
                simpleQueryForLong = compileStatement.simpleQueryForLong();
                readableDatabase.close();
            } catch (SQLiteDoneException e) {
                simpleQueryForLong = 0;
                readableDatabase.close();
            } catch (Throwable th) {
                Class cls = DateFlagsData.class;
            }
        }
        return simpleQueryForLong;
    }

    public static Cursor getNeedUpload(MyMealMateDatabase myMealMateDatabase) {
        CursorFactory cursorFactory;
        synchronized (DateFlagsData.class) {
            try {
            } finally {
                cursorFactory = DateFlagsData.class;
            }
        }
        SQLiteDatabase readableDatabase = myMealMateDatabase.getReadableDatabase();
        cursorFactory = new Factory();
        Cursor cursor = (Cursor) readableDatabase.rawQueryWithFactory(cursorFactory, "SELECT Date, Complete, Deleted, Timestamp FROM dateflags WHERE NeedUpload = ? ORDER BY Date", new String[]{"1"}, null);
        cursor.moveToFirst();
        return cursor;
    }

    public static void setNeedUpload(MyMealMateDatabase myMealMateDatabase, long j, boolean z) {
        int i = 1;
        synchronized (DateFlagsData.class) {
            try {
            } finally {
                Class cls = DateFlagsData.class;
            }
        }
        SQLiteStatement compileStatement = myMealMateDatabase.getWritableDatabase().compileStatement("UPDATE dateflags SET NeedUpload = ? WHERE Date = ?");
        if (!z) {
            i = 0;
        }
        compileStatement.bindLong(1, (long) i);
        compileStatement.bindLong(2, j);
        compileStatement.execute();
        myMealMateDatabase.close();
    }

    public long create() {
        int i = 1;
        SQLiteDatabase writableDatabase = _DB.getWritableDatabase();
        SQLiteStatement compileStatement = writableDatabase.compileStatement("insert into dateflags (Date, Complete, Deleted, NeedUpload, Timestamp) values (?, ?, ?, 1, ?)");
        compileStatement.bindLong(1, this.date);
        if (!this.complete) {
            i = 0;
        }
        compileStatement.bindLong(2, (long) i);
        compileStatement.bindNull(3);
        compileStatement.bindLong(4, this.timestamp == 0 ? BaseData.getNow() : this.timestamp);
        long executeInsert = compileStatement.executeInsert();
        writableDatabase.close();
        return executeInsert;
    }

    public void delete() {
        SQLiteDatabase writableDatabase = _DB.getWritableDatabase();
        SQLiteStatement compileStatement = writableDatabase.compileStatement("update dateflags set Deleted = 1, NeedUpload = 1 where Date = ?");
        compileStatement.bindLong(1, this.date);
        compileStatement.execute();
        writableDatabase.close();
    }

    public void setNeedUpload(boolean z) {
        synchronized (this) {
            setNeedUpload(_DB, this.date, z);
        }
    }

    public void update() {
        SQLiteDatabase writableDatabase = _DB.getWritableDatabase();
        SQLiteStatement compileStatement = writableDatabase.compileStatement("update dateflags set Complete = ?, NeedUpload = 1, Timestamp = ? where Date = ?");
        compileStatement.bindLong(1, (long) (this.complete ? 1 : 0));
        compileStatement.bindLong(2, this.timestamp == 0 ? BaseData.getNow() : this.timestamp);
        compileStatement.bindLong(3, this.date);
        compileStatement.execute();
        writableDatabase.close();
    }
}