package com.android.nitecafe.whirlpoolnews.web.interfaces;

import com.android.nitecafe.whirlpoolnews.models.Watched;

import java.util.List;

import rx.Observable;

public interface IWatchedThreadService {
    boolean isThreadWatched(int threadId);

    void removeThreadFromWatch(int threadId);

    void addThreadToWatch(int threadId);

    void setWatchedThreads(List<Integer> watchedThreads);

    void getWatchedThreads();

    Observable<List<Watched>> getUnreadWatchedThreadsInInterval(long interval);
}
