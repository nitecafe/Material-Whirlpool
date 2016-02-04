package com.android.nitecafe.whirlpoolnews.ui.activities;

import android.os.Bundle;

import com.android.nitecafe.whirlpoolnews.R;
import com.android.nitecafe.whirlpoolnews.WhirlpoolApp;
import com.android.nitecafe.whirlpoolnews.interfaces.IWhirlpoolRestClient;
import com.android.nitecafe.whirlpoolnews.ui.fragments.LoginFragment;

import javax.inject.Inject;

public class MainActivity extends NavigationDrawerActivity implements LoginFragment.OnShowHomeScreenListener {

    @Inject IWhirlpoolRestClient mWhirlpoolRestClient;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ((WhirlpoolApp) getApplication()).getDaggerComponent().inject(this);


        if (!mWhirlpoolRestClient.hasApiKeyBeenSet()) {
            drawer.setSelection(apiKeyDrawerItem);
        } else if (savedInstanceState == null) {
            drawer.setSelection(newsItemDrawerItem);
        }
    }

    @Override public void showHomeScreen() {
        drawer.setSelection(newsItemDrawerItem);
    }
}
