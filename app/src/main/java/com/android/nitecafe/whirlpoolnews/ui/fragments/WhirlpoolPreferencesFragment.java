package com.android.nitecafe.whirlpoolnews.ui.fragments;

import android.os.Bundle;
import android.support.v4.content.IntentCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;

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

            String frequency = getPreferenceManager().getSharedPreferences().getString(getString(R.string.watched_notifications_frequency_key)
                    , "");

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
}
