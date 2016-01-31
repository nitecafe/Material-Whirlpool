package com.android.nitecafe.whirlpoolnews.controllers;

import com.android.nitecafe.whirlpoolnews.interfaces.INewsActivity;
import com.android.nitecafe.whirlpoolnews.interfaces.IWhirlpoolRestClient;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Graham-i5 on 1/30/2016.
 */
public class NewsController {

    private IWhirlpoolRestClient _client;
    private INewsActivity mView;

    @Inject
    @Singleton
    public NewsController(IWhirlpoolRestClient _client) {
        this._client = _client;
    }

    public void GetNews() {
        _client.GetNews()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(newsList -> mView.DisplayNews(newsList.getNEWS()), throwable -> mView.DisplayErrorMessage());
    }

    public void Attach(INewsActivity view) {
        mView = view;
    }
}


