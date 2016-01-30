package com.android.nitecafe.whirlpoolnews;

import com.android.nitecafe.whirlpoolnews.models.NewsList;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by Graham-i5 on 1/30/2016.
 */
public class NewsController {

    private WhirlpoolRestClient _client;

    @Inject
    public NewsController(WhirlpoolRestClient _client) {
        this._client = _client;
    }

    public Observable<NewsList> GetNews() {
        return _client.GetNews();
    }
}
