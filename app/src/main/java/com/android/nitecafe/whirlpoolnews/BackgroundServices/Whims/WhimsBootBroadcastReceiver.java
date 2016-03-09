package com.android.nitecafe.whirlpoolnews.BackgroundServices.Whims;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

public class WhimsBootBroadcastReceiver extends WakefulBroadcastReceiver {
    @Override public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            Intent startServiceIntent = new Intent(context, WhimsIntentService.class);
            startWakefulService(context, startServiceIntent);
        }

    }
}
