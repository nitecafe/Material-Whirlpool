package com.android.nitecafe.whirlpoolnews.ui.activities;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.android.nitecafe.whirlpoolnews.R;
import com.android.nitecafe.whirlpoolnews.ui.fragments.ForumFragment;
import com.android.nitecafe.whirlpoolnews.ui.fragments.LoginFragment;
import com.android.nitecafe.whirlpoolnews.ui.fragments.NewsFragment;
import com.android.nitecafe.whirlpoolnews.ui.fragments.PopularThreadFragment;
import com.android.nitecafe.whirlpoolnews.ui.fragments.PostBookmarkFragment;
import com.android.nitecafe.whirlpoolnews.ui.fragments.RecentFragment;
import com.android.nitecafe.whirlpoolnews.ui.fragments.SearchFragment;
import com.android.nitecafe.whirlpoolnews.ui.fragments.WatchedFragment;
import com.android.nitecafe.whirlpoolnews.ui.fragments.WhimsFragment;
import com.android.nitecafe.whirlpoolnews.ui.fragments.WhirlpoolPreferencesFragment;
import com.mikepenz.aboutlibraries.LibsBuilder;
import com.mikepenz.aboutlibraries.ui.LibsSupportFragment;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

public abstract class NavigationDrawerActivity extends AppCompatActivity implements Drawer.OnDrawerItemClickListener {

    public final int NEWS_POSITION = 1;
    public final int FORUM_POSITION = 2;
    public final int POPULAR_POSITION = 4;
    public final int RECENT_POSITION = 5;
    public final int WATCHED_POSITION = 6;
    public final int POST_BOOKMARK_POSITION = 8;
    public final int WHIMS_POSITION = 9;
    public final int SEARCH_POSITION = 11;
    public final int SETTING_POSITION = 12;
    public final int APIKEY_POSITION = 13;
    public final int ABOUT_POSITION = 14;
    protected Drawer drawer;
    protected Toolbar toolbar;
    protected PrimaryDrawerItem apiKeyDrawerItem;
    protected PrimaryDrawerItem newsItemDrawerItem;
    protected PrimaryDrawerItem whimsDrawerItem;
    protected PrimaryDrawerItem forumDrawerItems;
    protected PrimaryDrawerItem popularItems;
    protected PrimaryDrawerItem recentItems;
    protected PrimaryDrawerItem watchedItems;
    private AccountHeader headerResult;
    private ProfileDrawerItem profileDrawerItem;
    private PrimaryDrawerItem postBookmarkItem;

    protected void onCreateDrawer() {
        profileDrawerItem = new ProfileDrawerItem().withName("Hello").withEmail(getResources().getString(R.string.app_name) + " v" + getVersionName());
        headerResult = new AccountHeaderBuilder()
                .withActivity(this).withHeaderBackground(R.color.primary)
                .addProfiles(profileDrawerItem)
                .withSelectionListEnabled(false)
                .withProfileImagesVisible(false)
                .withCompactStyle(true)
                .withProfileImagesClickable(false)
                .build();

        newsItemDrawerItem = new PrimaryDrawerItem().withName(R.string.drawer_item_industry_news).withIconTintingEnabled(true);
        newsItemDrawerItem.withIcon(R.drawable.ic_news);
        forumDrawerItems = new PrimaryDrawerItem().withName(R.string.drawer_item_discussion_forum).withIconTintingEnabled(true);
        forumDrawerItems.withIcon(R.drawable.ic_forum);

        popularItems = new PrimaryDrawerItem().withName(R.string.drawer_item_popular_threads).withIconTintingEnabled(true);
        popularItems.withIcon(R.drawable.ic_popular_threads);
        recentItems = new PrimaryDrawerItem().withName(R.string.drawer_item_recent_threads).withIconTintingEnabled(true);
        recentItems.withIcon(R.drawable.ic_recent_threads);
        watchedItems = new PrimaryDrawerItem().withName(R.string.drawer_item_watched_threads).withIconTintingEnabled(true);
        watchedItems.withIcon(R.drawable.ic_watched_threads);
        postBookmarkItem = new PrimaryDrawerItem().withName(R.string.drawer_item_post_bookmarks).withIcon(R.drawable.ic_bookmark_post)
                .withIconTintingEnabled(true);

        whimsDrawerItem = new PrimaryDrawerItem().withName(R.string.drawer_item_private_messages).withIconTintingEnabled(true);
        whimsDrawerItem.withIcon(R.drawable.ic_whims);
        apiKeyDrawerItem = new PrimaryDrawerItem().withName(R.string.drawer_item_api_key).withIconTintingEnabled(true);
        apiKeyDrawerItem.withIcon(R.drawable.ic_api_key);
        PrimaryDrawerItem searchDrawerItem = new PrimaryDrawerItem().withName(R.string.drawer_item_search).withIconTintingEnabled(true);
        searchDrawerItem.withIcon(R.drawable.ic_search_threads);
        PrimaryDrawerItem aboutDrawerItem = new PrimaryDrawerItem().withName(R.string.drawer_item_about).withIcon(R.drawable.ic_about_me).withIconTintingEnabled(true);
        PrimaryDrawerItem settingDrawerItem = new PrimaryDrawerItem()
                .withName(R.string.drawer_item_settings).withIconTintingEnabled(true)
                .withIcon(R.drawable.ic_settings);

        drawer = new DrawerBuilder().withActivity(this)
                .addDrawerItems(newsItemDrawerItem,
                        forumDrawerItems, new DividerDrawerItem(), popularItems, recentItems, watchedItems,
                        new DividerDrawerItem(), postBookmarkItem, whimsDrawerItem, new DividerDrawerItem(),
                        searchDrawerItem, settingDrawerItem, apiKeyDrawerItem, aboutDrawerItem)
                .withActionBarDrawerToggle(false)
                .withAccountHeader(headerResult)
                .withOnDrawerItemClickListener(this)
                .build();
    }

