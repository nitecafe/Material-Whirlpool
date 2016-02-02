package com.android.nitecafe.whirlpoolnews.controllers;

import com.android.nitecafe.whirlpoolnews.BuildConfig;
import com.android.nitecafe.whirlpoolnews.interfaces.ILoginFragment;
import com.android.nitecafe.whirlpoolnews.interfaces.IWhirlpoolRestClient;

import javax.inject.Inject;

public class LoginController {

    private ILoginFragment mView;
    private IWhirlpoolRestClient whirlpoolRestClient;

    @Inject
    public LoginController(IWhirlpoolRestClient whirlpoolRestClient) {

        this.whirlpoolRestClient = whirlpoolRestClient;
    }

    public void attachedView(ILoginFragment view) {
        mView = view;
    }

    public void login(String apiKey) {
        whirlpoolRestClient.setApiKey(BuildConfig.WHIRLPOOL_API_KEY);
    }


}
