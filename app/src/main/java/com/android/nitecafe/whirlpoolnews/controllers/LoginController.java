package com.android.nitecafe.whirlpoolnews.controllers;

import android.content.SharedPreferences;

import com.android.nitecafe.whirlpoolnews.constants.StringConstants;
import com.android.nitecafe.whirlpoolnews.interfaces.IWhirlpoolRestClient;
import com.android.nitecafe.whirlpoolnews.ui.interfaces.ILoginFragment;
import com.android.nitecafe.whirlpoolnews.web.interfaces.IWatchedThreadService;

import javax.inject.Inject;
import javax.inject.Named;

import rx.subjects.PublishSubject;

public class LoginController {

    private ILoginFragment loginFragment;
    private IWhirlpoolRestClient whirlpoolRestClient;
    private SharedPreferences sharedPreferences;
    private IWatchedThreadService watchedThreadIdentifier;
    private PublishSubject<Void> whimSubject;

    @Inject
    public LoginController(IWhirlpoolRestClient whirlpoolRestClient,
                           SharedPreferences sharedPreferences,
                           IWatchedThreadService watchedThreadIdentifier,
                           @Named("whim") PublishSubject<Void> whimSubject) {
        this.whirlpoolRestClient = whirlpoolRestClient;
        this.sharedPreferences = sharedPreferences;
        this.watchedThreadIdentifier = watchedThreadIdentifier;
        this.whimSubject = whimSubject;
    }

    public void attachedView(ILoginFragment view) {
        loginFragment = view;
    }

    public void login(String apiKey) {
        whirlpoolRestClient.setApiKey(apiKey);
        saveKeyToPreference(apiKey);
        watchedThreadIdentifier.getWatchedThreads(); // refresh watched threads
        whimSubject.onNext(null);
        if (loginFragment != null)
            loginFragment.showHomeScreen();
    }

    private void saveKeyToPreference(String apiKey) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(StringConstants.API_PREFERENCE_KEY, apiKey);
        editor.commit();
        if (loginFragment != null)
            loginFragment.showSavedMessage();
    }

}
