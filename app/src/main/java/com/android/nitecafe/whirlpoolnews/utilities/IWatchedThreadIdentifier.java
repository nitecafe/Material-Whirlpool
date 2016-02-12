package com.android.nitecafe.whirlpoolnews.utilities;

import java.util.List;

public interface IWatchedThreadIdentifier {
    boolean isThreadWatched(int threadId);

    void removeThreadFromWatch(int threadId);

    void addThreadToWatch(int threadId);

    void setWatchedThreads(List<Integer> watchedThreads);

    void getWatchedThreads();
}
