package com.android.nitecafe.whirlpoolnews.BackgroundServices.Whims;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class WhimsAlarmReceiver extends BroadcastReceiver {

    public static final int REQUEST_CODE = 2000;

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, WhimsIntentService.class);
        context.startService(i);
    }
}
