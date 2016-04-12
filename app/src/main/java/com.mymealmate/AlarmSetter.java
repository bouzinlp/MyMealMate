package com.mymealmate;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

public class AlarmSetter extends BroadcastReceiver {
    private static final long ALARM_INTERVAL = 43200000;

    public static void setFeedbackAlarm(Context context) {
        ((AlarmManager) context.getSystemService("alarm")).setInexactRepeating(3, SystemClock.elapsedRealtime() + ALARM_INTERVAL, ALARM_INTERVAL, PendingIntent.getBroadcast(context, 0, new Intent("MMM_FEEDBACK_CHECK"), 0));
    }

    public void onReceive(Context context, Intent intent) {
        setFeedbackAlarm(context);
    }
}