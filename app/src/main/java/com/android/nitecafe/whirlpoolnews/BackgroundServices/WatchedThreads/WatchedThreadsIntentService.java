package com.android.nitecafe.whirlpoolnews.BackgroundServices.WatchedThreads;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.android.nitecafe.whirlpoolnews.WhirlpoolApp;
import com.android.nitecafe.whirlpoolnews.web.interfaces.IWatchedThreadService;

import javax.inject.Inject;

public class WatchedThreadsIntentService extends IntentService {

    @Inject IWatchedThreadService mIWatchedThreadService;

    public WatchedThreadsIntentService() {
        super("Watched Thread Intent Service");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ((WhirlpoolApp) getApplication()).getDaggerComponent().inject(this);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        mIWatchedThreadService.getWatchedThreads();
        Log.i("WatchedIntentService", "Service running");
    }
}
