package com.android.nitecafe.whirlpoolnews.utilities;

/**
 * To get values set by users in the setting page
 */
public interface IPreferencesGetter {
    boolean isDarkThemeOn();

    String getHomeScreen();

    boolean getOpenLastPage();
}
