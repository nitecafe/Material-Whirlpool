package com.android.nitecafe.whirlpoolnews.controllers;

import com.android.nitecafe.whirlpoolnews.ui.interfaces.INewsFragment;
import com.android.nitecafe.whirlpoolnews.web.interfaces.IWhirlpoolRestService;

import javax.inject.Inject;

public class NewsController {

    private IWhirlpoolRestService whirlpoolRestService;
    private INewsFragment newsFragment;

    @Inject
    public NewsController(IWhirlpoolRestService whirlpoolRestService) {
        this.whirlpoolRestService = whirlpoolRestService;
    }

    public void GetNews() {
        whirlpoolRestService.GetNews()
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


