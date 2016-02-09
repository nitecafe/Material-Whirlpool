package com.android.nitecafe.whirlpoolnews.utilities;

import com.android.nitecafe.whirlpoolnews.models.ScrapedPostList;
import com.android.nitecafe.whirlpoolnews.models.ScrapedThreadList;

import rx.Observable;

public interface IThreadScraper {
    Observable<ScrapedThreadList> scrapeThreadsFromForumObservable(int forum_id, int page_number, int group_id);

    Observable<ScrapedPostList> scrapePostsFromThreadObservable(int threadId, String threadTitle, int page);
}
