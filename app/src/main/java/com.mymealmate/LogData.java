package com.mymealmate;

import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteCursorDriver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteQuery;
import android.database.sqlite.SQLiteStatement;
import org.xmlpull.v1.XmlPullParser;

public class LogData extends BaseData {
    public long date;
    public String description;
    public long id;
    public boolean needUpload;

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

        public String getDescription() {
            return getString(getColumnIndexOrThrow("Description"));
        }

        public long getEntryId() {
            return getLong(getColumnIndexOrThrow("Id"));
        }
    }

    public LogData(MyMealMateDatabase myMealMateDatabase) {
        super(myMealMateDatabase);
        this.id = 0;
        this.date = 0;
        this.description = XmlPullParser.NO_NAMESPACE;
        this.needUpload = true;
    }

    public static Cursor getNeedUpload(MyMealMateDatabase myMealMateDatabase) {
        CursorFactory cursorFactory;
        synchronized (LogData.class) {
            try {
            } finally {
                cursorFactory = LogData.class;
            }
        }
        SQLiteDatabase readableDatabase = myMealMateDatabase.getReadableDatabase();
        cursorFactory = new Factory();
        Cursor cursor = (Cursor) readableDatabase.rawQueryWithFactory(cursorFactory, "SELECT Id, Date, Description FROM log WHERE NeedUpload = ? ORDER BY Date", new String[]{"1"}, null);
        cursor.moveToFirst();
        return cursor;
    }

    public static void message(MyMealMateDatabase myMealMateDatabase, String str) {
        synchronized (LogData.class) {
            try {
            } finally {
                Class cls = LogData.class;
            }
        }
        Object logData = new LogData(myMealMateDatabase);
        logData.description = str;
        logData.create();
    }

    public static void setNeedUpload(MyMealMateDatabase myMealMateDatabase, long j, boolean z) {
        int i = 1;
        synchronized (LogData.class) {
            try {
            } finally {
                Class cls = LogData.class;
            }
        }
        SQLiteStatement compileStatement = myMealMateDatabase.getWritableDatabase().compileStatement("update log set NeedUpload = ? where Id = ?");
        if (!z) {
            i = 0;
        }
        compileStatement.bindLong(1, (long) i);
        compileStatement.bindLong(2, j);
        compileStatement.execute();
        myMealMateDatabase.close();
    }

    public long create() {
        SQLiteDatabase writableDatabase = _DB.getWritableDatabase();
        SQLiteStatement compileStatement = writableDatabase.compileStatement("insert into log (Date, Description, NeedUpload) values (?, ?, 1)");
        compileStatement.bindLong(1, BaseData.getNow());
        compileStatement.bindString(2, this.description);
        long executeInsert = compileStatement.executeInsert();
        writableDatabase.close();
        return executeInsert;
    }

    public void setNeedUpload(boolean z) {
        synchronized (this) {
            setNeedUpload(_DB, this.id, z);
        }
    }
}