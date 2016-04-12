package com.mymealmate;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

public class CompletionCheck extends BroadcastReceiver {
    protected static boolean isBlocked;

    static {
        isBlocked = false;
    }

    public static void feedbackChecks(Context context, MyMealMateDatabase myMealMateDatabase) {
        int i;
        int i2 = 0;
        Prefs prefs = new Prefs(myMealMateDatabase, context);
        Calendar prepareCalendar = prepareCalendar();
        Calendar instance = Calendar.getInstance();
        Calendar instance2 = Calendar.getInstance();
        Calendar instance3 = Calendar.getInstance();
        instance2.set(11, prefs.getHourStartMessageBlock());
        instance2.set(12, prefs.getMinuteStartMessageBlock());
        instance3.set(11, prefs.getHourEndMessageBlock());
        instance3.set(12, prefs.getMinuteEndMessageBlock());
        if (prefs.getMessageBlockAlways() || (instance.before(instance3) && instance.after(instance2))) {
            isBlocked = true;
        } else {
            isBlocked = false;
        }
        instance.setTime(prefs.getLastDailyCheck());
        instance.add(6, 1);
        if (instance.before(prepareCalendar)) {
            instance = prepareCalendar();
            instance2 = prepareCalendar();
            int firstDayOfWeek = instance.get(7) - instance.getFirstDayOfWeek();
            instance.add(6, -firstDayOfWeek);
            instance.add(6, -7);
            instance2.add(5, -firstDayOfWeek);
            instance2.add(14, -1);
            if (DateFlagsData.getCompletedDatesCount(myMealMateDatabase, instance.getTime(), instance2.getTime()) == 0) {
                injectSMS(context, prefs.getFeedbackB1());
            }
            prefs.setLastDailyCheck(prepareCalendar.getTime());
        }
        instance = Calendar.getInstance();
        instance.setTime(prefs.getLastWeeklyCheck());
        instance.add(3, 1);
        if (instance.before(prepareCalendar)) {
            Calendar prepareCalendar2 = prepareCalendar();
            prepareCalendar2.add(6, -(prepareCalendar2.get(7) - prepareCalendar2.getFirstDayOfWeek()));
            prepareCalendar2.add(6, -7);
            i = 0;
            for (int i3 = 0; i3 < 7; i3++) {
                ArrayList arrayByDate = FoodEntryData.getArrayByDate(myMealMateDatabase, DateProvider.getStartOfDay(prepareCalendar2), DateProvider.getEndOfDay(prepareCalendar2));
                if (!arrayByDate.isEmpty()) {
                    firstDayOfWeek = i2 + 1;
                    Iterator it = arrayByDate.iterator();
                    float f = 0.0f;
                    while (it.hasNext()) {
                        f = ((FoodEntryData) it.next()).calories + f;
                    }
                    i2 = f <= TargetSettings.getGoal(myMealMateDatabase, prepareCalendar2.getTime().getTime()) + 25.0f ? i + 1 : i;
                    prepareCalendar2.add(5, 1);
                    i = i2;
                    i2 = firstDayOfWeek;
                }
            }
            if (i2 > 0) {
                i2 = (i * 100) / i2;
                if (i == 0) {
                    injectSMS(context, prefs.getFeedbackA1());
                } else if (i2 > 0 && i2 < 50) {
                    injectSMS(context, prefs.getFeedbackA2());
                } else if (i2 >= 50 && i2 < 100) {
                    injectSMS(context, prefs.getFeedbackA3());
                } else if (i2 == 100) {
                    injectSMS(context, prefs.getFeedbackA4());
                }
            }
            prefs.setLastWeeklyCheck(prepareCalendar.getTime());
        }
        Calendar instance4 = Calendar.getInstance();
        instance4.setTime(prefs.getLastMonthlyCheck());
        instance4.add(2, 1);
        if (instance4.before(prepareCalendar)) {
            instance4 = prepareCalendar();
            instance = prepareCalendar();
            i = instance4.get(5) - 1;
            instance4.add(6, -i);
            instance4.add(2, -1);
            instance.add(6, -i);
            instance.add(14, -1);
            if (WeightData.getEntryCount(myMealMateDatabase, instance4.getTime(), instance.getTime()) == 0) {
                injectSMS(context, prefs.getFeedbackB3());
            }
            long completedDatesCount = DateFlagsData.getCompletedDatesCount(myMealMateDatabase, instance4.getTime(), instance.getTime());
            instance4.add(2, -1);
            instance.add(2, -1);
            if (DateFlagsData.getCompletedDatesCount(myMealMateDatabase, instance4.getTime(), instance.getTime()) > completedDatesCount) {
                injectSMS(context, prefs.getFeedbackB2());
            }
            prefs.setLastMonthlyCheck(prepareCalendar.getTime());
        }
    }

    public static void injectSMS(Context context, String str) {
        if (!isBlocked) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("address", "My Meal Mate");
            contentValues.put("body", str);
            context.getContentResolver().insert(Uri.parse("content://sms/inbox"), contentValues);
            Intent intent = new Intent("android.intent.action.MAIN");
            intent.setType("vnd.android-dir/mms-sms");
            intent.setFlags(872415232);
            Notification notification = new Notification(R.drawable.stat_notify_sms, "My Meal Mate", System.currentTimeMillis());
            notification.defaults = -1;
            notification.flags = 16;
            notification.setLatestEventInfo(context, "My Meal Mate", str, PendingIntent.getActivity(context, 0, intent, 268435456));
            ((NotificationManager) context.getSystemService("notification")).notify(1, notification);
        }
    }

    private static Calendar prepareCalendar() {
        Calendar instance = Calendar.getInstance();
        instance.setFirstDayOfWeek(2);
        instance.set(10, 0);
        instance.set(12, 0);
        instance.set(13, 0);
        instance.set(14, 0);
        return instance;
    }

    public void onReceive(Context context, Intent intent) {
        Log.i("MyMealMate", "Feedback check alarm received");
        MyMealMateDatabase myMealMateDatabase = new MyMealMateDatabase(context);
        Prefs prefs = new Prefs(myMealMateDatabase, context);
        String participantId = prefs.getParticipantId();
        if (participantId.length() != 0) {
            WSClient.setParticipantId(participantId);
            if (prefs.getFirstRun().booleanValue()) {
                feedbackChecks(context, myMealMateDatabase);
            }
            if (prefs.getHolidayMode().booleanValue()) {
                Log.i("MyMealMate", "Holiday mode is ON: skip uploading data");
            } else {
                Log.i("MyMealMate", "Uploading data");
                if (new WSHelper().uploadAll(myMealMateDatabase)) {
                    prefs.setLastUpload(Long.valueOf(new Date().getTime()));
                } else {
                    Log.i("MyMealMate", "Background data uploading FAILED!");
                }
            }
            myMealMateDatabase.close();
        }
    }
}