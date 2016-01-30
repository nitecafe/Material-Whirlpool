package com.android.nitecafe.whirlpoolnews;

import com.android.nitecafe.whirlpoolnews.models.News;
import com.android.nitecafe.whirlpoolnews.models.NewsList;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;

/**
 * Created by Graham-i5 on 1/30/2016.
 */
public class WhirlpoolRestClient {

    private final IWhirlpoolService whirlpoolService;

    @Inject
    @Singleton
    public WhirlpoolRestClient(IWhirlpoolService whirlpoolService) {
        this.whirlpoolService = whirlpoolService;
    }

    public Observable<NewsList> GetNews() {
        return whirlpoolService.GetNews();
    }
}
