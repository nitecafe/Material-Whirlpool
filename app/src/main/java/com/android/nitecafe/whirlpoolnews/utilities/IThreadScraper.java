package com.android.nitecafe.whirlpoolnews.utilities;

import com.android.nitecafe.whirlpoolnews.models.ScrapedThread;

import java.util.List;

import rx.Observable;

public interface IThreadScraper {
    Observable<List<ScrapedThread>> scrapeThreadsFromForumObservable(int forum_id, int page_number, int group_id);
}
