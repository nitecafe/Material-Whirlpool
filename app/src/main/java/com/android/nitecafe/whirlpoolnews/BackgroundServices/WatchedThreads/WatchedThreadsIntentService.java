package com.android.nitecafe.whirlpoolnews.BackgroundServices.WatchedThreads;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.text.TextUtils;
import android.util.Log;

import com.android.nitecafe.whirlpoolnews.R;
import com.android.nitecafe.whirlpoolnews.WhirlpoolApp;
import com.android.nitecafe.whirlpoolnews.constants.StringConstants;
import com.android.nitecafe.whirlpoolnews.models.Watched;
import com.android.nitecafe.whirlpoolnews.models.WatchedList;
import com.android.nitecafe.whirlpoolnews.ui.activities.MainActivity;
import com.android.nitecafe.whirlpoolnews.utilities.WhirlpoolDateUtils;
import com.android.nitecafe.whirlpoolnews.utilities.WhirlpoolUtils;
import com.android.nitecafe.whirlpoolnews.web.interfaces.IWhirlpoolRestClient;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;

public class WatchedThreadsIntentService extends IntentService {

    @Inject IWhirlpoolRestClient whirlpoolRestClient;
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
        WakefulBroadcastReceiver.completeWakefulIntent(intent);

        String frequency = sharedPreferences.getString(getString(R.string.watched_notifications_frequency_key), "");
        Observable<WatchedList> unreadWatchedThreads = whirlpoolRestClient.GetUnreadWatched();
        long l = WhirlpoolUtils.convertFrequencyStringIntoLong(frequency, getApplicationContext());
        unreadWatchedThreads.subscribe(watchedList -> {
            for (Watched w : watchedList.getWATCHED()) {
                if (WhirlpoolDateUtils.isTimeWithinDuration(w.getLASTDATE(), l)) {
                    createNotificationContent(watchedList.getWATCHED());
                    break;
                }
            }
        });

        Log.i("WatchedIntentService", "Service running");
    }

    private void createNotificationContent(List<Watched> watcheds) {

        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();

        List<String> titleList = new ArrayList<>();
        for (Watched w : watcheds) {
            titleList.add(w.getTITLE());
            inboxStyle.addLine(w.getTITLE());
        }
        String content = TextUtils.join(" | ", titleList);

        String title;
        if (watcheds.size() > 1)
            title = watcheds.size() + " threads with new replies";
        else
            title = watcheds.size() + " thread with new replies";


        notifyNewUnreadThreads(title, content, inboxStyle);
    }

    private void notifyNewUnreadThreads(String title, String content, NotificationCompat.InboxStyle inboxStyle) {
        NotificationCompat.Builder mBuilder = buildNotification(title, content, inboxStyle);

        int mNotificationId = 1000;
        NotificationManager mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotifyMgr.notify(mNotificationId, mBuilder.build());
    }

    @NonNull
    private NotificationCompat.Builder buildNotification(String title, String content, NotificationCompat.InboxStyle inboxStyle) {

        boolean vibrate = sharedPreferences.getBoolean(getString(R.string.watched_notifications_vibrate_key), false);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.white_notification_icon)
                        .setAutoCancel(true)
                        .setContentTitle(title)
                        .setContentText(content)
                        .setStyle(inboxStyle);

        if (vibrate)
            mBuilder.setDefaults(Notification.DEFAULT_ALL);
        else
            mBuilder.setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND);

        Intent resultIntent = new Intent(this, MainActivity.class);
        resultIntent.putExtra(StringConstants.NOTIFICATION_INTENT_SCREEN_KEY, StringConstants.NOTIFICATION_INTENT_WATCHED_SCREEN_KEY);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);
        return mBuilder;
    }
}
