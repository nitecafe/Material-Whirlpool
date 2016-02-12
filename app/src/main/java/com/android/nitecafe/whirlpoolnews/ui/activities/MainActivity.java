package com.android.nitecafe.whirlpoolnews.ui.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.android.nitecafe.whirlpoolnews.R;
import com.android.nitecafe.whirlpoolnews.WhirlpoolApp;
import com.android.nitecafe.whirlpoolnews.constants.StringConstants;
import com.android.nitecafe.whirlpoolnews.interfaces.IWhirlpoolRestClient;
import com.android.nitecafe.whirlpoolnews.ui.fragments.ForumFragment;
import com.android.nitecafe.whirlpoolnews.ui.fragments.LoginFragment;
import com.android.nitecafe.whirlpoolnews.ui.fragments.ScrapedPostFragment;
import com.android.nitecafe.whirlpoolnews.ui.fragments.ScrapedThreadFragment;
import com.android.nitecafe.whirlpoolnews.ui.fragments.ThreadFragment;
import com.android.nitecafe.whirlpoolnews.ui.interfaces.IOnThreadClicked;
import com.android.nitecafe.whirlpoolnews.utilities.ThreadScraper;
import com.marshalchen.ultimaterecyclerview.ui.floatingactionbutton.FloatingActionButton;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends NavigationDrawerActivity implements LoginFragment.OnShowHomeScreenListener, ForumFragment.IOnForumClicked, IOnThreadClicked {

    @Inject IWhirlpoolRestClient mWhirlpoolRestClient;
    @Bind(R.id.fab_reply_post) FloatingActionButton fabReplyPost;
    private int mThreadIdLoaded;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ((WhirlpoolApp) getApplication()).getDaggerComponent().inject(this);
        ButterKnife.bind(this);

        String scheme = getIntent().getScheme();
        if (IsFromInternalAppLink(scheme)) {
            Uri intent_uri = getIntent().getData();
            int threadId = Integer.parseInt(intent_uri.getQueryParameter("threadid"));
            OnThreadClicked(threadId, "Thread From Link");
        } else if (!mWhirlpoolRestClient.hasApiKeyBeenSet()) {
            drawer.setSelection(apiKeyDrawerItem);
        } else if (savedInstanceState == null) {
            drawer.setSelection(newsItemDrawerItem);
        }
    }

    private boolean IsFromInternalAppLink(String scheme) {
        return scheme != null && scheme.equals("com.nitecafe.whirlpool");
    }

    @Override
    public void showHomeScreen() {
        drawer.setSelection(newsItemDrawerItem);
    }

    @Override
    public void onForumClicked(int forumId, String forumTitle) {
        Fragment threadFragment;

        if (ThreadScraper.isPublicForum(forumId)) {
            threadFragment = ScrapedThreadFragment.newInstance(forumId, forumTitle, 0);
        } else
            threadFragment = ThreadFragment.newInstance(forumId, forumTitle);

        startFragment(threadFragment);
    }

    @Override
    public void OnThreadClicked(int threadId, String threadTitle) {
        OnWatchedThreadClicked(threadId, threadTitle, 1, 0);
    }

    @Override
    public void OnWatchedThreadClicked(int threadId, String threadTitle, int lastPageRead, int lastReadId) {
        mThreadIdLoaded = threadId;
        ScrapedPostFragment scrapedPostFragment = ScrapedPostFragment.newInstance(threadId, threadTitle, lastPageRead, lastReadId);
        startFragment(scrapedPostFragment);
        setUpPostReplyFab(scrapedPostFragment);
    }

    private void setUpPostReplyFab(ScrapedPostFragment scrapedPostFragment) {
        scrapedPostFragment.OnFragmentDestroySubject.subscribe(aVoid -> fabReplyPost.setVisibility(View.GONE));
        fabReplyPost.setVisibility(View.VISIBLE);
    }

    private void startFragment(Fragment fragment) {
        FragmentTransaction fts = getSupportFragmentManager().beginTransaction();
        FragmentTransaction fragmentTransaction = fts.replace(R.id.fragment_placeholder, fragment);
        fragmentTransaction.addToBackStack(null);
        fts.commit();
    }

    @OnClick(R.id.fab_reply_post)
    public void launchReplyPageInBrowser() {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse(StringConstants.REPLY_URL + String.valueOf(mThreadIdLoaded)));
        startActivity(browserIntent);
    }
}
