package com.android.nitecafe.whirlpoolnews.interfaces;

import com.android.nitecafe.whirlpoolnews.models.ForumList;
import com.android.nitecafe.whirlpoolnews.models.ForumThreadList;
import com.android.nitecafe.whirlpoolnews.models.NewsList;
import com.android.nitecafe.whirlpoolnews.models.RecentList;
import com.android.nitecafe.whirlpoolnews.models.ScrapedPostList;
import com.android.nitecafe.whirlpoolnews.models.ScrapedThreadList;
import com.android.nitecafe.whirlpoolnews.models.WatchedList;
import com.android.nitecafe.whirlpoolnews.models.WhimsList;

import rx.Observable;

public interface IWhirlpoolRestClient {
    void setApiKey(String apikey);

    Boolean hasApiKeyBeenSet();

    Observable<NewsList> GetNews();

    Observable<ForumList> GetForum();

    Observable<RecentList> GetRecent();

    Observable<WatchedList> GetUnreadWatched();

    Observable<WatchedList> GetAllWatched();

    Observable<ForumThreadList> GetThreads(int forumIds, int threadCount);

    Observable<ScrapedThreadList> GetScrapedThreads(int forumIds, int pageCount, int groupId);

    Observable<ScrapedPostList> GetScrapedPosts(int threadId, int page);

    Observable<Void> SetThreadAsWatch(int threadId);

    Observable<Void> SetThreadAsUnwatch(int threadId);

    Observable<Void> MarkThreadAsRead(int threadId);

    Observable<WhimsList> GetWhims();
}
