package com.android.nitecafe.whirlpoolnews.ui.activities;

import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.android.nitecafe.whirlpoolnews.R;
import com.android.nitecafe.whirlpoolnews.ui.FragmentsEnum;
import com.android.nitecafe.whirlpoolnews.ui.fragments.ForumFragment;
import com.android.nitecafe.whirlpoolnews.ui.fragments.LoginFragment;
import com.android.nitecafe.whirlpoolnews.ui.fragments.NewsFragment;
import com.android.nitecafe.whirlpoolnews.ui.fragments.PopularThreadFragment;
import com.android.nitecafe.whirlpoolnews.ui.fragments.RecentFragment;
import com.android.nitecafe.whirlpoolnews.ui.fragments.SearchFragment;
import com.android.nitecafe.whirlpoolnews.ui.fragments.WatchedFragment;
import com.android.nitecafe.whirlpoolnews.ui.fragments.WhimsFragment;
import com.mikepenz.aboutlibraries.LibsBuilder;
import com.mikepenz.aboutlibraries.ui.LibsSupportFragment;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

public class NavigationDrawerActivity extends AppCompatActivity implements Drawer.OnDrawerItemClickListener {

    public final int NEWS_POSITION = 1;
    public final int FORUM_POSITION = 2;
    public final int POPULAR_POSITION = 4;
    public final int RECENT_POSITION = 5;
    public final int WATCHED_POSITION = 6;
    public final int WHIMS_POSITION = 8;
    public final int SEARCH_POSITION = 10;
    public final int APIKEY_POSITION = 11;
    public final int ABOUT_POSITION = 12;
    protected Drawer drawer;
    protected Toolbar toolbar;
    protected PrimaryDrawerItem apiKeyDrawerItem;
    protected PrimaryDrawerItem newsItemDrawerItem;
    protected PrimaryDrawerItem whimsDrawerItem;
    private PrimaryDrawerItem searchDrawerItem;
    private PrimaryDrawerItem aboutDrawerItem;

    protected void onCreateDrawer() {

        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(this).withHeaderBackground(R.color.primary).build();

        newsItemDrawerItem = new PrimaryDrawerItem().withName("Industry News");
        newsItemDrawerItem.withIcon(R.drawable.ic_news);
        PrimaryDrawerItem forum = new PrimaryDrawerItem().withName("Discussion Forum");
        forum.withIcon(R.drawable.ic_forum);

        PrimaryDrawerItem popularItems = new PrimaryDrawerItem().withName("Popular Threads");
        popularItems.withIcon(R.drawable.ic_popular_threads);
        PrimaryDrawerItem recentItems = new PrimaryDrawerItem().withName("Recent Threads");
        recentItems.withIcon(R.drawable.ic_recent_threads);
        PrimaryDrawerItem watchedItems = new PrimaryDrawerItem().withName("Watched Threads");
        watchedItems.withIcon(R.drawable.ic_watched_threads);

        whimsDrawerItem = new PrimaryDrawerItem().withName("Private Messages");
        whimsDrawerItem.withIcon(R.drawable.ic_whims);
        apiKeyDrawerItem = new PrimaryDrawerItem().withName("Set API Key");
        apiKeyDrawerItem.withIcon(R.drawable.ic_api_key);
        searchDrawerItem = new PrimaryDrawerItem().withName("Search");
        searchDrawerItem.withIcon(R.drawable.ic_search_threads);
        aboutDrawerItem = new PrimaryDrawerItem().withName("About").withIcon(R.drawable.ic_about_me);

        drawer = new DrawerBuilder().withActivity(this)
                .addDrawerItems(newsItemDrawerItem,
                        forum, new DividerDrawerItem(), popularItems, recentItems, watchedItems,
                        new DividerDrawerItem(), whimsDrawerItem, new DividerDrawerItem(),
                        searchDrawerItem, apiKeyDrawerItem, aboutDrawerItem)
                .withActionBarDrawerToggle(false)
                .withAccountHeader(headerResult)
                .withOnDrawerItemClickListener(this)
                .build();

    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        onCreateDrawer();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setupDrawerToggle();
    }

