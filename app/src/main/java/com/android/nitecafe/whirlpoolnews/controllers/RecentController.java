package com.android.nitecafe.whirlpoolnews.controllers;

import com.android.nitecafe.whirlpoolnews.interfaces.IWhirlpoolRestClient;
import com.android.nitecafe.whirlpoolnews.scheduler.ISchedulerManager;
import com.android.nitecafe.whirlpoolnews.ui.interfaces.IRecentFragment;
import com.android.nitecafe.whirlpoolnews.utilities.IWatchedThreadIdentifier;

import javax.inject.Inject;
import javax.inject.Singleton;

public class RecentController extends ThreadBaseController<IRecentFragment> {

    private IWhirlpoolRestClient whirlpoolRestClient;
    private ISchedulerManager schedulerManager;
    private IRecentFragment recentFragment;

    @Inject
    @Singleton
    public RecentController(IWhirlpoolRestClient whirlpoolRestClient,
                            ISchedulerManager schedulerManager,
                            IWatchedThreadIdentifier watchedThreadIdentifier) {
        super(whirlpoolRestClient, schedulerManager, watchedThreadIdentifier);
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

    private void HideAllProgressBar() {
        recentFragment.HideCenterProgressBar();
        recentFragment.HideRefreshLoader();
    }

    @Override
    public void Attach(IRecentFragment recentFragment) {
        super.Attach(recentFragment);
        this.recentFragment = recentFragment;
    }

}
