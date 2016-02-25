package com.android.nitecafe.whirlpoolnews.utilities.interfaces;

import com.android.nitecafe.whirlpoolnews.models.ForumList;
import com.android.nitecafe.whirlpoolnews.models.NewsList;
import com.android.nitecafe.whirlpoolnews.models.RecentList;
import com.android.nitecafe.whirlpoolnews.models.ScrapedThread;
import com.android.nitecafe.whirlpoolnews.models.WatchedList;
import com.android.nitecafe.whirlpoolnews.models.WhimsList;

import java.util.ArrayList;

public interface ICachingUtils {
    void cacheNews(NewsList newsList);

    NewsList getNewsCache();

    ForumList getForumCache();

    void cacheForum(ForumList forumList);

    ArrayList<ScrapedThread> getPopularThreadsCache();

    void cachePopularThreads(ArrayList<ScrapedThread> scrapedThreadList);

    RecentList getRecentThreadsCache();

    void cacheRecentThreads(RecentList recentList);

    WatchedList getUnreadWatchedThreadsCache();

    void cacheUnreadWatchedThreads(WatchedList watchedList);

    WatchedList getAllWatchedThreadsCache();

    void cacheAllWatchedThreads(WatchedList watchedList);

    WhimsList getWhimsCache();

    void cacheWhims(WhimsList whimsList);
}
