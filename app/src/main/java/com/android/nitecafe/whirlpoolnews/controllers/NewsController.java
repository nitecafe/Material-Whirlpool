package com.android.nitecafe.whirlpoolnews.controllers;

import com.android.nitecafe.whirlpoolnews.interfaces.INewsFragment;
import com.android.nitecafe.whirlpoolnews.interfaces.IWhirlpoolRestClient;
import com.android.nitecafe.whirlpoolnews.scheduler.ISchedulerManager;

import javax.inject.Inject;
import javax.inject.Singleton;

public class NewsController {

    private IWhirlpoolRestClient _client;
    private ISchedulerManager schedulerManager;
    private INewsFragment mView;

    @Inject
    @Singleton
    public NewsController(IWhirlpoolRestClient _client, ISchedulerManager schedulerManager) {
        this._client = _client;
        this.schedulerManager = schedulerManager;
    }

    public void GetNews() {
        _client.GetNews()
                .observeOn(schedulerManager.GetMainScheduler())
                .subscribeOn(schedulerManager.GetIoScheduler())
                .subscribe(newsList -> {
                    if (mView != null) mView.DisplayNews(newsList.getNEWS());
                    HideAllProgressBar();
                }, throwable -> {
                    if (mView != null) mView.DisplayErrorMessage();
                    HideAllProgressBar();
                });
    }

    private void HideAllProgressBar() {
        if (mView == null)
            return;

        mView.HideRefreshLoader();
        mView.HideCenterProgressBar();
    }

    public void Attach(INewsFragment view) {
        mView = view;
    }
}


