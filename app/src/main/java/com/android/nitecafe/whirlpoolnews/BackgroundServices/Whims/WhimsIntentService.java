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
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.StyleSpan;
import android.util.Log;

import com.android.nitecafe.whirlpoolnews.R;
import com.android.nitecafe.whirlpoolnews.WhirlpoolApp;
import com.android.nitecafe.whirlpoolnews.constants.StringConstants;
import com.android.nitecafe.whirlpoolnews.models.Whim;
import com.android.nitecafe.whirlpoolnews.ui.activities.MainActivity;
import com.android.nitecafe.whirlpoolnews.utilities.WhirlpoolDateUtils;
import com.android.nitecafe.whirlpoolnews.utilities.WhirlpoolUtils;
import com.android.nitecafe.whirlpoolnews.web.interfaces.IWhimsService;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;

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
        String frequency = sharedPreferences.getString(getString(R.string.whims_notifications_frequency_key), "");
        long l = WhirlpoolUtils.convertFrequencyStringIntoLong(frequency, getApplicationContext());
        Observable<List<Whim>> unreadWhims = whimsService.GetUnreadWhims();
        unreadWhims.subscribe(whims -> {
            for (Whim w : whims) {
                if (WhirlpoolDateUtils.isTimeWithinDuration(w.getDATE(), l)) {
                    createNotificationContent(whims);
                    break;
                }
            }
        }, throwable -> Log.e("WhimsIntentService", throwable.getMessage()));

        Log.i("WhimsIntentService", "Service running");
    }

    private void createNotificationContent(List<Whim> whims) {

        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();

        List<String> names = new ArrayList<>();
        for (Whim w : whims) {
            String sender = w.getFROM().getNAME();
            names.add(sender);
            Spannable sb = new SpannableString(sender + " " + w.getMESSAGE());
            sb.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, sender.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            inboxStyle.addLine(sb);
        }
        String content = TextUtils.join(", ", names);

        String title;
        if (whims.size() > 1)
            title = whims.size() + " new private messages";
        else
            title = whims.size() + " new private message";

        notifyNewUnreadWhims(title, content, inboxStyle);
    }

    private void notifyNewUnreadWhims(String title, String content, NotificationCompat.InboxStyle inboxStyle) {
        NotificationCompat.Builder mBuilder = buildNotification(title, content, inboxStyle);

        int mNotificationId = 2000;
        NotificationManager mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotifyMgr.notify(mNotificationId, mBuilder.build());
    }

    @NonNull
    private NotificationCompat.Builder buildNotification(String title, String content, NotificationCompat.InboxStyle inboxStyle) {

        boolean vibrate = sharedPreferences.getBoolean(getString(R.string.whims_notifications_vibrate_key), false);

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
