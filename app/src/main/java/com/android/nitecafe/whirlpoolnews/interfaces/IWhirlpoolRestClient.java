package com.android.nitecafe.whirlpoolnews.interfaces;

import com.android.nitecafe.whirlpoolnews.models.ForumList;
import com.android.nitecafe.whirlpoolnews.models.ForumThreadList;
import com.android.nitecafe.whirlpoolnews.models.NewsList;
import com.android.nitecafe.whirlpoolnews.models.RecentList;
import com.android.nitecafe.whirlpoolnews.models.ScrapedThread;
import com.android.nitecafe.whirlpoolnews.models.WatchedList;

import java.util.List;

import rx.Observable;

public interface IWhirlpoolRestClient {
    void setApiKey(String apikey);

    Boolean hasApiKeyBeenSet();

    Observable<NewsList> GetNews();

    Observable<ForumList> GetForum();

    Observable<RecentList> GetRecent();

    Observable<WatchedList> GetWatched();

    Observable<ForumThreadList> GetThreads(int forumIds, int threadCount);

    Observable<List<ScrapedThread>> GetScrapedThreads(int forumIds, int pageCount, int groupId);
}
