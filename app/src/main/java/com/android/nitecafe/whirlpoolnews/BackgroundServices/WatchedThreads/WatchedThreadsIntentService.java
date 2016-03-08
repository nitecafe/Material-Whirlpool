package com.android.nitecafe.whirlpoolnews.BackgroundServices.WatchedThreads;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.android.nitecafe.whirlpoolnews.R;
import com.android.nitecafe.whirlpoolnews.WhirlpoolApp;
import com.android.nitecafe.whirlpoolnews.constants.StringConstants;
import com.android.nitecafe.whirlpoolnews.models.Watched;
import com.android.nitecafe.whirlpoolnews.ui.activities.MainActivity;
import com.android.nitecafe.whirlpoolnews.utilities.WhirlpoolUtils;
import com.android.nitecafe.whirlpoolnews.web.interfaces.IWatchedThreadService;

import java.util.List;

import javax.inject.Inject;

public class WatchedThreadsIntentService extends IntentService {

    @Inject IWatchedThreadService mIWatchedThreadService;
    @Inject SharedPreferences sharedPreferences;

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

        String frequency = sharedPreferences.getString(getString(R.string.watched_notifications_frequency_key), "");
        mIWatchedThreadService.getUnreadWatchedThreadsInInterval(WhirlpoolUtils.convertFrequencyStringIntoLong(frequency, getApplicationContext()))
                .subscribe(watcheds -> {
                    if (watcheds.size() > 0)
                        createNotificationContent(watcheds);
                });
        Log.i("WatchedIntentService", "Service running");
    }

    private void createNotificationContent(List<Watched> watcheds) {
        notifyNewUnreadThreads("New Replies", "You have " + watcheds.size() + " threads with new replies");
    }

    private void notifyNewUnreadThreads(String title, String content) {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.white_notification_icon)
                        .setAutoCancel(true)
                        .setContentTitle(title)
                        .setContentText(content);

        Intent resultIntent = new Intent(this, MainActivity.class);
        resultIntent.putExtra(StringConstants.NOTIFICATION_INTENT_SCREEN_KEY, StringConstants.NOTIFICATION_INTENT_WATCHED_SCREEN_KEY);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);

        int mNotificationId = 1000;
        NotificationManager mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotifyMgr.notify(mNotificationId, mBuilder.build());
    }
}
