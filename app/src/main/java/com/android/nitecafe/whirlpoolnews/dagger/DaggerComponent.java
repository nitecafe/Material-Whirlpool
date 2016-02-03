package com.android.nitecafe.whirlpoolnews.dagger;

import com.android.nitecafe.whirlpoolnews.ui.ForumFragment;
import com.android.nitecafe.whirlpoolnews.ui.LoginFragment;
import com.android.nitecafe.whirlpoolnews.ui.MainActivity;
import com.android.nitecafe.whirlpoolnews.ui.NewsFragment;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class, DaggerModule.class})
public interface DaggerComponent {
    void inject(NewsFragment activity);

    void inject(LoginFragment loginFragment);

    void inject(MainActivity mainActivity);

    void inject(ForumFragment forumFragment);
}
