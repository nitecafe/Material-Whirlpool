package com.android.nitecafe.whirlpoolnews.utilities;


import android.content.SharedPreferences;

import com.android.nitecafe.whirlpoolnews.models.ForumList;
import com.android.nitecafe.whirlpoolnews.models.NewsList;
import com.android.nitecafe.whirlpoolnews.models.RecentList;
import com.android.nitecafe.whirlpoolnews.models.ScrapedThread;
import com.android.nitecafe.whirlpoolnews.models.WatchedList;
import com.android.nitecafe.whirlpoolnews.models.WhimsList;
import com.android.nitecafe.whirlpoolnews.utilities.interfaces.ICachingUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import javax.inject.Inject;

public class CachingUtils implements ICachingUtils {

    private final Gson gson;
    private final String NEWS_CACHE_KEY = "news_cache";
    private final String POPULAR_CACHE_KEY = "popular_cache";
    private final String FORUM_CACHE_KEY = "forum_cache";
    private final String RECENT_CACHE_KEY = "recent_cache";
    private final String UNREAD_WATCHED_CACHE_KEY = "unread_watched_cache";
    private final String ALL_WATCHED_CACHE_KEY = "all_watched_cache";
    private final String WHIMS_CACHE_KEY = "whims_cache";
    private SharedPreferences sharedPreferences;

    @Inject
    public CachingUtils(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
        gson = new Gson();
    }

    private Object getObjectFromCache(String cacheKey, Class classToDeserialize) {
        String string = sharedPreferences.getString(cacheKey, "");

        if (!string.isEmpty())
            return gson.fromJson(string, classToDeserialize);
        else
            return null;
    }

    private Object getObjectFromCache(String cacheKey, Type classToDeserialize) {
        String string = sharedPreferences.getString(cacheKey, "");

        if (!string.isEmpty())
            return gson.fromJson(string, classToDeserialize);
        else
            return null;
    }

    private void saveObject(String cacheKey, Object object) {
        String s = gson.toJson(object);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString(cacheKey, s);
        edit.commit();
    }

    @Override public void cacheNews(NewsList newsList) {
        saveObject(NEWS_CACHE_KEY, newsList);
    }

    @Override public NewsList getNewsCache() {
        return (NewsList) getObjectFromCache(NEWS_CACHE_KEY, NewsList.class);
    }

    @Override public ForumList getForumCache() {
        return (ForumList) getObjectFromCache(FORUM_CACHE_KEY, ForumList.class);
    }

    @Override public void cacheForum(ForumList forumList) {
        saveObject(FORUM_CACHE_KEY, forumList);
    }

    @Override public ArrayList<ScrapedThread> getPopularThreadsCache() {

        Type listType = new TypeToken<ArrayList<ScrapedThread>>() {
        }.getType();

        return (ArrayList<ScrapedThread>) getObjectFromCache(POPULAR_CACHE_KEY, listType);
    }

    @Override public void cachePopularThreads(ArrayList<ScrapedThread> scrapedThreadList) {
        saveObject(POPULAR_CACHE_KEY, scrapedThreadList);
    }

    @Override public RecentList getRecentThreadsCache() {
        return (RecentList) getObjectFromCache(RECENT_CACHE_KEY, RecentList.class);
    }

    @Override public void cacheRecentThreads(RecentList recentList) {
        saveObject(RECENT_CACHE_KEY, recentList);
    }

    @Override public WatchedList getUnreadWatchedThreadsCache() {
        return (WatchedList) getObjectFromCache(UNREAD_WATCHED_CACHE_KEY, WatchedList.class);
    }

    @Override public void cacheUnreadWatchedThreads(WatchedList watchedList) {
        saveObject(UNREAD_WATCHED_CACHE_KEY, watchedList);
    }

    @Override public WatchedList getAllWatchedThreadsCache() {
        return (WatchedList) getObjectFromCache(ALL_WATCHED_CACHE_KEY, WatchedList.class);
    }

    @Override public void cacheAllWatchedThreads(WatchedList watchedList) {
        saveObject(ALL_WATCHED_CACHE_KEY, watchedList);
    }

    @Override public WhimsList getWhimsCache() {
        return (WhimsList) getObjectFromCache(WHIMS_CACHE_KEY, WhimsList.class);
    }

    @Override public void cacheWhims(WhimsList whimsList) {
        saveObject(WHIMS_CACHE_KEY, whimsList);
    }
}
