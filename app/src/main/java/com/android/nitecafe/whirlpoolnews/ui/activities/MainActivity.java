package com.android.nitecafe.whirlpoolnews.ui.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.android.nitecafe.whirlpoolnews.R;
import com.android.nitecafe.whirlpoolnews.WhirlpoolApp;
import com.android.nitecafe.whirlpoolnews.interfaces.IWhirlpoolRestClient;
import com.android.nitecafe.whirlpoolnews.ui.fragments.ForumFragment;
import com.android.nitecafe.whirlpoolnews.ui.fragments.LoginFragment;
import com.android.nitecafe.whirlpoolnews.ui.fragments.RecentFragment;
import com.android.nitecafe.whirlpoolnews.ui.fragments.ThreadFragment;

import javax.inject.Inject;

public class MainActivity extends NavigationDrawerActivity implements LoginFragment.OnShowHomeScreenListener, ForumFragment.IOnForumClicked, RecentFragment.IOnThreadClicked {

    @Inject IWhirlpoolRestClient mWhirlpoolRestClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ((WhirlpoolApp) getApplication()).getDaggerComponent().inject(this);


        if (!mWhirlpoolRestClient.hasApiKeyBeenSet()) {
            drawer.setSelection(apiKeyDrawerItem);
        } else if (savedInstanceState == null) {
            drawer.setSelection(newsItemDrawerItem);
        }
    }

    @Override
    public void showHomeScreen() {
        drawer.setSelection(newsItemDrawerItem);
    }

    @Override
    public void onForumClicked(int forumId, String forumTitle) {
        final ThreadFragment threadFragment = ThreadFragment.newInstance(forumId, forumTitle);
        FragmentTransaction fts = getSupportFragmentManager().beginTransaction();
        FragmentTransaction fragmentTransaction = fts.replace(R.id.fragment_placeholder, threadFragment);
        fragmentTransaction.addToBackStack(null);
        fts.commit();
    }

    @Override
    public void OnThreadClicked(int threadId, String threadTitle) {

    }
}
