package com.android.nitecafe.whirlpoolnews.dagger;

import com.android.nitecafe.whirlpoolnews.ui.NewsActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Graham-i5 on 1/30/2016.
 */
@Singleton
@Component(modules = {AppModule.class, DaggerModule.class})
public interface DaggerComponent {
    void inject(NewsActivity activity);
}
