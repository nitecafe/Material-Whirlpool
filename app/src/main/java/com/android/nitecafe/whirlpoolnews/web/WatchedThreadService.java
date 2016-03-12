package com.android.nitecafe.whirlpoolnews.web;

import android.util.Log;

import com.android.nitecafe.whirlpoolnews.models.Watched;
import com.android.nitecafe.whirlpoolnews.scheduler.ISchedulerManager;
import com.android.nitecafe.whirlpoolnews.utilities.WhirlpoolDateUtils;
import com.android.nitecafe.whirlpoolnews.web.interfaces.IWatchedThreadService;
import com.android.nitecafe.whirlpoolnews.web.interfaces.IWhirlpoolRestClient;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;

/**
 * For retrieving list of watched threads at the start of app to allow app
 * to create the right context menu depending if thread is
 * watched or not.
 */
@Singleton
public class WatchedThreadService implements IWatchedThreadService {

    private List<Integer> watchedThreads = new ArrayList<>();
    private IWhirlpoolRestClient whirlpoolRestClient;
    private ISchedulerManager schedulerManager;

    @Inject
    public WatchedThreadService(IWhirlpoolRestClient whirlpoolRestClient, ISchedulerManager schedulerManager) {
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
                    Log.e("WatchedThreadService", "failed to get watched threads");
                });
    }

    @Override
    public Observable<List<Watched>> getUnreadWatchedThreadsInInterval(long interval) {
        return whirlpoolRestClient.GetUnreadWatched()
                .map(watchedList -> watchedList.getWATCHED())
                .flatMap(watcheds -> Observable.from(watcheds))
                .filter(watched -> WhirlpoolDateUtils.isTimeWithinDuration(watched.getLASTDATE(), interval))
                .toList();
    }
}
