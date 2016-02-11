package com.android.nitecafe.whirlpoolnews.controllers;

import com.android.nitecafe.whirlpoolnews.interfaces.IWhirlpoolRestClient;
import com.android.nitecafe.whirlpoolnews.scheduler.ISchedulerManager;
import com.android.nitecafe.whirlpoolnews.ui.interfaces.IRecentFragment;

import javax.inject.Inject;
import javax.inject.Singleton;

public class RecentController {

    private IWhirlpoolRestClient whirlpoolRestClient;
    private ISchedulerManager schedulerManager;
    private IRecentFragment recentFragment;

    @Inject
    @Singleton
    public RecentController(IWhirlpoolRestClient whirlpoolRestClient, ISchedulerManager schedulerManager) {
        this.whirlpoolRestClient = whirlpoolRestClient;
        this.schedulerManager = schedulerManager;
    }

    public void GetRecent() {
        whirlpoolRestClient.GetRecent()
                .observeOn(schedulerManager.GetMainScheduler())
                .subscribeOn(schedulerManager.GetIoScheduler())
                .subscribe(recentList -> {
                    if (recentFragment != null) {
                        recentFragment.DisplayRecent(recentList.getRECENT());
                        HideAllProgressBar();
                    }
                }, throwable -> {
                    if (recentFragment != null) {
                        recentFragment.DisplayErrorMessage();
                        HideAllProgressBar();
                    }
                });
    }

    public void WatchThread(int threadId) {
        whirlpoolRestClient.SetThreadAsWatch(threadId)
                .observeOn(schedulerManager.GetMainScheduler())
                .subscribeOn(schedulerManager.GetIoScheduler())
                .subscribe(aVoid -> {
                            recentFragment.ShowThreadWatchedSuccessfully();
                        },
                        throwable -> recentFragment.ShowThreadWatchedFailureMessage()
                );
    }

    private void HideAllProgressBar() {
        recentFragment.HideCenterProgressBar();
        recentFragment.HideRefreshLoader();
    }

    public void attach(IRecentFragment recentFragment) {
        this.recentFragment = recentFragment;
    }

}
