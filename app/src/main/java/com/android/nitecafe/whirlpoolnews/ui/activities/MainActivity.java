package com.android.nitecafe.whirlpoolnews.ui.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.android.nitecafe.whirlpoolnews.R;
import com.android.nitecafe.whirlpoolnews.WhirlpoolApp;
import com.android.nitecafe.whirlpoolnews.constants.StringConstants;
import com.android.nitecafe.whirlpoolnews.ui.fragments.ForumFragment;
import com.android.nitecafe.whirlpoolnews.ui.fragments.IndividualWhimFragment;
import com.android.nitecafe.whirlpoolnews.ui.fragments.LoginFragment;
import com.android.nitecafe.whirlpoolnews.ui.fragments.ScrapedPostParentFragment;
import com.android.nitecafe.whirlpoolnews.ui.fragments.ScrapedThreadFragment;
import com.android.nitecafe.whirlpoolnews.ui.fragments.SearchResultThreadFragment;
import com.android.nitecafe.whirlpoolnews.ui.fragments.ThreadFragment;
import com.android.nitecafe.whirlpoolnews.ui.interfaces.IOnSearchClicked;
import com.android.nitecafe.whirlpoolnews.ui.interfaces.IOnThreadClicked;
import com.android.nitecafe.whirlpoolnews.ui.interfaces.IOnWhimClicked;
import com.android.nitecafe.whirlpoolnews.utilities.ThreadScraper;
import com.android.nitecafe.whirlpoolnews.utilities.interfaces.IPreferencesGetter;
import com.android.nitecafe.whirlpoolnews.web.WhimsService;
import com.android.nitecafe.whirlpoolnews.web.interfaces.IWatchedThreadService;
import com.android.nitecafe.whirlpoolnews.web.interfaces.IWhirlpoolRestClient;
import com.mikepenz.materialdrawer.holder.BadgeStyle;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.pushbots.push.Pushbots;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.subjects.PublishSubject;

public class MainActivity extends NavigationDrawerActivity implements LoginFragment.OnShowHomeScreenListener, ForumFragment.IOnForumClicked, IOnThreadClicked, IOnWhimClicked, IOnSearchClicked {

    @Inject IWhirlpoolRestClient mWhirlpoolRestClient;
    @Bind(R.id.fab_reply_post) FloatingActionButton fabReplyPost;
    @Bind(R.id.fab_create_thread) FloatingActionButton fabCreateThread;
    @Bind(R.id.fab_reply_whim) FloatingActionButton fabReplyWhim;
    @Inject IWatchedThreadService watchedThreadIdentifier;
    @Inject WhimsService whimsService;
    @Inject @Named("whim") PublishSubject<Void> whimSubject;
    @Inject SharedPreferences mSharedPreferences;
    @Inject IPreferencesGetter preferencesGetter;
    private int mThreadIdLoaded;
    private int mForumId;
    private int whimId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        ((WhirlpoolApp) getApplication()).getDaggerComponent().inject(this);
        setThemeBasedOnSettings();
        setFontSizeBasedOnSettings();
        super.onCreate(savedInstanceState);
        Pushbots.sharedInstance().init(this); //pushbot
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        final Bundle bundleExtra = getIntent().getExtras();

        //for showing pushbot messages
        if (bundleExtra != null) {
            final String title = bundleExtra.getString(StringConstants.PUSHBOT_TITLE_KEY);
            final String message = bundleExtra.getString(StringConstants.PUSHBOT_FULLMESSAGE_KEY);
            if (message != null && title != null) {
                new MaterialDialog.Builder(this)
                        .title(title)
                        .content(message)
                        .positiveText("OK")
                        .show();
                getIntent().removeExtra(StringConstants.PUSHBOT_FULLMESSAGE_KEY);
                getIntent().removeExtra(StringConstants.PUSHBOT_TITLE_KEY);
            }
        }

        String scheme = getIntent().getScheme();
        if (IsFromInternalAppLink(scheme)) {
            parseInternalLink();
        } else if (!mWhirlpoolRestClient.hasApiKeyBeenSet()) {
            drawer.setSelection(apiKeyDrawerItem, false);
            startFragmentWithNoBackStack(APIKEY_POSITION);
        } else if (savedInstanceState == null) {

            String homeScreen = preferencesGetter.getHomeScreen();
            if (homeScreen.isEmpty()) {
                drawer.setSelection(newsItemDrawerItem, false);
                startFragmentWithNoBackStack(NEWS_POSITION);
            } else {
                PrimaryDrawerItem drawerItemFromString = getDrawerItemFromString(homeScreen);
                drawer.setSelection(drawerItemFromString, false);
                int position = getPositionFromDrawerItem(drawerItemFromString);
                startFragmentWithNoBackStack(position);
            }
        }

