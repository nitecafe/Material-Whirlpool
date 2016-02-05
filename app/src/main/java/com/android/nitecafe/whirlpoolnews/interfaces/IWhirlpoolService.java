package com.android.nitecafe.whirlpoolnews.interfaces;

import com.android.nitecafe.whirlpoolnews.models.ForumList;
import com.android.nitecafe.whirlpoolnews.models.NewsList;
import com.android.nitecafe.whirlpoolnews.models.Recent;
import com.android.nitecafe.whirlpoolnews.models.RecentList;
import com.android.nitecafe.whirlpoolnews.models.WatchedList;

import retrofit.http.GET;
import rx.Observable;

public interface IWhirlpoolService {

    @GET("?get=news") Observable<NewsList> GetNews();

    @GET("?get=forum") Observable<ForumList> GetForum();

    @GET("?get=recent") Observable<RecentList> GetRecent();

    @GET("?get=watched&watchedmode=0") Observable<WatchedList> GetWatched();
}
