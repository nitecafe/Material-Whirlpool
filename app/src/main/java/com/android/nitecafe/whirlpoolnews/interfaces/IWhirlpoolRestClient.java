package com.android.nitecafe.whirlpoolnews.interfaces;

import com.android.nitecafe.whirlpoolnews.models.NewsList;

import rx.Observable;

/**
 * Created by Graham-i5 on 1/31/2016.
 */
public interface IWhirlpoolRestClient {
    Observable<NewsList> GetNews();
}
