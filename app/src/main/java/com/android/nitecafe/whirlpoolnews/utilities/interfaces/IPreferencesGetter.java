package com.android.nitecafe.whirlpoolnews.utilities.interfaces;

/**
 * To get values set by users in the setting page
 */
public interface IPreferencesGetter {
    boolean isDarkThemeOn();

    String getHomeScreen();

    boolean getOpenLastPage();

    boolean isAutoMarkAsReadLastPage();

    String getFontSize();

    boolean isHideMessageFromIgnoredContactsOn();

    String getWatchedThreadsNotificationFrequency();

    String getWhimsNotificationFrequency();
}
