package com.android.nitecafe.whirlpoolnews.dagger;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.android.nitecafe.whirlpoolnews.interfaces.IWhirlpoolRestClient;
import com.android.nitecafe.whirlpoolnews.scheduler.ISchedulerManager;
import com.android.nitecafe.whirlpoolnews.scheduler.SchedulerManager;
import com.android.nitecafe.whirlpoolnews.utilities.IThreadScraper;
import com.android.nitecafe.whirlpoolnews.utilities.IWatchedThreadIdentifier;
import com.android.nitecafe.whirlpoolnews.utilities.ThreadScraper;
import com.android.nitecafe.whirlpoolnews.utilities.WatchedThreadIdentifier;
import com.android.nitecafe.whirlpoolnews.web.WhirlpoolRestClient;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.otto.Bus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;

@Module
public class DaggerModule {

    private String mBaseUrl;

    public DaggerModule(String baseUrl) {
        mBaseUrl = baseUrl;
    }

    @Provides
    @Singleton OkHttpClient provideOkHttpClient() {
        return new OkHttpClient();
    }

    @Provides
    @Singleton Retrofit provideRetrofit(OkHttpClient client) {
        return new Retrofit.Builder()
                .baseUrl(mBaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(client)
                .build();
    }

//    @Provides
//    @Singleton IWhirlpoolService provideWhirlPoolService(Retrofit retrofit) {
//        return retrofit.create(IWhirlpoolService.class);
//    }

    @Provides
    @Singleton SharedPreferences providesSharedPreferences(Application application) {
        return PreferenceManager.getDefaultSharedPreferences(application);
    }

    @Provides
    @Singleton IWhirlpoolRestClient providesWhirlpoolRestClient(WhirlpoolRestClient restClient) {
        return restClient;
    }

    @Provides
    @Singleton IWatchedThreadIdentifier providesWatchedThreadIdentifier(WatchedThreadIdentifier watchedThreadIdentifier) {
        return watchedThreadIdentifier;
    }

    @Provides
    @Singleton Bus provideBus() {
        return new Bus();
    }

    @Provides
    @Singleton ISchedulerManager provideSchedulerManager() {
        return new SchedulerManager();
    }

    @Provides
    @Singleton IThreadScraper provideThreadScraper() {
        return new ThreadScraper();
    }
}
