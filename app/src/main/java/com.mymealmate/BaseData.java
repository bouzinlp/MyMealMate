package com.mymealmate;

import android.content.Context;
import java.util.Date;

public class BaseData {
    protected static MyMealMateDatabase _DB;
    protected Context _ctx;

    static {
        _DB = null;
    }

    public BaseData(MyMealMateDatabase myMealMateDatabase) {
        this._ctx = null;
        _DB = myMealMateDatabase;
    }

    protected static long getNow() {
        return new Date().getTime();
    }

    public MyMealMateDatabase getDB() {
        return _DB;
    }
}