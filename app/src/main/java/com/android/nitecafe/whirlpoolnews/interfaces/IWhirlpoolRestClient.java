package com.android.nitecafe.whirlpoolnews.interfaces;

import com.android.nitecafe.whirlpoolnews.models.ForumList;
import com.android.nitecafe.whirlpoolnews.models.NewsList;
import com.android.nitecafe.whirlpoolnews.models.RecentList;

import rx.Observable;

public interface IWhirlpoolRestClient {
    void setApiKey(String apikey);

    Boolean hasApiKeyBeenSet();

    Observable<NewsList> GetNews();

    Observable<ForumList> GetForum();

    Observable<RecentList> GetRecent();
}
