package com.mymealmate;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDoneException;
import android.database.sqlite.SQLiteStatement;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ExerciseData extends BaseData {
    public ExerciseData(MyMealMateDatabase myMealMateDatabase) {
        super(myMealMateDatabase);
    }

    public static Cursor getCategories(MyMealMateDatabase myMealMateDatabase) {
        String str;
        synchronized (ExerciseData.class) {
            try {
            } finally {
                str = ExerciseData.class;
            }
        }
        str = "SELECT Id AS _id, Category FROM exercisecategory";
        Cursor rawQuery = myMealMateDatabase.getReadableDatabase().rawQuery(str, null);
        rawQuery.moveToFirst();
        return rawQuery;
    }

    public static Cursor getExercises(MyMealMateDatabase myMealMateDatabase, Long l) {
        String str;
        synchronized (ExerciseData.class) {
            try {
            } finally {
                str = ExerciseData.class;
            }
        }
        str = "SELECT Code AS _id, METs, Description FROM exercisedata WHERE Category = ?";
        Cursor rawQuery = myMealMateDatabase.getReadableDatabase().rawQuery(str, new String[]{l.toString()});
        rawQuery.moveToFirst();
        return rawQuery;
    }

    public static float getMETsByCode(MyMealMateDatabase myMealMateDatabase, long j) {
        SQLiteDatabase readableDatabase;
        float parseFloat;
        synchronized (ExerciseData.class) {
            try {
                readableDatabase = myMealMateDatabase.getReadableDatabase();
                SQLiteStatement compileStatement = readableDatabase.compileStatement("select METs from exercisedata where Code = ? limit 1");
                compileStatement.bindLong(1, j);
                parseFloat = Float.parseFloat(compileStatement.simpleQueryForString());
                readableDatabase.close();
            } catch (SQLiteDoneException e) {
                parseFloat = 0.0f;
                readableDatabase.close();
            } catch (Throwable th) {
                Class cls = ExerciseData.class;
            }
        }
        return parseFloat;
    }

    public static void updateDB(MyMealMateDatabase myMealMateDatabase, Context context) {
        synchronized (ExerciseData.class) {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(context.getAssets().open("part6.csv")));
            while (true) {
                String readLine = bufferedReader.readLine();
                if (readLine == null) {
                    break;
                }
                try {
                    Cursor rawQuery;
                    SQLiteDatabase writableDatabase;
                    SQLiteDatabase readableDatabase = myMealMateDatabase.getReadableDatabase();
                    String[] split = readLine.split(";");
                    Cursor rawQuery2 = readableDatabase.rawQuery("SELECT foodid FROM food WHERE foodid = " + split[0], null);
                    Object obj = (rawQuery2.getCount() <= 0 || !rawQuery2.moveToFirst()) ? null : 1;
                    rawQuery2.close();
                    int i = 0;
                    int i2 = 0;
                    if (split[3].length() > 0) {
                        rawQuery = readableDatabase.rawQuery("SELECT brandid FROM brand WHERE brandname = ?", new String[]{split[3]});
                        if (rawQuery.getCount() <= 0 || !rawQuery.moveToFirst()) {
                            writableDatabase = myMealMateDatabase.getWritableDatabase();
                            SQLiteStatement compileStatement = writableDatabase.compileStatement("insert into brand values( (select max(brandid) from brand) + 1,?)");
                            compileStatement.bindString(1, split[3]);
                            i = (int) compileStatement.executeInsert();
                            writableDatabase.close();
                        } else {
                            i = rawQuery.getInt(0);
                        }
                        rawQuery.close();
                    }
                    if (split[2].length() > 0) {
                        rawQuery = readableDatabase.rawQuery("SELECT subbrandid FROM subbrand WHERE brandid = ? and subbrand = ? ", new String[]{String.valueOf(i), split[2]});
                        if (rawQuery.moveToFirst()) {
                            i2 = rawQuery.getInt(0);
                        } else {
                            writableDatabase = myMealMateDatabase.getWritableDatabase();
                            SQLiteStatement compileStatement2 = writableDatabase.compileStatement("insert into subbrand values( (select max(subbrandid) from subbrand) + 1,?,?)");
                            compileStatement2.bindString(1, String.valueOf(i));
                            compileStatement2.bindString(2, split[2]);
                            i2 = (int) compileStatement2.executeInsert();
                            writableDatabase.close();
                        }
                        rawQuery.close();
                    }
                    int i3 = 0;
                    Double valueOf = Double.valueOf(0.0d);
                    Double valueOf2 = Double.valueOf(0.0d);
                    try {
                        valueOf = Double.valueOf(Double.parseDouble(split[16].contains(",") ? split[16].replace(',', '.') : split[16] + ".00"));
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                    try {
                        valueOf2 = Double.valueOf(Double.parseDouble(split[4].contains(",") ? split[4].replace(',', '.') : split[16] + ".00"));
                    } catch (NumberFormatException e2) {
                        e2.printStackTrace();
                    }
                    if (!split[17].equals("g")) {
                        i3 = 1;
                    }
                    SQLiteDatabase writableDatabase2 = myMealMateDatabase.getWritableDatabase();
                    SQLiteStatement compileStatement3 = writableDatabase2.compileStatement(obj != null ? "update food set desc=?,brandid=?,subbrandid=?,serving=?,size=?,cals=?,liquid=? where foodid=" + split[0] : "insert into food values(" + split[0] + ",?,?,?,?,?,?,?)");
                    compileStatement3.bindString(1, split[1]);
                    compileStatement3.bindLong(2, (long) i);
                    compileStatement3.bindLong(3, (long) i2);
                    compileStatement3.bindString(4, split[15]);
                    compileStatement3.bindDouble(5, valueOf.doubleValue());
                    compileStatement3.bindDouble(6, valueOf2.doubleValue());
                    compileStatement3.bindLong(7, (long) i3);
                    compileStatement3.execute();
                    writableDatabase2.close();
                    readableDatabase.close();
                } catch (IOException e3) {
                    e3.printStackTrace();
                } catch (Throwable th) {
                    Class cls = ExerciseData.class;
                }
            }
        }
    }
}