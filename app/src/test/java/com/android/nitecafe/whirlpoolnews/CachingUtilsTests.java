package com.android.nitecafe.whirlpoolnews;


import android.content.SharedPreferences;

import com.android.nitecafe.whirlpoolnews.models.ForumList;
import com.android.nitecafe.whirlpoolnews.models.NewsList;
import com.android.nitecafe.whirlpoolnews.models.RecentList;
import com.android.nitecafe.whirlpoolnews.models.ScrapedThread;
import com.android.nitecafe.whirlpoolnews.models.WatchedList;
import com.android.nitecafe.whirlpoolnews.models.WhimsList;
import com.android.nitecafe.whirlpoolnews.utilities.CachingUtils;
import com.google.gson.Gson;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CachingUtilsTests {

    private final String NEWS_CACHE_KEY = "news_cache";
    private final String POPULAR_CACHE_KEY = "popular_cache";
    private final String FORUM_CACHE_KEY = "forum_cache";
    private final String RECENT_CACHE_KEY = "recent_cache";
    private final String UNREAD_WATCHED_CACHE_KEY = "unread_watched_cache";
    private final String ALL_WATCHED_CACHE_KEY = "all_watched_cache";
    private final String WHIMS_CACHE_KEY = "whims_cache";


    @Mock SharedPreferences sharedPreferencesMock;
    @Mock SharedPreferences.Editor editorMock;
    private CachingUtils _cachingUtil;
    private Gson gson;

    @Before
    public void Setup() {
        when(sharedPreferencesMock.edit()).thenReturn(editorMock);
        _cachingUtil = new CachingUtils(sharedPreferencesMock);
        gson = new Gson();
    }

    @Test
    public void CacheNews_WhenCalled_StoreInPreference() {

        //arrange
        NewsList newsList = new NewsList();

        //act
        _cachingUtil.cacheNews(newsList);

        //assert
        verify(editorMock).putString(NEWS_CACHE_KEY, gson.toJson(newsList));
    }

    @Test
    public void GetNews_WhenCalled_GetFromPreference() {

        //arrange
        when(sharedPreferencesMock.getString(NEWS_CACHE_KEY, "")).thenReturn("");

        //act
        _cachingUtil.getNewsCache();

        //assert
        verify(sharedPreferencesMock).getString(NEWS_CACHE_KEY, "");
    }

    @Test
    public void GetForum_WhenCalled_GetFromPreference() {

        //arrange
        when(sharedPreferencesMock.getString(FORUM_CACHE_KEY, "")).thenReturn("");

        //act
        _cachingUtil.getForumCache();

        //assert
        verify(sharedPreferencesMock).getString(FORUM_CACHE_KEY, "");
    }

    @Test
    public void CacheForum_WhenCalled_StoreInPreference() {

        //arrange
        ForumList forumList = new ForumList();

        //act
        _cachingUtil.cacheForum(forumList);

        //assert
        verify(editorMock).putString(FORUM_CACHE_KEY, gson.toJson(forumList));
    }

    @Test
    public void GetPopularThreads_WhenCalled_GetFromPreference() {

        //arrange
        when(sharedPreferencesMock.getString(POPULAR_CACHE_KEY, "")).thenReturn("");

        //act
        _cachingUtil.getPopularThreadsCache();

        //assert
        verify(sharedPreferencesMock).getString(POPULAR_CACHE_KEY, "");
    }

    @Test
    public void CachePopularThreads_WhenCalled_StoreInPreference() {

        //arrange
        ArrayList<ScrapedThread> scrapedThreadList = new ArrayList<>();

        //act
        _cachingUtil.cachePopularThreads(scrapedThreadList);

        //assert
        verify(editorMock).putString(POPULAR_CACHE_KEY, gson.toJson(scrapedThreadList));
    }

    @Test
    public void GetRecentThreads_WhenCalled_GetFromPreference() {

        //arrange
        when(sharedPreferencesMock.getString(RECENT_CACHE_KEY, "")).thenReturn("");

        //act
        _cachingUtil.getRecentThreadsCache();

        //assert
        verify(sharedPreferencesMock).getString(RECENT_CACHE_KEY, "");
    }

    @Test
    public void CacheRecentThreads_WhenCalled_StoreInPreference() {

        //arrange
        RecentList recentList = new RecentList();

        //act
        _cachingUtil.cacheRecentThreads(recentList);

        //assert
        verify(editorMock).putString(RECENT_CACHE_KEY, gson.toJson(recentList));
    }

    @Test
    public void GetUnreadWatchedThreads_WhenCalled_GetFromPreference() {

        //arrange
        when(sharedPreferencesMock.getString(UNREAD_WATCHED_CACHE_KEY, "")).thenReturn("");

        //act
        _cachingUtil.getUnreadWatchedThreadsCache();

        //assert
        verify(sharedPreferencesMock).getString(UNREAD_WATCHED_CACHE_KEY, "");
    }

    @Test
    public void CacheUnreadWatchedThreads_WhenCalled_StoreInPreference() {

        //arrange
        WatchedList watchedList = new WatchedList();

        //act
        _cachingUtil.cacheUnreadWatchedThreads(watchedList);

        //assert
        verify(editorMock).putString(UNREAD_WATCHED_CACHE_KEY, gson.toJson(watchedList));
    }

    @Test
    public void GetAllWatchedThreads_WhenCalled_GetFromPreference() {

        //arrange
        when(sharedPreferencesMock.getString(ALL_WATCHED_CACHE_KEY, "")).thenReturn("");

        //act
        _cachingUtil.getAllWatchedThreadsCache();

        //assert
        verify(sharedPreferencesMock).getString(ALL_WATCHED_CACHE_KEY, "");
    }

    @Test
    public void CacheAllWatchedThreads_WhenCalled_StoreInPreference() {

        //arrange
        WatchedList watchedList = new WatchedList();

        //act
        _cachingUtil.cacheAllWatchedThreads(watchedList);

        //assert
        verify(editorMock).putString(ALL_WATCHED_CACHE_KEY, gson.toJson(watchedList));
    }

    @Test
    public void GetWhims_WhenCalled_GetFromPreference() {

        //arrange
        when(sharedPreferencesMock.getString(WHIMS_CACHE_KEY, "")).thenReturn("");

        //act
        _cachingUtil.getWhimsCache();

        //assert
        verify(sharedPreferencesMock).getString(WHIMS_CACHE_KEY, "");
    }

    @Test
    public void CacheWhim_WhenCalled_StoreInPreference() {

        //arrange
        WhimsList whimsList = new WhimsList();

        //act
        _cachingUtil.cacheWhims(whimsList);

        //assert
        verify(editorMock).putString(WHIMS_CACHE_KEY, gson.toJson(whimsList));
    }
}
