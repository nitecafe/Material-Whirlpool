package com.android.nitecafe.whirlpoolnews;

import android.app.Application;

import com.android.nitecafe.whirlpoolnews.constants.StringConstants;
import com.android.nitecafe.whirlpoolnews.dagger.AppModule;
import com.android.nitecafe.whirlpoolnews.dagger.DaggerComponent;
import com.android.nitecafe.whirlpoolnews.dagger.DaggerDaggerComponent;
import com.android.nitecafe.whirlpoolnews.dagger.DaggerModule;
import com.squareup.leakcanary.LeakCanary;

public class WhirlpoolApp extends Application {


    private DaggerComponent daggerComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        daggerComponent = DaggerDaggerComponent.builder()
                .appModule(new AppModule(this))
                .daggerModule(new DaggerModule(StringConstants.WHIRLPOOL_API_URL))
                .build();

        if (BuildConfig.DEBUG)
            LeakCanary.install(this);  //not working on android 6.0
    }

    public DaggerComponent getDaggerComponent() {
        return daggerComponent;
    }
}
