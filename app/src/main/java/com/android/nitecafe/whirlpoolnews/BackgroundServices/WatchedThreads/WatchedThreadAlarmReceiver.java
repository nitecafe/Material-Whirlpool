package com.android.nitecafe.whirlpoolnews.BackgroundServices.WatchedThreads;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class WatchedThreadAlarmReceiver extends BroadcastReceiver {

    public static final int REQUEST_CODE = 1000;

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, WatchedThreadsIntentService.class);
        context.startService(i);
    }
}
