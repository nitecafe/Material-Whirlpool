package com.android.nitecafe.whirlpoolnews.controllers;

import com.android.nitecafe.whirlpoolnews.interfaces.IWhirlpoolRestClient;
import com.android.nitecafe.whirlpoolnews.scheduler.ISchedulerManager;
import com.android.nitecafe.whirlpoolnews.ui.interfaces.IWatchedFragment;
import com.android.nitecafe.whirlpoolnews.utilities.IWatchedThreadIdentifier;

import javax.inject.Inject;
import javax.inject.Singleton;

public class WatchedController {

    private IWhirlpoolRestClient whirlpoolRestClient;
    private ISchedulerManager schedulerManager;
    private IWatchedThreadIdentifier mWatchedThreadIdentifier;
    private IWatchedFragment watchedFragment;

    @Inject
    @Singleton
    public WatchedController(IWhirlpoolRestClient whirlpoolRestClient, ISchedulerManager schedulerManager, IWatchedThreadIdentifier watchedThreadIdentifier) {
        this.whirlpoolRestClient = whirlpoolRestClient;
        this.schedulerManager = schedulerManager;
        mWatchedThreadIdentifier = watchedThreadIdentifier;
    }

    public void GetUnreadWatched() {
        whirlpoolRestClient.GetUnreadWatched()
                .observeOn(schedulerManager.GetMainScheduler())
                .subscribeOn(schedulerManager.GetIoScheduler())
                .subscribe(watchedList -> {
                    if (watchedFragment != null) {
                        watchedFragment.DisplayWatched(watchedList.getWATCHED());
                        HideAllProgressBar();
                    }
                }, throwable -> {
                    if (watchedFragment != null) {
                        watchedFragment.DisplayErrorMessage();
                        HideAllProgressBar();
                    }
                });
    }

    public void GetAllWatched() {
        whirlpoolRestClient.GetAllWatched()
                .observeOn(schedulerManager.GetMainScheduler())
                .subscribeOn(schedulerManager.GetIoScheduler())
                .subscribe(watchedList -> {
                    if (watchedFragment != null) {
                        watchedFragment.DisplayWatched(watchedList.getWATCHED());
                        HideAllProgressBar();
                    }
                }, throwable -> {
                    if (watchedFragment != null) {
                        watchedFragment.DisplayErrorMessage();
                        HideAllProgressBar();
                    }
                });
    }

    public void UnwatchThread(int threadId) {
        whirlpoolRestClient.SetThreadAsUnwatch(threadId)
                .observeOn(schedulerManager.GetMainScheduler())
                .subscribeOn(schedulerManager.GetIoScheduler())
                .subscribe(aVoid -> {
                            watchedFragment.ShowActionSuccessMessage();
                            watchedFragment.loadWatched();
                            mWatchedThreadIdentifier.removeThreadFromWatch(threadId);
                        },
                        throwable -> watchedFragment.ShowActionFailedMessage()
                );
    }

    private void HideAllProgressBar() {
        watchedFragment.HideCenterProgressBar();
        watchedFragment.HideRefreshLoader();
    }

    public void attach(IWatchedFragment watchedFragment) {
        this.watchedFragment = watchedFragment;
    }

    public void MarkThreadAsRead(int threadId) {
        whirlpoolRestClient.MarkThreadAsRead(threadId)
                .observeOn(schedulerManager.GetMainScheduler())
                .subscribeOn(schedulerManager.GetIoScheduler())
                .subscribe(aVoid -> {
                            watchedFragment.ShowActionSuccessMessage();
                            watchedFragment.loadWatched();
                        },
                        throwable -> watchedFragment.ShowActionFailedMessage()
                );
    }
}
