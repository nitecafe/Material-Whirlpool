package com.android.nitecafe.whirlpoolnews;

import com.android.nitecafe.whirlpoolnews.models.NewsList;

import retrofit.http.GET;
import rx.Observable;

/**
 * Created by Graham-i5 on 1/30/2016.
 */
public interface IWhirlpoolService {

    @GET("?get=news")
    Observable<NewsList> GetNews();
}
