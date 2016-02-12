package com.android.nitecafe.whirlpoolnews.utilities;

import android.util.Log;

import com.android.nitecafe.whirlpoolnews.interfaces.IWhirlpoolRestClient;
import com.android.nitecafe.whirlpoolnews.scheduler.ISchedulerManager;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;

public class WatchedThreadIdentifier implements IWatchedThreadIdentifier {

    private List<Integer> watchedThreads = new ArrayList<>();
    private IWhirlpoolRestClient whirlpoolRestClient;
    private ISchedulerManager schedulerManager;

    @Singleton
    @Inject
    public WatchedThreadIdentifier(IWhirlpoolRestClient whirlpoolRestClient, ISchedulerManager schedulerManager) {
        this.whirlpoolRestClient = whirlpoolRestClient;
        this.schedulerManager = schedulerManager;
    }

    @Override public boolean isThreadWatched(int threadId) {
        return watchedThreads.contains(threadId);
    }

    @Override public void removeThreadFromWatch(int threadId) {
        if (watchedThreads.contains(threadId)) {
            watchedThreads.remove(watchedThreads.indexOf(threadId));
        } else
            throw new IllegalArgumentException("Do not have this as watched");
    }

    @Override public void addThreadToWatch(int threadId) {
        if (!watchedThreads.contains(threadId)) {
            watchedThreads.add(threadId);
        } else
            throw new IllegalArgumentException("Already been watched");
    }

    @Override public void setWatchedThreads(List<Integer> watchedThreads) {
        this.watchedThreads = watchedThreads;
    }

    @Override public void getWatchedThreads() {
        whirlpoolRestClient.GetAllWatched()
                .observeOn(schedulerManager.GetMainScheduler())
                .subscribeOn(schedulerManager.GetIoScheduler())
                .map(watchedList -> watchedList.getWATCHED())
                .flatMap(watcheds -> Observable.from(watcheds))
                .map(watched -> watched.getID())
                .subscribe(integer -> watchedThreads.add(integer), throwable -> {
                    Log.e("WatchedThreadIdentifier", "failed to get watched threads");
                });
    }
}
