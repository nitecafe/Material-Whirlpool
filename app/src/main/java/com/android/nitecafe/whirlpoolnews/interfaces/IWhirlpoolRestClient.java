package com.android.nitecafe.whirlpoolnews.interfaces;

import com.android.nitecafe.whirlpoolnews.models.ForumList;
import com.android.nitecafe.whirlpoolnews.models.ForumThreadList;
import com.android.nitecafe.whirlpoolnews.models.NewsList;
import com.android.nitecafe.whirlpoolnews.models.RecentList;
import com.android.nitecafe.whirlpoolnews.models.ScrapedPostList;
import com.android.nitecafe.whirlpoolnews.models.ScrapedThreadList;
import com.android.nitecafe.whirlpoolnews.models.WatchedList;

import rx.Observable;

public interface IWhirlpoolRestClient {
    void setApiKey(String apikey);

    Boolean hasApiKeyBeenSet();

    Observable<NewsList> GetNews();

    Observable<ForumList> GetForum();

    Observable<RecentList> GetRecent();

    Observable<WatchedList> GetWatched();

    Observable<ForumThreadList> GetThreads(int forumIds, int threadCount);

    Observable<ScrapedThreadList> GetScrapedThreads(int forumIds, int pageCount, int groupId);

    Observable<ScrapedPostList> GetScrapedPosts(int threadId, String threadTitle, int page);
}
