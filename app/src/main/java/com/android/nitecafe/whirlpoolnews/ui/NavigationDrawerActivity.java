package com.android.nitecafe.whirlpoolnews.ui;

import android.support.annotation.LayoutRes;
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

    protected void onCreateDrawer() {

        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(this).build();

        PrimaryDrawerItem newsItem = new PrimaryDrawerItem().withName("News");
        PrimaryDrawerItem popularItems = new PrimaryDrawerItem().withName("Popular Threads");
        PrimaryDrawerItem recentItems = new PrimaryDrawerItem().withName("Recent Threads");
        PrimaryDrawerItem watchedItems = new PrimaryDrawerItem().withName("Watched Threads");
        PrimaryDrawerItem forum = new PrimaryDrawerItem().withName("Forum");
        PrimaryDrawerItem whims = new PrimaryDrawerItem().withName("Whims");
        PrimaryDrawerItem apiKey = new PrimaryDrawerItem().withName("API Key");

        drawer = new DrawerBuilder().withActivity(this)
                .addDrawerItems(newsItem, new DividerDrawerItem(),
                        forum, popularItems, recentItems, watchedItems,
                        new DividerDrawerItem(), whims, new DividerDrawerItem(), apiKey)
                .withActionBarDrawerToggle(false)
                .withAccountHeader(headerResult)
                .build();
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID)
    {
        super.setContentView(layoutResID);
        onCreateDrawer();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
        return false;
    }
}
