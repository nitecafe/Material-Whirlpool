package com.android.nitecafe.whirlpoolnews.ui.interfaces;

public interface ILoginFragment {
    void showHomeScreen();

    void updateUsername(String s);

    void showLoginFailureMessage();

    void showLoggingInProgressLoader();

    void hideProgressLoader();

    void showLoginSucessMessage(String username);
}
