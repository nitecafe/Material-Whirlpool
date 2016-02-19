package com.android.nitecafe.whirlpoolnews.web;

import com.android.nitecafe.whirlpoolnews.models.NewsList;

import rx.Observable;

public interface IWhirlpoolRestService {
    Observable<NewsList> GetNews();
}
