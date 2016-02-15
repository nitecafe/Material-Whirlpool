package com.android.nitecafe.whirlpoolnews.controllers;

import com.android.nitecafe.whirlpoolnews.interfaces.IWhirlpoolRestClient;
import com.android.nitecafe.whirlpoolnews.scheduler.ISchedulerManager;
import com.android.nitecafe.whirlpoolnews.ui.interfaces.IPopularFragment;
import com.android.nitecafe.whirlpoolnews.utilities.IWatchedThreadIdentifier;

import javax.inject.Inject;
import javax.inject.Singleton;

public class PopularThreadsController extends ThreadBaseController<IPopularFragment> {

    private IWhirlpoolRestClient whirlpoolRestClient;
    private ISchedulerManager schedulerManager;
    private IPopularFragment mView;

    @Inject
    @Singleton
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
                    if (mView != null) mView.DisplayPopularThreads(threadList);
                    HideAllProgressBar();
                }, throwable -> {
                    if (mView != null) mView.DisplayErrorMessage();
                    HideAllProgressBar();
                });
    }

    private void HideAllProgressBar() {
        if (mView == null)
            return;

        mView.HideCenterProgressBar();
    }

    public void Attach(IPopularFragment view) {
        mView = view;
    }
}


