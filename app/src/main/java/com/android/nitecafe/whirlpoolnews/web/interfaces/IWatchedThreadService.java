package com.android.nitecafe.whirlpoolnews.web.interfaces;

import java.util.List;

public interface IWatchedThreadService {
    boolean isThreadWatched(int threadId);

    void removeThreadFromWatch(int threadId);

    void addThreadToWatch(int threadId);

    void setWatchedThreads(List<Integer> watchedThreads);

    void getWatchedThreads();
}
