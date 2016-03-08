package com.android.nitecafe.whirlpoolnews.ui.fragments;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.content.IntentCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;

import com.android.nitecafe.whirlpoolnews.BackgroundServices.WatchedThreads.BootBroadcastReceiver;
import com.android.nitecafe.whirlpoolnews.BackgroundServices.WatchedThreads.WatchedThreadAlarmReceiver;
import com.android.nitecafe.whirlpoolnews.R;
import com.android.nitecafe.whirlpoolnews.utilities.WhirlpoolUtils;

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

        Preference watchedNotificationsFrequency = getPreferenceManager().findPreference(getString(R.string.watched_notifications_frequency_key));
        watchedNotificationsFrequency.setOnPreferenceChangeListener((preference1, o1) -> {
            updateWatchedThreadAlarm(o1.toString());
            return true;
        });

        Preference watchedNotifications = getPreferenceManager().findPreference(getString(R.string.watched_notifications_key));
        watchedNotifications.setOnPreferenceChangeListener((preference, o) -> {
            Boolean isChecked = Boolean.valueOf(o.toString());

            if (isChecked) {
                String frequency = getPreferenceManager().getSharedPreferences().getString(getString(R.string.watched_notifications_frequency_key), "");
                updateWatchedThreadAlarm(frequency);
                enableBootReceiver();
            } else {
                cancelWatchedThreadAlarm();
                disableBootReceiver();
            }

            return true;
        });

    }

    private void updateWatchedThreadAlarm(String frequency) {
        long interval = WhirlpoolUtils.convertFrequencyStringIntoLong(frequency, getContext());
        scheduleWatchedThreadAlarm(interval);
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
            supportActionBar.setTitle(getString(R.string.title_settings));
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

    private void enableBootReceiver() {
        ComponentName receiver = new ComponentName(getContext(), BootBroadcastReceiver.class);
        PackageManager pm = getContext().getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
    }

    private void disableBootReceiver() {
        ComponentName receiver = new ComponentName(getContext(), BootBroadcastReceiver.class);
        PackageManager pm = getContext().getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
    }
}
