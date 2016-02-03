package com.android.nitecafe.whirlpoolnews.web;

import android.content.SharedPreferences;

import com.android.nitecafe.whirlpoolnews.constants.StringConstants;
import com.android.nitecafe.whirlpoolnews.interfaces.IWhirlpoolRestClient;
import com.android.nitecafe.whirlpoolnews.interfaces.IWhirlpoolService;
import com.android.nitecafe.whirlpoolnews.models.ForumList;
import com.android.nitecafe.whirlpoolnews.models.NewsList;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.Request;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit.Retrofit;
import rx.Observable;

public class WhirlpoolRestClient implements IWhirlpoolRestClient {

    private IWhirlpoolService whirlpoolService;
    private Retrofit retrofit;
    private boolean hasApiKeyBeenSet;

    @Inject
    @Singleton
    public WhirlpoolRestClient(Retrofit retrofit, IWhirlpoolService whirlpoolService, SharedPreferences sharedPreferences) {
        this.retrofit = retrofit;
        this.whirlpoolService = whirlpoolService;

        String apiKey = sharedPreferences.getString(StringConstants.API_PREFERENCE_KEY, "");
        if (!apiKey.isEmpty()) {
            setApiKey(apiKey);
        }
    }

    @Override public void setApiKey(String apikey) {
        retrofit.client().interceptors().clear();
        retrofit.client().interceptors().add(chain -> {
            final HttpUrl build = chain.request().httpUrl().newBuilder()
                    .addQueryParameter("key", apikey)
                    .addQueryParameter("output", "json")
                    .build();

            final Request request = chain.request().newBuilder().url(build).build();
            return chain.proceed(request);
        });
        hasApiKeyBeenSet = true;
        whirlpoolService = retrofit.create(IWhirlpoolService.class);
    }

    @Override public Boolean hasApiKeyBeenSet() {
        return hasApiKeyBeenSet;
    }


    @Override
    public Observable<NewsList> GetNews() {
        return getWhirlpoolService().GetNews();
    }

    @Override public Observable<ForumList> GetForum() {
        return getWhirlpoolService().GetForum();
    }

    protected IWhirlpoolService getWhirlpoolService(){
        return whirlpoolService;
    }

}