    protected void setupDrawerToggle() {
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(
                this, drawer.getDrawerLayout(), toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.getDrawerLayout().setDrawerListener(mDrawerToggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        mDrawerToggle.syncState();
    }

    @Override
    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {

        FragmentsEnum fragmentToStart;
        switch (position) {
            case NEWS_POSITION:
                fragmentToStart = FragmentsEnum.NEWS;
                break;
            case APIKEY_POSITION:
                fragmentToStart = FragmentsEnum.API_KEY;
                break;
            case FORUM_POSITION:
                fragmentToStart = FragmentsEnum.FORUM;
                break;
            case RECENT_POSITION:
                fragmentToStart = FragmentsEnum.RECENT_THREAD;
                break;
            case WATCHED_POSITION:
                fragmentToStart = FragmentsEnum.WATCHED_THREAD;
                break;
            case WHIMS_POSITION:
                fragmentToStart = FragmentsEnum.WHIMS;
                break;
            case POPULAR_POSITION:
                fragmentToStart = FragmentsEnum.POPULAR_THREAD;
                break;
            case SEARCH_POSITION:
                fragmentToStart = FragmentsEnum.SEARCH;
                break;
            case ABOUT_POSITION:
                fragmentToStart = FragmentsEnum.ABOUT;
                break;
            default:
                fragmentToStart = FragmentsEnum.NEWS;
        }

        startFragment(fragmentToStart);

        return true;
    }

    protected void startFragmentWithNoBackStack(FragmentsEnum fragmentsEnum) {
        Fragment fragmentToStart = InstantiateFragment(fragmentsEnum);
        ReplaceFragment(fragmentToStart, false);
    }

    protected void startFragment(FragmentsEnum fragment) {
        Fragment fragmentToStart = InstantiateFragment(fragment);

        //need a way to set title as i dont own the About Fragment
        if (fragment == FragmentsEnum.ABOUT)
            getSupportActionBar().setTitle("About");

        ReplaceFragment(fragmentToStart, true);
    }

    private void ReplaceFragment(Fragment fragmentToStart, Boolean addToBackStack) {
        FragmentTransaction fts = getSupportFragmentManager().beginTransaction();
        fts.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
        FragmentTransaction fragmentTransaction = fts.replace(R.id.fragment_placeholder, fragmentToStart);

        if (addToBackStack)
            fragmentTransaction.addToBackStack(null);

        fragmentTransaction.commit();

        drawer.closeDrawer();
    }

    @NonNull
    private Fragment InstantiateFragment(FragmentsEnum fragment) {
        Fragment fragmentToStart;
        switch (fragment) {
            case NEWS:
                fragmentToStart = new NewsFragment();
                break;
            case API_KEY:
                fragmentToStart = new LoginFragment();
                break;
            case FORUM:
                fragmentToStart = new ForumFragment();
                break;
            case RECENT_THREAD:
                fragmentToStart = new RecentFragment();
                break;
            case WATCHED_THREAD:
                fragmentToStart = new WatchedFragment();
                break;
            case WHIMS:
                fragmentToStart = new WhimsFragment();
                break;
            case POPULAR_THREAD:
                fragmentToStart = new PopularThreadFragment();
                break;
            case SEARCH:
                fragmentToStart = new SearchFragment();
                break;
            case ABOUT:
                fragmentToStart = CreateAboutFragment();
                break;
            default:
                fragmentToStart = new NewsFragment();
        }
        return fragmentToStart;
    }

    private LibsSupportFragment CreateAboutFragment() {
        return new LibsBuilder()
                .withExcludedLibraries("NineOldAndroids",
                        "AboutLibraries")
                .supportFragment();
    }
}