        whimSubject.subscribe(aVoid -> updateWhimDrawerItemBadge());

        final String userNameFromPreference = getUserNameFromPreference();
        if (!userNameFromPreference.isEmpty())
            updateProfileDetails(userNameFromPreference);
    }

    private void setThemeBasedOnSettings() {
        if (preferencesGetter.isDarkThemeOn())
            setTheme(R.style.AppTheme_Dark);
        else
            setTheme(R.style.AppTheme);
    }

    private void setFontSizeBasedOnSettings() {
        if (preferencesGetter.isBiggerFontSize())
            getTheme().applyStyle(R.style.BigTextSize, true);
    }

    private String getUserNameFromPreference() {
        return mSharedPreferences.getString(StringConstants.USERNAME, "");
    }

    private void parseInternalLink() {
        Uri intent_uri = getIntent().getData();
        int threadId = Integer.parseInt(intent_uri.getQueryParameter("threadid"));
        int page;
        try {
            page = Integer.parseInt(intent_uri.getQueryParameter("p"));
        } catch (NumberFormatException e) {
            page = 1;
        }

        WhirlpoolApp.getInstance().trackEvent("Internal Links", "Opening internal links", "");
        startPostViewPagerFragmentNoBackStack(threadId, "Thread From Link", 1, page, 0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        watchedThreadIdentifier.getWatchedThreads();
        updateWhimDrawerItemBadge();

        //google analytic
        WhirlpoolApp.getInstance().trackScreenView("Main Activity");
    }

    private void updateWhimDrawerItemBadge() {
        whimsService.GetNumberOfUnreadWhims().
                subscribe(integer -> setPrivateMessagesBadgeCount(integer),
                        throwable -> Log.e("MainActivity", "Failed to retrieve whim."));
    }

    private void setPrivateMessagesBadgeCount(Integer integer) {
        whimsDrawerItem.withBadge(String.valueOf(integer))
                .withBadgeStyle(new BadgeStyle().withTextColor(Color.WHITE).
                        withColorRes(R.color.primary));
        drawer.updateItem(whimsDrawerItem);
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
        WhirlpoolApp.getInstance().trackEvent("RecycleView Click", "View Forum", "Opening Forum to view threads");
        mForumId = forumId;

        if (ThreadScraper.isPublicForum(forumId)) {
            ScrapedThreadFragment scrapedThreadFragment = ScrapedThreadFragment.newInstance(forumId, forumTitle, 0);
            setUpThreadCreateFab(scrapedThreadFragment.OnFragmentCreateViewSubject, scrapedThreadFragment.OnFragmentDestroySubject);
            startFragment(scrapedThreadFragment);
        } else {
            ThreadFragment threadFragment = ThreadFragment.newInstance(forumId, forumTitle);
            setUpThreadCreateFab(threadFragment.OnFragmentCreateViewSubject, threadFragment.OnFragmentDestroySubject);
            startFragment(threadFragment);
        }
    }

    @Override
    public void OnThreadClicked(int threadId, String threadTitle, int totalPage) {
        if (preferencesGetter.getOpenLastPage())
            OnThreadClicked(threadId, threadTitle, totalPage, 0, totalPage);
        else
            OnThreadClicked(threadId, threadTitle, 1, 0, totalPage);
    }

    @Override
    public void OnThreadClicked(int threadId, String threadTitle, int lastPageRead, int lastReadId, int totalPage) {
        WhirlpoolApp.getInstance().trackEvent("RecycleView Click", "View Thread", "Opening thread to view posts");
        startPostViewPagerFragment(threadId, threadTitle, totalPage, lastPageRead, lastReadId);
    }

    private void startPostViewPagerFragment(int threadId, String threadTitle, int totalPage, int page, int postLastRead) {
        mThreadIdLoaded = threadId;
        ScrapedPostParentFragment scrapedPostParentFragment = ScrapedPostParentFragment.newInstance(threadId, threadTitle, page, postLastRead, totalPage);
//        setUpPostReplyFab(scrapedPostParentFragment);
        startFragment(scrapedPostParentFragment);
    }

    private void startPostViewPagerFragmentNoBackStack(int threadId, String threadTitle, int totalPage, int page, int postLastRead) {
        mThreadIdLoaded = threadId;
        ScrapedPostParentFragment scrapedPostParentFragment = ScrapedPostParentFragment.newInstance(threadId, threadTitle, page, postLastRead, totalPage);
        startFragmentNoBackStack(scrapedPostParentFragment);
    }

//    private void setUpPostReplyFab(ScrapedPostParentFragment scrapedPostParentFragment) {
//        scrapedPostParentFragment.OnFragmentDestroySubject.subscribe(aVoid ->
//                fabReplyPost.setVisibility(View.GONE));
//        scrapedPostParentFragment.OnFragmentCreateViewSubject.subscribe(aVoid -> {
//            resetFabLocationToBottom(fabReplyPost);
//            fabReplyPost.setVisibility(View.VISIBLE);
//        });
//    }

    private void setUpWhimReplyFab(IndividualWhimFragment individualWhimFragment) {
        individualWhimFragment.OnFragmentDestroySubject.subscribe(aVoid ->
                fabReplyWhim.setVisibility(View.GONE));
        individualWhimFragment.OnFragmentCreateViewSubject.subscribe(aVoid -> {
            resetFabLocationToBottom(fabReplyWhim);
            fabReplyWhim.setVisibility(View.VISIBLE);
        });
    }

    private void setUpThreadCreateFab(PublishSubject<Void> createStream, PublishSubject<Void> destroyStream) {
        destroyStream.subscribe(aVoid ->
                fabCreateThread.setVisibility(View.GONE));
        createStream.subscribe(aVoid -> {
            resetFabLocationToBottom(fabCreateThread);
            fabCreateThread.setVisibility(View.VISIBLE);
        });
    }

    /**
     * To fix a bug where fab not reverting to original location after it has
     * been moved up by the snackbar and visibility was set to gone.
     */
    private void resetFabLocationToBottom(FloatingActionButton floatingActionButton) {
        floatingActionButton.setTranslationY(0.0f);
    }

    private void startFragment(Fragment fragment) {
        FragmentTransaction fts = getSupportFragmentManager().beginTransaction();
        fts.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
        fts.replace(R.id.fragment_placeholder, fragment);
        fts.addToBackStack(null);
        fts.commit();
    }

    private void startFragmentNoBackStack(Fragment fragment) {
        FragmentTransaction fts = getSupportFragmentManager().beginTransaction();
        fts.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
        fts.replace(R.id.fragment_placeholder, fragment);
        fts.commit();
    }


    @OnClick(R.id.fab_reply_post)
    public void launchReplyPageInBrowser() {
        WhirlpoolApp.getInstance().trackEvent("FAB", "Reply Post", "");
        Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse(StringConstants.REPLY_URL + String.valueOf(mThreadIdLoaded)));
        startActivity(browserIntent);
    }

    @OnClick(R.id.fab_create_thread)
    public void launchCreateThreadInBrowser() {
        WhirlpoolApp.getInstance().trackEvent("FAB", "Create Thread", "");
        Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse(StringConstants.NEW_THREAD_URL + String.valueOf(mForumId)));
        startActivity(browserIntent);
    }

    @Override
    public void OnWhimClicked(int id, String message, String sender) {
        WhirlpoolApp.getInstance().trackEvent("RecycleView Click", "View Individual Whims", "");
        whimId = id;
        IndividualWhimFragment individualWhimFragment = IndividualWhimFragment.newInstance(message, sender);
        setUpWhimReplyFab(individualWhimFragment);
        startFragment(individualWhimFragment);
    }

    @OnClick(R.id.fab_reply_whim)
    public void launchReplyWhimInBrowser() {
        WhirlpoolApp.getInstance().trackEvent("FAB", "Reply Whim", "");
        Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse(StringConstants.WHIM_REPLY_URL + String.valueOf(whimId)));
        startActivity(browserIntent);
    }

    @Override
    public void onSearchClicked(String query, int forumId, int groupId) {
        WhirlpoolApp.getInstance().trackEvent("Search", "Search", "");
        final SearchResultThreadFragment searchResultThreadFragment = SearchResultThreadFragment.newInstance(query, forumId, groupId);
        startFragment(searchResultThreadFragment);
    }
}
