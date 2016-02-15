package com.android.nitecafe.whirlpoolnews.utilities;

import com.android.nitecafe.whirlpoolnews.models.ScrapedPostList;
import com.android.nitecafe.whirlpoolnews.models.ScrapedThread;
import com.android.nitecafe.whirlpoolnews.models.ScrapedThreadList;

import java.util.ArrayList;

import rx.Observable;

public interface IThreadScraper {
    Observable<ScrapedThreadList> scrapeThreadsFromForumObservable(int forum_id, int page_number, int group_id);

    Observable<ScrapedPostList> scrapePostsFromThreadObservable(int threadId, int page);

    Observable<ArrayList<ScrapedThread>> ScrapPopularThreadsObservable();
}
