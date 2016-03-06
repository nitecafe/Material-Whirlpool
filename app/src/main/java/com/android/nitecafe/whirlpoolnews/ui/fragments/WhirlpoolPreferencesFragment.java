package com.android.nitecafe.whirlpoolnews.ui.fragments;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.IntentCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;

import com.android.nitecafe.whirlpoolnews.BackgroundServices.WatchedThreads.WatchedThreadAlarmReceiver;
import com.android.nitecafe.whirlpoolnews.R;

public class WhirlpoolPreferencesFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.preferences);
        setToolbarTitle();

        Preference darkThemePreference = getPreferenceManager().findPreference(getString(R.string.dark_theme_key));
        darkThemePreference.setOnPreferenceChangeListener((preference, o) -> {
            restartApplication();
            return true;
        });

        Preference biggerFontSizePreference = getPreferenceManager().findPreference(getString(R.string.font_size_key));
        biggerFontSizePreference.setOnPreferenceChangeListener((preference, o) -> {
            restartApplication();
            return true;
        });

        Preference watchedNotifications = getPreferenceManager().findPreference(getString(R.string.watched_notifications_key));
        watchedNotifications.setOnPreferenceChangeListener((preference, o) -> {
            Boolean isChecked = Boolean.valueOf(o.toString());

            if (isChecked) {
                String frequency = getPreferenceManager().getSharedPreferences().getString(getString(R.string.watched_notifications_frequency_key), "");

                if (frequency.equals(getString(R.string.watched_thread_notification_frequency_15)))
                    scheduleWatchedThreadAlarm(AlarmManager.INTERVAL_FIFTEEN_MINUTES);
                else if (frequency.equals(getString(R.string.watched_thread_notification_frequency_30)))
                    scheduleWatchedThreadAlarm(AlarmManager.INTERVAL_HALF_HOUR);
                else if (frequency.equals(getString(R.string.watched_thread_notification_frequency_60)))
                    scheduleWatchedThreadAlarm(AlarmManager.INTERVAL_HOUR);
                else if (frequency.equals(getString(R.string.watched_thread_notification_frequency_4_hours)))
                    scheduleWatchedThreadAlarm(AlarmManager.INTERVAL_HOUR * 4);
                else if (frequency.equals(getString(R.string.watched_thread_notification_frequency_half_day)))
                    scheduleWatchedThreadAlarm(AlarmManager.INTERVAL_HALF_DAY);
                else
                    scheduleWatchedThreadAlarm(AlarmManager.INTERVAL_DAY);
            } else
                cancelWatchedThreadAlarm();

            return true;
        });

    }

    /**
     * Restart application to apply theme
     */
    private void restartApplication() {
        startActivity(IntentCompat.makeRestartActivityTask(getActivity().getComponentName()));
    }

    private void setToolbarTitle() {
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        ActionBar supportActionBar = activity.getSupportActionBar();
        if (supportActionBar != null)
            supportActionBar.setTitle("Settings");
    }


    private void scheduleWatchedThreadAlarm(long interval) {
        final PendingIntent watchedThreadsPendingIntent = getWatchedThreadsPendingIntent();
        AlarmManager alarm = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        alarm.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, interval,
                interval, watchedThreadsPendingIntent);
    }

    private void cancelWatchedThreadAlarm() {
        final PendingIntent watchedThreadsPendingIntent = getWatchedThreadsPendingIntent();
        AlarmManager alarm = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        alarm.cancel(watchedThreadsPendingIntent);
    }

    private PendingIntent getWatchedThreadsPendingIntent() {
        Intent intent = new Intent(getActivity().getApplicationContext(), WatchedThreadAlarmReceiver.class);
        return PendingIntent.getBroadcast(getContext(), WatchedThreadAlarmReceiver.REQUEST_CODE,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }
}
