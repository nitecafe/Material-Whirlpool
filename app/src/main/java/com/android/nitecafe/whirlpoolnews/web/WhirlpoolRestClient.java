package com.android.nitecafe.whirlpoolnews.web;

import com.android.nitecafe.whirlpoolnews.interfaces.IWhirlpoolRestClient;
import com.android.nitecafe.whirlpoolnews.interfaces.IWhirlpoolService;
import com.android.nitecafe.whirlpoolnews.models.NewsList;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;

/**
 * Created by Graham-i5 on 1/30/2016.
 */
public class WhirlpoolRestClient implements IWhirlpoolRestClient {

    private final IWhirlpoolService whirlpoolService;

    @Inject
    @Singleton
    public WhirlpoolRestClient(IWhirlpoolService whirlpoolService) {
        this.whirlpoolService = whirlpoolService;
    }

    @Override
    public Observable<NewsList> GetNews() {
        return whirlpoolService.GetNews();
    }
}
