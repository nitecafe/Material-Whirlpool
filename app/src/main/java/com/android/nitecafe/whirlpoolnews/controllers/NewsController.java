package com.android.nitecafe.whirlpoolnews.controllers;

import com.android.nitecafe.whirlpoolnews.interfaces.IWhirlpoolRestClient;
import com.android.nitecafe.whirlpoolnews.scheduler.ISchedulerManager;
import com.android.nitecafe.whirlpoolnews.ui.interfaces.INewsFragment;

import javax.inject.Inject;

public class NewsController {

    private IWhirlpoolRestClient whirlpoolRestClient;
    private ISchedulerManager schedulerManager;
    private INewsFragment newsFragment;

    @Inject
    public NewsController(IWhirlpoolRestClient _client, ISchedulerManager schedulerManager) {
        this.whirlpoolRestClient = _client;
        this.schedulerManager = schedulerManager;
    }

    public void GetNews() {
        whirlpoolRestClient.GetNews()
                .observeOn(schedulerManager.GetMainScheduler())
                .subscribeOn(schedulerManager.GetIoScheduler())
                .subscribe(newsList -> {
                    if (newsFragment != null) {
                        newsFragment.DisplayNews(newsList.getNEWS());
                        HideAllProgressBar();
                    }
                }, throwable -> {
                    if (newsFragment != null) {
                        newsFragment.DisplayErrorMessage();
                        HideAllProgressBar();
                    }
                });
    }

    private void HideAllProgressBar() {
        newsFragment.HideRefreshLoader();
        newsFragment.HideCenterProgressBar();
    }

    public void Attach(INewsFragment view) {
        newsFragment = view;
    }
}


