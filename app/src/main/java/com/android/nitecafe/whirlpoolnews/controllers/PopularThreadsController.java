package com.android.nitecafe.whirlpoolnews.controllers;

import com.android.nitecafe.whirlpoolnews.interfaces.IWhirlpoolRestClient;
import com.android.nitecafe.whirlpoolnews.scheduler.ISchedulerManager;
import com.android.nitecafe.whirlpoolnews.ui.interfaces.IPopularFragment;
import com.android.nitecafe.whirlpoolnews.utilities.IWatchedThreadIdentifier;

import javax.inject.Inject;

public class PopularThreadsController extends ThreadBaseController<IPopularFragment> {

    private IWhirlpoolRestClient whirlpoolRestClient;
    private ISchedulerManager schedulerManager;
    private IPopularFragment popularFragment;

    @Inject
    public PopularThreadsController(IWhirlpoolRestClient whirlpoolRestClient, ISchedulerManager schedulerManager, IWatchedThreadIdentifier watchedThreadIdentifier) {
        super(whirlpoolRestClient, schedulerManager, watchedThreadIdentifier);
        this.whirlpoolRestClient = whirlpoolRestClient;
        this.schedulerManager = schedulerManager;
    }

    public void GetPopularThreads() {
        whirlpoolRestClient.GetPopularThreads()
                .observeOn(schedulerManager.GetMainScheduler())
                .subscribeOn(schedulerManager.GetIoScheduler())
                .subscribe(threadList -> {
                    if (popularFragment != null) popularFragment.DisplayPopularThreads(threadList);
                    HideAllProgressBar();
                }, throwable -> {
                    if (popularFragment != null) popularFragment.DisplayErrorMessage();
                    HideAllProgressBar();
                });
    }

    private void HideAllProgressBar() {
        if (popularFragment == null)
            return;

        popularFragment.HideCenterProgressBar();
    }

    public void Attach(IPopularFragment view) {
        popularFragment = view;
    }
}


