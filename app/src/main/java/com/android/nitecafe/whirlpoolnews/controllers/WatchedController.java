package com.android.nitecafe.whirlpoolnews.controllers;

import com.android.nitecafe.whirlpoolnews.interfaces.IWhirlpoolRestClient;
import com.android.nitecafe.whirlpoolnews.scheduler.ISchedulerManager;
import com.android.nitecafe.whirlpoolnews.ui.interfaces.IWatchedFragment;
import com.android.nitecafe.whirlpoolnews.utilities.IWatchedThreadIdentifier;

import javax.inject.Inject;

public class WatchedController extends ThreadBaseController<IWatchedFragment> {

    private IWhirlpoolRestClient whirlpoolRestClient;
    private ISchedulerManager schedulerManager;
    private IWatchedFragment watchedFragment;

    @Inject
    public WatchedController(IWhirlpoolRestClient whirlpoolRestClient,
                             ISchedulerManager schedulerManager,
                             IWatchedThreadIdentifier watchedThreadIdentifier) {
        super(whirlpoolRestClient, schedulerManager, watchedThreadIdentifier);
        this.whirlpoolRestClient = whirlpoolRestClient;
        this.schedulerManager = schedulerManager;
    }

    @Override
    public void Attach(IWatchedFragment baseFragment) {
        super.Attach(baseFragment);
        this.watchedFragment = baseFragment;
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

    @Override
    protected void onUnwatchThreadSuccess() {
        super.onUnwatchThreadSuccess();
        watchedFragment.loadWatched();
    }

    @Override
    protected void onMarkThreadAsReadSuccess() {
        super.onMarkThreadAsReadSuccess();
        watchedFragment.loadWatched();
    }

    private void HideAllProgressBar() {
        watchedFragment.HideCenterProgressBar();
        watchedFragment.HideRefreshLoader();
    }
}
