package com.android.nitecafe.whirlpoolnews.ui;

import android.os.Bundle;

import com.android.nitecafe.whirlpoolnews.R;
import com.android.nitecafe.whirlpoolnews.WhirlpoolApp;
import com.android.nitecafe.whirlpoolnews.interfaces.IWhirlpoolRestClient;

import javax.inject.Inject;

public class MainActivity extends NavigationDrawerActivity {

    @Inject IWhirlpoolRestClient mWhirlpoolRestClient;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ((WhirlpoolApp) getApplication()).getDaggerComponent().inject(this);
    }

    @Override protected void onResume() {
        super.onResume();

        if (!mWhirlpoolRestClient.hasApiKeyBeenSet()) {
            drawer.setSelection(apiKey);
        }
    }
}
