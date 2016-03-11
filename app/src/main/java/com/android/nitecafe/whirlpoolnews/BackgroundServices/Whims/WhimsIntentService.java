package com.android.nitecafe.whirlpoolnews.BackgroundServices.Whims;

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
import com.android.nitecafe.whirlpoolnews.models.Whim;
import com.android.nitecafe.whirlpoolnews.ui.activities.MainActivity;
import com.android.nitecafe.whirlpoolnews.utilities.WhirlpoolUtils;
import com.android.nitecafe.whirlpoolnews.web.IWhimsService;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class WhimsIntentService extends IntentService {

    @Inject IWhimsService whimsService;
    @Inject SharedPreferences sharedPreferences;

    public WhimsIntentService() {
        super("Whims Intent Service");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ((WhirlpoolApp) getApplication()).getDaggerComponent().inject(this);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        WakefulBroadcastReceiver.completeWakefulIntent(intent);

        String frequency = sharedPreferences.getString(getString(R.string.whims_notifications_frequency_key), "");
        whimsService.GetUnreadWhimsInInterval(WhirlpoolUtils.convertFrequencyStringIntoLong(frequency, getApplicationContext()))
                .subscribe(whims -> {
                    if (whims.size() > 0)
                        createNotificationContent(whims);
                });

        Log.i("WhimsIntentService", "Service running");
    }

    private void createNotificationContent(List<Whim> whims) {

        List<String> names = new ArrayList<>();
        for (Whim w : whims) {
            names.add(w.getFROM().getNAME());
        }
        String content = TextUtils.join(", ", names);

        String title;
        if (whims.size() > 1)
            title = whims.size() + " new private messages";
        else
            title = whims.size() + " new private message";

        notifyNewUnreadWhims(title, content, content);
    }

    private void notifyNewUnreadWhims(String title, String content, String bigContent) {
        NotificationCompat.Builder mBuilder = buildNotification(title, content, bigContent);

        int mNotificationId = 2000;
        NotificationManager mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotifyMgr.notify(mNotificationId, mBuilder.build());
    }

    @NonNull
    private NotificationCompat.Builder buildNotification(String title, String content, String bigContent) {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.white_notification_icon)
                        .setAutoCancel(true)
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setContentTitle(title)
                        .setContentText(content)
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(bigContent));

        Intent resultIntent = new Intent(this, MainActivity.class);
        resultIntent.putExtra(StringConstants.NOTIFICATION_INTENT_SCREEN_KEY, StringConstants.NOTIFICATION_INTENT_WHIMS_SCREEN_KEY);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);
        return mBuilder;
    }
}
