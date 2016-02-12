package com.android.nitecafe.whirlpoolnews.utilities;

import java.util.List;

public interface IWatchedThreadIdentifier {
    boolean isThreadWatched(int threadId);

    void setWatchedThreads(List<Integer> watchedThreads);

    void getWatchedThreads();
}
