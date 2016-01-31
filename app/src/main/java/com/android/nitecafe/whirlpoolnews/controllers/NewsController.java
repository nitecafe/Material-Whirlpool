package com.android.nitecafe.whirlpoolnews.controllers;

import com.android.nitecafe.whirlpoolnews.interfaces.INewsActivity;
import com.android.nitecafe.whirlpoolnews.interfaces.IWhirlpoolRestClient;
import com.android.nitecafe.whirlpoolnews.scheduler.ISchedulerManager;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Graham-i5 on 1/30/2016.
 */
public class NewsController {

    private IWhirlpoolRestClient _client;
    private ISchedulerManager schedulerManager;
    private INewsActivity mView;

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
                    mView.DisplayNews(newsList.getNEWS());
                    HideAllProgressBar();
                }, throwable -> {
                    mView.DisplayErrorMessage();
                    HideAllProgressBar();
                });
    }

    private void HideAllProgressBar() {
        mView.HideRefreshLoader();
        mView.HideCenterProgressBar();
    }

    public void Attach(INewsActivity view) {
        mView = view;
    }
}


