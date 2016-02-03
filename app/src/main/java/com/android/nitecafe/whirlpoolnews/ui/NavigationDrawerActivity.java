package com.android.nitecafe.whirlpoolnews.ui;

import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.android.nitecafe.whirlpoolnews.R;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

public class NavigationDrawerActivity extends AppCompatActivity implements Drawer.OnDrawerItemClickListener {

    protected Drawer drawer;
    protected Toolbar toolbar;

    public final int NEWS_POSITION = 1;
    public final int FORUM_POSITION = 2;
    public final int POPULAR_POSITION = 4;
    public final int RECENT_POSITION = 5;
    public final int WATCHED_POSITION = 6;
    public final int WHIMS_POSITION = 8;
    public final int APIKEY_POSITION = 10;
    protected PrimaryDrawerItem apiKeyDrawerItem;
    protected PrimaryDrawerItem newsItemDrawerItem;

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

        PrimaryDrawerItem whims = new PrimaryDrawerItem().withName("Private Messages");
        whims.withIcon(R.drawable.ic_whims);
        apiKeyDrawerItem = new PrimaryDrawerItem().withName("Set API Key");
        apiKeyDrawerItem.withIcon(R.drawable.ic_api_key);

        drawer = new DrawerBuilder().withActivity(this)
                .addDrawerItems(newsItemDrawerItem,
                        forum, new DividerDrawerItem(), popularItems, recentItems, watchedItems,
                        new DividerDrawerItem(), whims, new DividerDrawerItem(), apiKeyDrawerItem)
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
    }

    @Override public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {

        Fragment fragmentToStart;
        switch (position) {
            case NEWS_POSITION:
                fragmentToStart = new NewsFragment();
                break;
            case APIKEY_POSITION:
                fragmentToStart = new LoginFragment();
                break;
            default:
                fragmentToStart = new NewsFragment();
        }

        startFragment(fragmentToStart);

        return true;
    }

    protected void startFragment(Fragment fragment) {
        FragmentTransaction fts = getSupportFragmentManager().beginTransaction();
        FragmentTransaction fragmentTransaction = fts.replace(R.id.fragment_placeholder, fragment);

        if (!(fragment instanceof LoginFragment))
            fragmentTransaction.addToBackStack(null);

        fts.commit();

        drawer.closeDrawer();
    }
}
