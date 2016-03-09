package com.android.nitecafe.whirlpoolnews.BackgroundServices.WatchedThreads;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

public class WatchedThreadsBootBroadcastReceiver extends WakefulBroadcastReceiver {
    @Override public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            Intent startServiceIntent = new Intent(context, WatchedThreadsIntentService.class);
            startWakefulService(context, startServiceIntent);
        }

    }
}
