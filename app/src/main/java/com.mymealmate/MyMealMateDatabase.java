package com.mymealmate;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteCursorDriver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQuery;
import android.os.Environment;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MyMealMateDatabase extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "MyMealMateDB";
    private static final String DATABASE_NAME_ASSET = "MyMealMateDB.mp3";
    private static final String DATABASE_NAME_SD = "MyMealMateDB.db";
    private static final int DATABASE_VERSION = 1;
    private static final String DB_PATH = "/data/data/com.mymealmate/databases/";
    private static final String DB_PATH_SD;
    private final Context context_;
    private SQLiteDatabase dataBase_;

    public static class MyMealMateCursor extends SQLiteCursor {
        private static final String QUERY_BRAND = "SELECT brandid, brandname FROM brand WHERE brandname LIKE '%s%s%s' ORDER BY brandname";
        private static final String QUERY_BRAND_BY_ID = "SELECT brandid, brandname FROM brand WHERE brandid = %s ORDER BY brandname";
        private static final String QUERY_FOOD_BY_BRAND = "SELECT foodid, serving, desc, cals, liquid, size, brandid FROM food WHERE brandid = %s AND %s ORDER BY brandid, desc";
        private static final String QUERY_FOOD_BY_BRAND_AND_SUBBRAND = "SELECT foodid, serving, desc, cals, liquid, size, brandid FROM food WHERE (brandid = %s AND subbrandid = %s) AND %s ORDER BY brandid, subbrandid, desc";
        private static final String QUERY_FOOD_BY_DESC = "SELECT foodid, serving, desc, cals, liquid, size, brandid FROM food WHERE %s ORDER BY brandid, desc";
        private static final String QUERY_FOOD_BY_ID = "SELECT foodid, serving, desc, cals, liquid, size, brandid FROM food WHERE foodid = %s";
        private static final String QUERY_SUBBRAND = "SELECT subbrandid, subbrand FROM subbrand WHERE brandid = %s ORDER BY subbrandid";

        private static class Factory implements CursorFactory {
            private Factory() {
            }

            public Cursor newCursor(SQLiteDatabase sQLiteDatabase, SQLiteCursorDriver sQLiteCursorDriver, String str, SQLiteQuery sQLiteQuery) {
                return new MyMealMateCursor(sQLiteCursorDriver, str, sQLiteQuery, null);
            }
        }

        public enum SortBy {
            headertext,
            desc
        }

        private MyMealMateCursor(SQLiteDatabase sQLiteDatabase, SQLiteCursorDriver sQLiteCursorDriver, String str, SQLiteQuery sQLiteQuery) {
            super(sQLiteDatabase, sQLiteCursorDriver, str, sQLiteQuery);
        }

        public int getBrandId() {
            return getInt(getColumnIndexOrThrow("brandid"));
        }

        public String getBrandName() {
            return getString(getColumnIndexOrThrow("brandname"));
        }

        public float getCalories() {
            return getFloat(getColumnIndexOrThrow("cals"));
        }

        public String getFoodDescription() {
            return getString(getColumnIndexOrThrow("desc"));
        }

        public int getFoodId() {
            return getInt(getColumnIndexOrThrow("foodid"));
        }

        public String getServing() {
            return getString(getColumnIndexOrThrow("serving"));
        }

        public float getSize() {
            return getFloat(getColumnIndexOrThrow("size"));
        }

        public int getSubbrandId() {
            return getInt(getColumnIndexOrThrow("subbrandid"));
        }

        public String getSubbrandName() {
            return getString(getColumnIndexOrThrow("subbrand"));
        }
    }

    static {
        DB_PATH_SD = Environment.getExternalStorageDirectory() + "/MyMealMate/";
    }

    public MyMealMateDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context_ = context;
        try {
            createDataBase();
            try {
                openDataBase();
            } catch (SQLException e) {
                throw e;
            }
        } catch (IOException e2) {
            throw new Error("Unable to create database");
        }
    }

    private boolean checkDataBase() {
        SQLiteDatabase sQLiteDatabase = null;
        try {
            sQLiteDatabase = SQLiteDatabase.openDatabase("/data/data/com.mymealmate/databases/MyMealMateDB", null, DATABASE_VERSION);
        } catch (SQLiteException e) {
        }
        if (sQLiteDatabase != null) {
            sQLiteDatabase.close();
        }
        return sQLiteDatabase != null;
    }

    private void copyDataBase() throws IOException {
        InputStream open = this.context_.getAssets().open(DATABASE_NAME_ASSET);
        OutputStream fileOutputStream = new FileOutputStream("/data/data/com.mymealmate/databases/MyMealMateDB");
        byte[] bArr = new byte[10240];
        while (true) {
            int read = open.read(bArr);
            if (read <= 0) {
                fileOutputStream.flush();
                fileOutputStream.close();
                open.close();
                return;
            }
            fileOutputStream.write(bArr, 0, read);
        }
    }

    public void close() {
        synchronized (this) {
            if (this.dataBase_ != null) {
                this.dataBase_.close();
            }
            super.close();
        }
    }

    public boolean copyDBtoSD() {
        boolean z = true;
        boolean z2 = this.dataBase_ != null;
        if (z2) {
            close();
        }
        try {
            new File(DB_PATH_SD).mkdirs();
            InputStream fileInputStream = new FileInputStream("/data/data/com.mymealmate/databases/MyMealMateDB");
            try {
                OutputStream fileOutputStream = new FileOutputStream(DB_PATH_SD + DATABASE_NAME_SD);
                try {
                    byte[] bArr = new byte[10240];
                    while (true) {
                        int read = fileInputStream.read(bArr);
                        if (read <= 0) {
                            break;
                        }
                        fileOutputStream.write(bArr, 0, read);
                    }
                    if (fileOutputStream != null) {
                        fileOutputStream.flush();
                        fileOutputStream.close();
                    }
                    if (fileInputStream != null) {
                        fileInputStream.close();
                    }
                } catch (IOException e) {
                }
            } catch (IOException e2) {
                z = false;
                if (z2) {
                    openDataBase();
                }
                return z;
            }
        } catch (IOException e3) {
            z = false;
            if (z2) {
                openDataBase();
            }
            return z;
        }
        if (z2) {
            openDataBase();
        }
        return z;
    }

    public void createDataBase() throws IOException {
        if (!checkDataBase()) {
            getReadableDatabase();
            try {
                copyDataBase();
            } catch (IOException e) {
                throw new Error("Error copying database");
            }
        }
    }

    public MyMealMateCursor getBrandCursorById(int i) {
        SQLiteDatabase readableDatabase = getReadableDatabase();
        Object[] objArr = new Object[DATABASE_VERSION];
        objArr[0] = Integer.toString(i);
        MyMealMateCursor myMealMateCursor = (MyMealMateCursor) readableDatabase.rawQueryWithFactory(new Factory(), String.format("SELECT brandid, brandname FROM brand WHERE brandid = %s ORDER BY brandname", objArr), null, null);
        myMealMateCursor.moveToFirst();
        return myMealMateCursor;
    }

    public MyMealMateCursor getFoodCursor(int i, int i2, String str) {
        String format;
        SQLiteDatabase readableDatabase = getReadableDatabase();
        if (i == -1) {
            Object[] objArr = new Object[DATABASE_VERSION];
            objArr[0] = str;
            format = String.format("SELECT foodid, serving, desc, cals, liquid, size, brandid FROM food WHERE %s ORDER BY brandid, desc", objArr);
        } else if (i2 == -1) {
            format = String.format("SELECT foodid, serving, desc, cals, liquid, size, brandid FROM food WHERE brandid = %s AND %s ORDER BY brandid, desc", new Object[]{Integer.toString(i), str});
        } else {
            format = String.format("SELECT foodid, serving, desc, cals, liquid, size, brandid FROM food WHERE (brandid = %s AND subbrandid = %s) AND %s ORDER BY brandid, subbrandid, desc", new Object[]{Integer.toString(i), Integer.toString(i2), str});
        }
        MyMealMateCursor myMealMateCursor = (MyMealMateCursor) readableDatabase.rawQueryWithFactory(new Factory(), format, null, null);
        myMealMateCursor.moveToFirst();
        return myMealMateCursor;
    }

    public MyMealMateCursor getFoodCursorById(long j) {
        SQLiteDatabase readableDatabase = getReadableDatabase();
        Object[] objArr = new Object[DATABASE_VERSION];
        objArr[0] = Long.toString(j);
        MyMealMateCursor myMealMateCursor = (MyMealMateCursor) readableDatabase.rawQueryWithFactory(new Factory(), String.format("SELECT foodid, serving, desc, cals, liquid, size, brandid FROM food WHERE foodid = %s", objArr), null, null);
        myMealMateCursor.moveToFirst();
        return myMealMateCursor;
    }

    public MyMealMateCursor getSearchBrandCursor(String str) {
        MyMealMateCursor myMealMateCursor = (MyMealMateCursor) getReadableDatabase().rawQueryWithFactory(new Factory(), String.format("SELECT brandid, brandname FROM brand WHERE brandname LIKE '%s%s%s' ORDER BY brandname", new Object[]{"%", str, "%"}), null, null);
        myMealMateCursor.moveToFirst();
        return myMealMateCursor;
    }

    public MyMealMateCursor getSubBrandCursor(int i) {
        SQLiteDatabase readableDatabase = getReadableDatabase();
        Object[] objArr = new Object[DATABASE_VERSION];
        objArr[0] = Integer.toString(i);
        MyMealMateCursor myMealMateCursor = (MyMealMateCursor) readableDatabase.rawQueryWithFactory(new Factory(), String.format("SELECT subbrandid, subbrand FROM subbrand WHERE brandid = %s ORDER BY subbrandid", objArr), null, null);
        myMealMateCursor.moveToFirst();
        return myMealMateCursor;
    }

    public void onCreate(SQLiteDatabase sQLiteDatabase) {
    }

    public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
    }

    public void openDataBase() throws SQLException {
        close();
        this.dataBase_ = SQLiteDatabase.openDatabase("/data/data/com.mymealmate/databases/MyMealMateDB", null, 0);
    }
}