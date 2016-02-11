package com.android.nitecafe.whirlpoolnews.interfaces;

import com.android.nitecafe.whirlpoolnews.models.ForumList;
import com.android.nitecafe.whirlpoolnews.models.ForumThreadList;
import com.android.nitecafe.whirlpoolnews.models.NewsList;
import com.android.nitecafe.whirlpoolnews.models.RecentList;
import com.android.nitecafe.whirlpoolnews.models.WatchedList;

import retrofit.http.GET;
import retrofit.http.Query;
import rx.Observable;

public interface IWhirlpoolService {

    @GET("?get=news")
    Observable<NewsList> GetNews();

    @GET("?get=forum")
    Observable<ForumList> GetForum();

    @GET("?get=recent")
    Observable<RecentList> GetRecent();

    @GET("?get=watched") Observable<WatchedList> GetWatched(@Query("watchedmode") int watchedMode);

    @GET("?get=threads")
    Observable<ForumThreadList> GetThreads(@Query("forumIds") int forumId, @Query("threadcount") int threadCount);

    @GET("?get=watchedremove")
    Observable<Void> SetThreadAsUnwatched(@Query("watchedremove") int threadId);

    @GET("?get=watchedadd")
    Observable<Void> SetThreadAsWatched(@Query("watchedadd") int threadId);

    @GET("?get=watchedread")
    Observable<Void> SetThreadAsRead(@Query("watchedread") int threadId);
}
