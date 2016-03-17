package com.android.nitecafe.whirlpoolnews.BackgroundServices.WatchedThreads;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.preference.PreferenceManager;

import com.android.nitecafe.whirlpoolnews.R;
import com.android.nitecafe.whirlpoolnews.utilities.WhirlpoolUtils;

public class WatchedThreadsBootBroadcastReceiver extends BroadcastReceiver {

    @Override public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
            boolean notificationOn = defaultSharedPreferences.getBoolean(context.getString(R.string.watched_notifications_key), false);
            if (notificationOn) {
                String frequency = defaultSharedPreferences.getString(context.getString(R.string.watched_notifications_frequency_key), "");
                long interval = WhirlpoolUtils.convertFrequencyStringIntoLong(frequency, context);
                scheduleAlarm(context, interval);
            }
        }
    }

    private PendingIntent getWatchedThreadsPendingIntent(Context context) {
        Intent intent = new Intent(context, WatchedThreadAlarmReceiver.class);
        return PendingIntent.getBroadcast(context, WatchedThreadAlarmReceiver.REQUEST_CODE,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private void scheduleAlarm(Context context, long interval) {
        PendingIntent watchedThreadsPendingIntent = getWatchedThreadsPendingIntent(context);
        AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarm.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, 1000,
                interval, watchedThreadsPendingIntent);
    }
}
