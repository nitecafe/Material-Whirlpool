package com.android.nitecafe.whirlpoolnews.dagger;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.android.nitecafe.whirlpoolnews.scheduler.ISchedulerManager;
import com.android.nitecafe.whirlpoolnews.scheduler.SchedulerManager;
import com.android.nitecafe.whirlpoolnews.services.IPostBookmarkService;
import com.android.nitecafe.whirlpoolnews.services.PostBookmarkService;
import com.android.nitecafe.whirlpoolnews.utilities.CachingUtils;
import com.android.nitecafe.whirlpoolnews.utilities.FavouriteThreadService;
import com.android.nitecafe.whirlpoolnews.utilities.ObjectSerializer;
import com.android.nitecafe.whirlpoolnews.utilities.PreferencesGetter;
import com.android.nitecafe.whirlpoolnews.utilities.ThreadScraper;
import com.android.nitecafe.whirlpoolnews.utilities.interfaces.ICachingUtils;
import com.android.nitecafe.whirlpoolnews.utilities.interfaces.IFavouriteThreadService;
import com.android.nitecafe.whirlpoolnews.utilities.interfaces.IObjectSerializer;
import com.android.nitecafe.whirlpoolnews.utilities.interfaces.IPreferencesGetter;
import com.android.nitecafe.whirlpoolnews.utilities.interfaces.IThreadScraper;
import com.android.nitecafe.whirlpoolnews.web.WatchedThreadService;
import com.android.nitecafe.whirlpoolnews.web.WhimsService;
import com.android.nitecafe.whirlpoolnews.web.WhirlpoolRestClient;
import com.android.nitecafe.whirlpoolnews.web.WhirlpoolRestService;
import com.android.nitecafe.whirlpoolnews.web.interfaces.IWatchedThreadService;
import com.android.nitecafe.whirlpoolnews.web.interfaces.IWhimsService;
import com.android.nitecafe.whirlpoolnews.web.interfaces.IWhirlpoolRestClient;
import com.android.nitecafe.whirlpoolnews.web.interfaces.IWhirlpoolRestService;
import com.squareup.okhttp.OkHttpClient;

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

    @Provides
    @Singleton SharedPreferences providesSharedPreferences(Application application) {
        return PreferenceManager.getDefaultSharedPreferences(application);
    }

    @Provides
    @Singleton IWhirlpoolRestClient providesWhirlpoolRestClient(WhirlpoolRestClient restClient) {
        return restClient;
    }

    @Provides
    @Singleton IWatchedThreadService providesWatchedThreadIdentifier(WatchedThreadService watchedThreadService) {
        return watchedThreadService;
    }

    @Provides
    @Singleton ISchedulerManager provideSchedulerManager() {
        return new SchedulerManager();
    }

    @Provides
    @Singleton IThreadScraper provideThreadScraper() {
        return new ThreadScraper();
    }

    @Provides
    @Singleton ICachingUtils provideCacheUtils(CachingUtils cachingUtils) {
        return cachingUtils;
    }

    @Provides
    @Singleton IWhirlpoolRestService provideWhirlpoolRestService(WhirlpoolRestService whirlpoolRestService) {
        return whirlpoolRestService;
    }

    @Provides
    @Singleton IFavouriteThreadService provideFavouriteThreadService(FavouriteThreadService favouriteThreadService) {
        return favouriteThreadService;
    }

    @Provides
    @Singleton IObjectSerializer provideObjectSerializer(ObjectSerializer objectSerializer) {
        return objectSerializer;
    }

    @Provides
    @Singleton IPreferencesGetter providesPreferenceGetter(Application application, SharedPreferences sharedPreferences) {
        return new PreferencesGetter(sharedPreferences, application);
    }

    @Provides
    @Singleton IPostBookmarkService providesPostBookmarkService(PostBookmarkService postBookmarkService) {
        return postBookmarkService;
    }

    @Provides
    @Singleton IWhimsService providesWhimsService(WhimsService whimsService) {
        return whimsService;
    }
}
