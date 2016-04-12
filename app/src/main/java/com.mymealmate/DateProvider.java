package com.mymealmate;

import android.util.Log;
import java.util.Calendar;
import java.util.Date;

public class DateProvider {
    private static DateProvider instance_;
    private Calendar calendar_;

    static {
        instance_ = null;
    }

    protected DateProvider() {
        Log.i("MyMealMate", "Calendar initialization");
        this.calendar_ = Calendar.getInstance();
        Log.i("MyMealMate", "Calendar absolute value: " + String.valueOf(this.calendar_.getTimeInMillis()));
        this.calendar_.set(10, 1);
        this.calendar_.set(12, 1);
        this.calendar_.set(13, 1);
        this.calendar_.set(14, 1);
        Log.i("MyMealMate", "Calendar adjusted value: " + String.valueOf(this.calendar_.getTimeInMillis()));
    }

    public static Date getEndOfDay(Calendar calendar) {
        calendar.set(11, 23);
        calendar.set(12, 59);
        calendar.set(13, 59);
        return calendar.getTime();
    }

    public static String getFormatedDate(Calendar calendar) {
        String str = new String();
        Log.i("MyMealMate", "Calendar value to format: " + String.valueOf(calendar.getTimeInMillis()));
        String[] strArr = new String[]{"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
        Object obj = strArr[calendar.get(7) - 1];
        String[] strArr2 = new String[]{"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        str = new StringBuilder(String.valueOf(obj)).append(", ").append(calendar.get(5)).append(" ").append(strArr2[calendar.get(2)]).toString();
        Log.i("MyMealMate", "Calendar formatted value: " + str);
        return str;
    }

    public static DateProvider getInstance() {
        synchronized (DateProvider.class) {
            try {
            } finally {
                Object obj = DateProvider.class;
            }
        }
        if (instance_ == null) {
            instance_ = new DateProvider();
        }
        DateProvider dateProvider = instance_;
        return dateProvider;
    }

    public static Date getStartOfDay(Calendar calendar) {
        calendar.set(11, 0);
        calendar.set(12, 0);
        calendar.set(13, 0);
        return calendar.getTime();
    }

    public int getDate() {
        return this.calendar_.get(5);
    }

    public long getDateTime() {
        return this.calendar_.getTime().getTime();
    }

    public Date getEndOfDay() {
        Date time = this.calendar_.getTime();
        time.setHours(23);
        time.setMinutes(59);
        time.setSeconds(59);
        return time;
    }

    public String getFormatedDate() {
        return getFormatedDate(this.calendar_);
    }

    public int getMonth() {
        return this.calendar_.get(2);
    }

    public Date getStartOfDay() {
        Date time = this.calendar_.getTime();
        time.setHours(0);
        time.setMinutes(0);
        time.setSeconds(0);
        return time;
    }

    public int getYear() {
        return this.calendar_.get(1);
    }

    public boolean isToday() {
        Calendar instance = Calendar.getInstance();
        int i = instance.get(5);
        return getYear() == instance.get(1) && getMonth() == instance.get(2) && getDate() == i;
    }

    public void nextDate() {
        this.calendar_.add(5, 1);
        Log.i("MyMealMate", "Calendar new value: " + String.valueOf(this.calendar_.getTimeInMillis()));
    }

    public void prevDate() {
        this.calendar_.add(5, -1);
        Log.i("MyMealMate", "Calendar new value: " + String.valueOf(this.calendar_.getTimeInMillis()));
    }

    public void setDate(int i) {
        this.calendar_.set(5, i);
    }

    public void setDateTime(long j) {
        this.calendar_.setTimeInMillis(j);
    }

    public void setMonth(int i) {
        this.calendar_.set(2, i);
    }

    public void setYMD(int i, int i2, int i3) {
        this.calendar_.set(i, i2, i3);
    }

    public void setYear(int i) {
        this.calendar_.set(1, i);
    }
}