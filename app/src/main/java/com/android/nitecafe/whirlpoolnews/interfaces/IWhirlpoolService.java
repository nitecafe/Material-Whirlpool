package com.android.nitecafe.whirlpoolnews.interfaces;

import com.android.nitecafe.whirlpoolnews.models.ForumList;
import com.android.nitecafe.whirlpoolnews.models.NewsList;

import retrofit.http.GET;
import rx.Observable;

public interface IWhirlpoolService {

    @GET("?get=news") Observable<NewsList> GetNews();

    @GET("?get=forum") Observable<ForumList> GetForum();
}
