package com.android.nitecafe.whirlpoolnews;

import android.app.Application;

import com.android.nitecafe.whirlpoolnews.constants.StringConstants;
import com.android.nitecafe.whirlpoolnews.dagger.AppModule;
import com.android.nitecafe.whirlpoolnews.dagger.DaggerComponent;
import com.android.nitecafe.whirlpoolnews.dagger.DaggerDaggerComponent;
import com.android.nitecafe.whirlpoolnews.dagger.DaggerModule;
import com.android.nitecafe.whirlpoolnews.dagger.SubjectModule;
import com.crashlytics.android.Crashlytics;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.StandardExceptionParser;
import com.google.android.gms.analytics.Tracker;

import io.fabric.sdk.android.Fabric;

public class WhirlpoolApp extends Application {


    private static WhirlpoolApp mInstance;
    private DaggerComponent daggerComponent;
    private Tracker mTracker;

    public static synchronized WhirlpoolApp getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        if (!BuildConfig.DEBUG)
            Fabric.with(this, new Crashlytics());

        daggerComponent = DaggerDaggerComponent.builder()
                .appModule(new AppModule(this))
                .daggerModule(new DaggerModule(StringConstants.WHIRLPOOL_API_URL))
                .subjectModule(new SubjectModule())
                .build();

        mInstance = this;

//        LeakCanary.install(this);  //not working on android 6.0

    }

    synchronized public Tracker getGoogleAnalyticsTracker() {
        if (mTracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
            mTracker = analytics.newTracker(R.xml.global_tracker);
        }
        return mTracker;
    }

    /***
     * Tracking screen view
     *
     * @param screenName screen name to be displayed on GA dashboard
     */
    public void trackScreenView(String screenName) {
        Tracker t = getGoogleAnalyticsTracker();

        // Set screen name.
        t.setScreenName(screenName);

        // Send a screen view.
        t.send(new HitBuilders.ScreenViewBuilder().build());

        GoogleAnalytics.getInstance(this).dispatchLocalHits();
    }

    /***
     * Tracking exception
     *
     * @param e exception to be tracked
     */
    public void trackException(Exception e) {
        if (e != null) {
            Tracker t = getGoogleAnalyticsTracker();

            t.send(new HitBuilders.ExceptionBuilder()
                            .setDescription(
                                    new StandardExceptionParser(this, null)
                                            .getDescription(Thread.currentThread().getName(), e))
                            .setFatal(false)
                            .build()
            );
        }
    }

    /***
     * Tracking event
     *
     * @param category event category
     * @param action   action of the event
     * @param label    label
     */
    public void trackEvent(String category, String action, String label) {
        Tracker t = getGoogleAnalyticsTracker();

        // Build and send an Event.
        t.send(new HitBuilders.EventBuilder().setCategory(category).setAction(action).setLabel(label).build());
    }

    public DaggerComponent getDaggerComponent() {
        return daggerComponent;
    }
}
