package com.android.nitecafe.whirlpoolnews.controllers;

import android.content.SharedPreferences;

import com.android.nitecafe.whirlpoolnews.constants.StringConstants;
import com.android.nitecafe.whirlpoolnews.ui.interfaces.ILoginFragment;
import com.android.nitecafe.whirlpoolnews.interfaces.IWhirlpoolRestClient;

import javax.inject.Inject;
import javax.inject.Singleton;

public class LoginController {

    private ILoginFragment mView;
    private IWhirlpoolRestClient whirlpoolRestClient;
    private SharedPreferences mSharedPreferences;

    @Inject
    @Singleton
    public LoginController(IWhirlpoolRestClient whirlpoolRestClient, SharedPreferences sharedPreferences) {
        this.whirlpoolRestClient = whirlpoolRestClient;
        mSharedPreferences = sharedPreferences;
    }

    public void attachedView(ILoginFragment view) {
        mView = view;
    }

    public void login(String apiKey) {
        whirlpoolRestClient.setApiKey(apiKey);
        saveKeyToPreference(apiKey);
        mView.showHomeScreen();
    }

    private void saveKeyToPreference(String apiKey) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(StringConstants.API_PREFERENCE_KEY, apiKey);
        editor.commit();
        mView.showSavedMessage();
    }

}
