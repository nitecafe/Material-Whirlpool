package com.android.nitecafe.whirlpoolnews.BackgroundServices.Whims;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.preference.PreferenceManager;

import com.android.nitecafe.whirlpoolnews.R;
import com.android.nitecafe.whirlpoolnews.utilities.WhirlpoolUtils;

public class WhimsBootBroadcastReceiver extends BroadcastReceiver {
    @Override public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
            String frequency = defaultSharedPreferences.getString(context.getString(R.string.whims_notifications_frequency_key), "");
            long interval = WhirlpoolUtils.convertFrequencyStringIntoLong(frequency, context);
            scheduleAlarm(context, interval);
        }
    }

    private PendingIntent getWhimsPendingIntent(Context context) {
        Intent intent = new Intent(context, WhimsAlarmReceiver.class);
        return PendingIntent.getBroadcast(context, WhimsAlarmReceiver.REQUEST_CODE,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private void scheduleAlarm(Context context, long interval) {
        final PendingIntent whimsPendingIntent = getWhimsPendingIntent(context);
        AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarm.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, interval,
                interval, whimsPendingIntent);
    }
}
