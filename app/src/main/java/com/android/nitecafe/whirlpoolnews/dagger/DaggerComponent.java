package com.android.nitecafe.whirlpoolnews.dagger;

import com.android.nitecafe.whirlpoolnews.ui.fragments.ForumFragment;
import com.android.nitecafe.whirlpoolnews.ui.fragments.LoginFragment;
import com.android.nitecafe.whirlpoolnews.ui.activities.MainActivity;
import com.android.nitecafe.whirlpoolnews.ui.fragments.NewsFragment;
import com.android.nitecafe.whirlpoolnews.ui.fragments.RecentFragment;
import com.android.nitecafe.whirlpoolnews.ui.fragments.ThreadFragment;
import com.android.nitecafe.whirlpoolnews.ui.fragments.WatchedFragment;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class, DaggerModule.class})
public interface DaggerComponent {
    void inject(NewsFragment activity);

    void inject(LoginFragment loginFragment);

    void inject(MainActivity mainActivity);

    void inject(ForumFragment forumFragment);

    void inject(RecentFragment recentFragment);

    void inject(WatchedFragment watchedFragment);

    void inject(ThreadFragment threadFragment);
}
