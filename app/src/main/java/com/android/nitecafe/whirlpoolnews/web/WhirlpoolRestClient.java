package com.android.nitecafe.whirlpoolnews.web;

import android.content.SharedPreferences;

import com.android.nitecafe.whirlpoolnews.constants.StringConstants;
import com.android.nitecafe.whirlpoolnews.models.ForumList;
import com.android.nitecafe.whirlpoolnews.models.ForumThreadList;
import com.android.nitecafe.whirlpoolnews.models.NewsList;
import com.android.nitecafe.whirlpoolnews.models.RecentList;
import com.android.nitecafe.whirlpoolnews.models.ScrapedPostList;
import com.android.nitecafe.whirlpoolnews.models.ScrapedThread;
import com.android.nitecafe.whirlpoolnews.models.ScrapedThreadList;
import com.android.nitecafe.whirlpoolnews.models.UserDetailsList;
import com.android.nitecafe.whirlpoolnews.models.WatchedList;
import com.android.nitecafe.whirlpoolnews.models.WhimsList;
import com.android.nitecafe.whirlpoolnews.utilities.interfaces.IThreadScraper;
import com.android.nitecafe.whirlpoolnews.web.interfaces.IWhirlpoolRestClient;
import com.android.nitecafe.whirlpoolnews.web.interfaces.IWhirlpoolService;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.Request;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit.Retrofit;
import rx.Observable;

@Singleton
public class WhirlpoolRestClient implements IWhirlpoolRestClient {

    private IWhirlpoolService whirlpoolService;
    private Retrofit retrofit;
    private SharedPreferences mSharedPreferences;
    private IThreadScraper threadScraper;

    @Inject
    public WhirlpoolRestClient(Retrofit retrofit, SharedPreferences sharedPreferences, IThreadScraper threadScraper) {
        this.retrofit = retrofit;
        mSharedPreferences = sharedPreferences;
        this.threadScraper = threadScraper;

        String apiKey = getKeyFromPreference();
        if (!apiKey.isEmpty()) {
            setApiKey(apiKey);
        } else {
            setApiKey("");
        }
    }

    @Override
    public void setApiKey(String apikey) {
        retrofit.client().interceptors().clear();
        retrofit.client().interceptors().add(chain -> {
            final HttpUrl build = chain.request().httpUrl().newBuilder()
                    .addQueryParameter("key", apikey)
                    .addQueryParameter("output", "json")
                    .build();

            final Request request = chain.request().newBuilder().url(build)
                    .addHeader("User-Agent", StringConstants.USER_AGENT_NAME)
                    .build();
            return chain.proceed(request);
        });

        whirlpoolService = retrofit.create(IWhirlpoolService.class);

        saveKeyToPreference(apikey);
    }

    private void saveKeyToPreference(String apiKey) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(StringConstants.API_PREFERENCE_KEY, apiKey);
        editor.apply();
    }

    private String getKeyFromPreference() {
        return mSharedPreferences.getString(StringConstants.API_PREFERENCE_KEY, "");
    }

    @Override
    public Boolean hasApiKeyBeenSet() {
        return !getKeyFromPreference().isEmpty();
    }

    @Override
    public Observable<NewsList> GetNews() {
        return getWhirlpoolService().GetNews();
    }

    @Override
    public Observable<ForumList> GetForum() {
        return getWhirlpoolService().GetForum();
    }

    @Override
    public Observable<RecentList> GetRecent() {
        return getWhirlpoolService().GetRecent();
    }

    protected IWhirlpoolService getWhirlpoolService() {
        return whirlpoolService;
    }

    @Override
    public Observable<WatchedList> GetUnreadWatched() {
        return getWhirlpoolService().GetWatched(0);
    }

    @Override
    public Observable<WatchedList> GetAllWatched() {
        return getWhirlpoolService().GetWatched(1);
    }

    @Override
    public Observable<ForumThreadList> GetThreads(int forumIds, int threadCount) {
        return getWhirlpoolService().GetThreads(forumIds, threadCount);
    }

    @Override
    public Observable<ScrapedThreadList> GetScrapedThreads(int forumIds, int pageCount, int groupId) {
        return threadScraper.scrapeThreadsFromForumObservable(forumIds, pageCount, groupId);
    }

    @Override
    public Observable<ScrapedPostList> GetScrapedPosts(int threadId, int page) {
        return threadScraper.scrapePostsFromThreadObservable(threadId, page);
    }

    @Override
    public Observable<Void> SetThreadAsWatch(int threadId) {
        return getWhirlpoolService().SetThreadAsWatched(threadId);
    }

    @Override
    public Observable<Void> SetThreadAsUnwatch(int threadId) {
        return getWhirlpoolService().SetThreadAsUnwatched(threadId);
    }

    @Override
    public Observable<Void> MarkThreadAsRead(int threadId) {
        return getWhirlpoolService().SetThreadAsRead(threadId);
    }

    @Override
    public Observable<WhimsList> GetWhims() {
        return getWhirlpoolService().GetWhims();
    }

    @Override
    public Observable<Void> MarkWhimAsRead(int whimId) {
        return getWhirlpoolService().MarkWhimAsRead(whimId);
    }

    @Override
    public Observable<ArrayList<ScrapedThread>> GetPopularThreads() {
        return threadScraper.ScrapPopularThreadsObservable();
    }

    @Override
    public Observable<List<ScrapedThread>> SearchThreads(int forumId, int groupId, String query) {
        return threadScraper.searchThreadsObservable(forumId, groupId, query);
    }

    @Override
    public Observable<UserDetailsList> GetUserDetails() {
        return getWhirlpoolService().GetUserDetails();
    }

    @Override
    public void saveUserName(String s) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(StringConstants.USERNAME, s);
        editor.apply();
    }
}
