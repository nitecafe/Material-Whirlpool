package com.android.nitecafe.whirlpoolnews.controllers;

import com.android.nitecafe.whirlpoolnews.interfaces.IWhirlpoolRestClient;
import com.android.nitecafe.whirlpoolnews.scheduler.ISchedulerManager;
import com.android.nitecafe.whirlpoolnews.ui.interfaces.IWatchedFragment;

import javax.inject.Inject;
import javax.inject.Singleton;

public class WatchedController {

    private IWhirlpoolRestClient whirlpoolRestClient;
    private ISchedulerManager schedulerManager;
    private IWatchedFragment watchedFragment;

    @Inject
    @Singleton
    public WatchedController(IWhirlpoolRestClient whirlpoolRestClient, ISchedulerManager schedulerManager) {
        this.whirlpoolRestClient = whirlpoolRestClient;
        this.schedulerManager = schedulerManager;
    }

    public void GetWatched() {
        whirlpoolRestClient.GetWatched()
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

    private void HideAllProgressBar() {
        watchedFragment.HideCenterProgressBar();
        watchedFragment.HideRefreshLoader();
    }

    public void attach(IWatchedFragment watchedFragment) {
        this.watchedFragment = watchedFragment;
    }

}
