package com.android.nitecafe.whirlpoolnews.utilities;

import android.content.Context;
import android.content.SharedPreferences;

import com.android.nitecafe.whirlpoolnews.R;
import com.android.nitecafe.whirlpoolnews.constants.StringConstants;
import com.android.nitecafe.whirlpoolnews.utilities.interfaces.IPreferencesGetter;

public class PreferencesGetter implements IPreferencesGetter {

    private SharedPreferences sharedPreferences;
    private Context context;

    public PreferencesGetter(SharedPreferences sharedPreferences, Context context) {
        this.sharedPreferences = sharedPreferences;
        this.context = context;
    }

    @Override public boolean isDarkThemeOn() {
        return sharedPreferences.getBoolean(context.getResources().getString(R.string.dark_theme_key), false);
    }

    @Override public String getHomeScreen() {
        return sharedPreferences.getString(context.getString(R.string.home_screen_key), "");
    }

    @Override public boolean getOpenLastPage() {
        return sharedPreferences.getBoolean(context.getString(R.string.go_last_page_key), false);
    }

    @Override
    public boolean isAutoMarkAsReadLastPage() {
        return sharedPreferences.getBoolean(context.getString(R.string.auto_mark_read_key), false);
    }

    @Override
    public String getFontSize() {
        return sharedPreferences.getString(context.getString(R.string.font_size_key), "");
    }

    @Override
    public boolean isHideMessageFromIgnoredContactsOn() {
        return sharedPreferences.getBoolean(context.getResources().getString(R.string.whims_hide_ignored_key), false);
    }

    @Override
    public String getWatchedThreadsNotificationFrequency() {
        return sharedPreferences.getString(context.getResources().getString(R.string.watched_notifications_frequency_key), "");
    }

    @Override
    public String getWhimsNotificationFrequency() {
        return sharedPreferences.getString(context.getResources().getString(R.string.whims_notifications_frequency_key), "");
    }

    @Override
    public String getUserName() {
        return sharedPreferences.getString(StringConstants.USERNAME, "");
    }
}