    protected PrimaryDrawerItem getDrawerItemFromString(String s) {

        String forum = getString(R.string.title_discussion_forum);
        String popularThreads = getString(R.string.title_popular_threads);
        String recentThreads = getString(R.string.title_recent_posts);
        String watchedThreads = getString(R.string.title_watched_threads);
        String privateMessages = getString(R.string.title_private_messages);
        String postBookmark = getString(R.string.title_post_bookmark);

        if (s.equals(forum))
            return forumDrawerItems;
        if (s.equals(popularThreads))
            return popularItems;
        if (s.equals(recentThreads))
            return recentItems;
        if (s.equals(watchedThreads))
            return watchedItems;
        if (s.equals(privateMessages))
            return whimsDrawerItem;
        if (s.equals(postBookmark))
            return postBookmarkItem;

        return newsItemDrawerItem;

    }

    protected int getPositionFromDrawerItem(PrimaryDrawerItem item) {

        if (item == forumDrawerItems)
            return FORUM_POSITION;
        if (item == popularItems)
            return POPULAR_POSITION;
        if (item == recentItems)
            return RECENT_POSITION;
        if (item == watchedItems)
            return WATCHED_POSITION;
        if (item == whimsDrawerItem)
            return WHIMS_POSITION;
        if (item == postBookmarkItem)
            return POST_BOOKMARK_POSITION;

        return NEWS_POSITION;
    }

    protected void updateProfileDetails(String username) {
        profileDrawerItem.withName(username);
        headerResult.updateProfile(profileDrawerItem);
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

        drawer.getDrawerLayout().addDrawerListener(mDrawerToggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        mDrawerToggle.syncState();
    }

    @Override
    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {

        Fragment fragmentToStart = InstantiateFragment(position);

        //need a way to set title as i dont own the About Fragment
        if (position == ABOUT_POSITION)
            getSupportActionBar().setTitle(getString(R.string.drawer_item_about));

        ReplaceFragment(fragmentToStart, true);
        return true;
    }

    protected void startFragmentWithNoBackStack(int position) {
        Fragment fragmentToStart = InstantiateFragment(position);
        ReplaceFragment(fragmentToStart, false);
    }

    private void ReplaceFragment(Fragment fragmentToStart, Boolean addToBackStack) {
        FragmentTransaction fts = getSupportFragmentManager().beginTransaction();
        fts.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
        fts.replace(R.id.fragment_placeholder, fragmentToStart);

        if (addToBackStack)
            fts.addToBackStack(null);

        fts.commit();
        drawer.closeDrawer();
    }

    @NonNull
    private Fragment InstantiateFragment(Integer position) {
        Fragment fragmentToStart;
        switch (position) {
            case NEWS_POSITION:
                fragmentToStart = new NewsFragment();
                break;
            case APIKEY_POSITION:
                final LoginFragment loginFragment = new LoginFragment();
                loginFragment.UserNameSubject.subscribe(s -> updateProfileDetails(s));
                fragmentToStart = loginFragment;
                break;
            case FORUM_POSITION:
                fragmentToStart = new ForumFragment();
                break;
            case RECENT_POSITION:
                fragmentToStart = new RecentFragment();
                break;
            case WATCHED_POSITION:
                fragmentToStart = new WatchedFragment();
                break;
            case WHIMS_POSITION:
                fragmentToStart = new WhimsFragment();
                break;
            case POPULAR_POSITION:
                fragmentToStart = new PopularThreadFragment();
                break;
            case SEARCH_POSITION:
                fragmentToStart = new SearchFragment();
                break;
            case ABOUT_POSITION:
                fragmentToStart = CreateAboutFragment();
                break;
            case SETTING_POSITION:
                fragmentToStart = new WhirlpoolPreferencesFragment();
                break;
            case POST_BOOKMARK_POSITION:
                fragmentToStart = new PostBookmarkFragment();
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

    @Override public void onBackPressed() {
        if (drawer != null && drawer.isDrawerOpen())
            drawer.closeDrawer();
        else
            super.onBackPressed();
    }


    protected String getVersionName() {
        PackageInfo pInfo;
        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }
        return pInfo.versionName;
    }

}

