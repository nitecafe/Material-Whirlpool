package com.android.nitecafe.whirlpoolnews.dagger;

import com.android.nitecafe.whirlpoolnews.BackgroundServices.WatchedThreads.WatchedThreadsIntentService;
import com.android.nitecafe.whirlpoolnews.BackgroundServices.Whims.WhimsIntentService;
import com.android.nitecafe.whirlpoolnews.ui.activities.MainActivity;
import com.android.nitecafe.whirlpoolnews.ui.fragments.ForumFragment;
import com.android.nitecafe.whirlpoolnews.ui.fragments.LoginFragment;
import com.android.nitecafe.whirlpoolnews.ui.fragments.NewsFragment;
import com.android.nitecafe.whirlpoolnews.ui.fragments.PopularThreadFragment;
import com.android.nitecafe.whirlpoolnews.ui.fragments.PostBookmarkFragment;
import com.android.nitecafe.whirlpoolnews.ui.fragments.RecentFragment;
import com.android.nitecafe.whirlpoolnews.ui.fragments.ScrapedPostChildFragment;
import com.android.nitecafe.whirlpoolnews.ui.fragments.ScrapedPostParentFragment;
import com.android.nitecafe.whirlpoolnews.ui.fragments.ScrapedThreadFragment;
import com.android.nitecafe.whirlpoolnews.ui.fragments.SearchFragment;
import com.android.nitecafe.whirlpoolnews.ui.fragments.SearchResultThreadFragment;
import com.android.nitecafe.whirlpoolnews.ui.fragments.ThreadFragment;
import com.android.nitecafe.whirlpoolnews.ui.fragments.WatchedChildFragment;
import com.android.nitecafe.whirlpoolnews.ui.fragments.WatchedFragment;
import com.android.nitecafe.whirlpoolnews.ui.fragments.WhimsFragment;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class, DaggerModule.class, SubjectModule.class})
public interface DaggerComponent {
    void inject(NewsFragment activity);

    void inject(LoginFragment loginFragment);

    void inject(MainActivity mainActivity);

    void inject(ForumFragment forumFragment);

    void inject(RecentFragment recentFragment);

    void inject(WatchedFragment watchedFragment);

    void inject(ThreadFragment threadFragment);

    void inject(ScrapedThreadFragment scrapedThreadFragment);

    void inject(WatchedChildFragment watchedChildFragment);

    void inject(WhimsFragment whimsFragment);

    void inject(PopularThreadFragment popularThreadFragment);

    void inject(SearchFragment searchFragment);

    void inject(SearchResultThreadFragment searchResultThreadFragment);

    void inject(ScrapedPostParentFragment scrapedPostParentFragment);

    void inject(ScrapedPostChildFragment scrapedPostChildFragment);

    void inject(PostBookmarkFragment postBookmarkFragment);

    void inject(WatchedThreadsIntentService watchedThreadsIntentService);

    void inject(WhimsIntentService whimsIntentService);
}
