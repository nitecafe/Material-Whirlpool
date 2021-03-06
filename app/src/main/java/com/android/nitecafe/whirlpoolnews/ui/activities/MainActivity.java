package com.android.nitecafe.whirlpoolnews.ui.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
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
import com.android.nitecafe.whirlpoolnews.controllers.MainPresenter;
import com.android.nitecafe.whirlpoolnews.scheduler.ISchedulerManager;
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
import com.android.nitecafe.whirlpoolnews.utilities.customTabs.CustomTabsActivityHelper;
import com.android.nitecafe.whirlpoolnews.utilities.interfaces.IPreferencesGetter;
import com.android.nitecafe.whirlpoolnews.web.WhimsService;
import com.android.nitecafe.whirlpoolnews.web.interfaces.IWatchedThreadService;
import com.android.nitecafe.whirlpoolnews.web.interfaces.IWhirlpoolRestClient;
import com.android.nitecafe.whirlpoolnews.web.interfaces.IWhirlpoolRestService;
import com.mikepenz.materialdrawer.holder.BadgeStyle;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.pushbots.push.Pushbots;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;
import rx.subjects.PublishSubject;

public class MainActivity extends NavigationDrawerActivity implements LoginFragment.OnShowHomeScreenListener, ForumFragment.IOnForumClicked, IOnThreadClicked, IOnWhimClicked, IOnSearchClicked {

    private final String previousVersion = "2.5";
    @Inject IWhirlpoolRestClient mWhirlpoolRestClient;
    @Inject IWhirlpoolRestService mWhirlpoolRestService;
    @Bind(R.id.fab_create_thread) FloatingActionButton fabCreateThread;
    @Bind(R.id.fab_reply_whim) FloatingActionButton fabReplyWhim;
    @Inject IWatchedThreadService watchedThreadIdentifier;
    @Inject WhimsService whimsService;
    @Inject @Named("whim") PublishSubject<Void> whimSubject;
    @Inject @Named("browser") PublishSubject<Uri> launchBrowserSubject;
    @Inject @Named("prefetch") PublishSubject<Uri> prefetchSubject;
    @Inject @Named("prefetchBundle") PublishSubject<List<Bundle>> prefetchBundleSubject;
    @Inject SharedPreferences mSharedPreferences;
    @Inject IPreferencesGetter preferencesGetter;
    @Inject CustomTabsActivityHelper mCustomTabsActivityHelper;
    @Inject ISchedulerManager schedulerManager;
    @Inject MainPresenter mMainPresenter;
    private int mForumId;
    private int whimId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        ((WhirlpoolApp) getApplication()).getDaggerComponent().inject(this);
        setThemeBasedOnSettings();
        setFontSizeBasedOnSettings();
        super.onCreate(savedInstanceState);
        Pushbots.sharedInstance().init(this);
        Pushbots.sharedInstance().untag(previousVersion);
        Pushbots.sharedInstance().tag(getVersionName());
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        showPushBotMessage();
        launchStartingScreen(savedInstanceState);

        mSubscriptions.add(whimSubject.subscribe(aVoid -> updateWhimDrawerItemBadge()));
        mSubscriptions.add(launchBrowserSubject.subscribe(uri -> launchCustomTab(uri)));
        mSubscriptions.add(prefetchSubject.subscribe(uri -> prefetchUrl(uri)
                , throwable -> Log.e(StringConstants.LOG_ERROR_TAG, "Failed to prefetch single link.")));
        mSubscriptions.add(prefetchBundleSubject.subscribe(bundles -> prefetchBundle(bundles)
                , throwable -> Log.e(StringConstants.LOG_ERROR_TAG, "Failed to prefetch links from bundle.")));

        final String userNameFromPreference = preferencesGetter.getUserName();
        if (!userNameFromPreference.isEmpty()) {
            updateProfileDetails(userNameFromPreference);
            Pushbots.sharedInstance().setAlias(userNameFromPreference);
        }

        decodeBitmap(R.drawable.ic_arrow_back_sample);
    }

    private void launchStartingScreen(Bundle savedInstanceState) {
        final Bundle bundleExtra = getIntent().getExtras();

        String scheme = getIntent().getScheme();
        if (bundleExtra != null) {
            String string = bundleExtra.getString(StringConstants.NOTIFICATION_INTENT_SCREEN_KEY);
            if (StringConstants.NOTIFICATION_INTENT_WATCHED_SCREEN_KEY.equals(string)) {
                drawer.setSelection(watchedItems, false);
                startFragmentWithNoBackStack(WATCHED_POSITION);
                getIntent().removeExtra(StringConstants.NOTIFICATION_INTENT_SCREEN_KEY);
                return;
            } else if (StringConstants.NOTIFICATION_INTENT_WHIMS_SCREEN_KEY.equals(string)) {
                drawer.setSelection(whimsDrawerItem, false);
                startFragmentWithNoBackStack(WHIMS_POSITION);
                getIntent().removeExtra(StringConstants.NOTIFICATION_INTENT_SCREEN_KEY);
                return;
            }
        }

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
    }

    private void showPushBotMessage() {
        final Bundle bundleExtra = getIntent().getExtras();

        if (bundleExtra != null) {
            final String title = bundleExtra.getString(StringConstants.PUSHBOT_TITLE_KEY);
            final String message = bundleExtra.getString(StringConstants.PUSHBOT_FULLMESSAGE_KEY);
            final String downloadLink = bundleExtra.getString(StringConstants.PUSHBOT_DOWNLOAD_LINK_KEY);
            if (message != null && title != null) {
                MaterialDialog.Builder builder = new MaterialDialog.Builder(this)
                        .title(title)
                        .content(message);

                if (downloadLink == null) {
                    builder.negativeText("OK");
                } else {
                    builder.positiveText("Download")
                            .negativeText("Later")
                            .onPositive((dialog, which) -> launchLinkInBrowser(downloadLink));
                }

                builder.show();

                getIntent().removeExtra(StringConstants.PUSHBOT_FULLMESSAGE_KEY);
                getIntent().removeExtra(StringConstants.PUSHBOT_TITLE_KEY);
                getIntent().removeExtra(StringConstants.PUSHBOT_DOWNLOAD_LINK_KEY);
            }
        }
    }

    private void setThemeBasedOnSettings() {
        if (preferencesGetter.isDarkThemeOn())
            setTheme(R.style.AppTheme_Dark);
        else
            setTheme(R.style.AppTheme);
    }

    private void setFontSizeBasedOnSettings() {
        String fontSize = preferencesGetter.getFontSize();
        if (fontSize.equals(getString(R.string.font_size_large)))
            getTheme().applyStyle(R.style.LargeTextSize, true);
        else if (fontSize.equals(getString(R.string.font_size_very_large)))
            getTheme().applyStyle(R.style.VeryLargeTextSize, true);
        else if (fontSize.equals(getString(R.string.font_size_super_large)))
            getTheme().applyStyle(R.style.SuperLargeTextSize, true);
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
        mSubscriptions.add(whimsService.GetNumberOfUnreadWhims().
                subscribe(integer -> {
                            if (integer == 0)
                                hidePrivateMessagesBadgeCount();
                            else
                                setPrivateMessagesBadgeCount(integer);
                        },
                        throwable -> Log.e("MainActivity", "Failed to retrieve whim.")
                ));
    }

    private void hidePrivateMessagesBadgeCount() {
        whimsDrawerItem.withBadge((String) null);
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
        WhirlpoolApp.getInstance().trackEvent(StringConstants.ANALYTIC_RECYCLEVIEW_CLICK, "View Forum", "Opening Forum to view threads");
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
        prefetchCreateThreadPage();
    }

    @Override
    public void OnThreadClicked(int threadId, String threadTitle, int totalPage, int forumId) {
        if (preferencesGetter.getOpenLastPage())
            OnThreadClicked(threadId, threadTitle, totalPage, 0, totalPage, forumId);
        else
            OnThreadClicked(threadId, threadTitle, 1, 0, totalPage, forumId);
    }

    @Override
    public void OnThreadClicked(int threadId, String threadTitle, int lastPageRead, int lastReadId, int totalPage, int forumId) {
        WhirlpoolApp.getInstance().trackEvent(StringConstants.ANALYTIC_RECYCLEVIEW_CLICK, "View Thread", "Opening thread to view posts");

        if (ThreadScraper.isPublicForum(forumId))
            startPostViewPagerFragment(threadId, threadTitle, totalPage, lastPageRead, lastReadId);
        else {
            OnOpenWebVersionClicked(threadId, lastPageRead, lastReadId);
        }
    }

    @Override
    public void OnOpenWebVersionClicked(int threadId, int totalPage) {
        if (preferencesGetter.getOpenLastPage())
            OnOpenWebVersionClicked(threadId, totalPage, 0);
        else
            OnOpenWebVersionClicked(threadId, 1, 0);
    }

    @Override
    public void OnOpenWebVersionClicked(int threadId, int lastPageRead, int lastReadId) {

        if (mMainPresenter.IsThreadWatched(threadId) && preferencesGetter.isAutoMarkAsReadLastPage()) {
            mMainPresenter.MarkThreadAsRead(threadId);
        }

        final Uri parse = Uri.parse(StringConstants.THREAD_URL + String.valueOf(threadId) + "&p=" +
                String.valueOf(lastPageRead) + "&#r" + String.valueOf(lastReadId));
        launchCustomTab(parse);
    }

    private void launchCustomTab(Uri uri) {
        mCustomTabsActivityHelper.openCustomTabStandard(this, uri);
    }

    private void startPostViewPagerFragment(int threadId, String threadTitle, int totalPage, int page, int postLastRead) {
        ScrapedPostParentFragment scrapedPostParentFragment = ScrapedPostParentFragment.newInstance(threadId, threadTitle, page, postLastRead, totalPage);
        startFragment(scrapedPostParentFragment);
    }

    private void startPostViewPagerFragmentNoBackStack(int threadId, String threadTitle, int totalPage, int page, int postLastRead) {
        ScrapedPostParentFragment scrapedPostParentFragment = ScrapedPostParentFragment.newInstance(threadId, threadTitle, page, postLastRead, totalPage);
        startFragmentNoBackStack(scrapedPostParentFragment);
    }

    private void setUpWhimReplyFab(IndividualWhimFragment individualWhimFragment) {
        mSubscriptions.add(individualWhimFragment.OnFragmentDestroySubject.subscribe(aVoid ->
                fabReplyWhim.setVisibility(View.GONE)));
        mSubscriptions.add(individualWhimFragment.OnFragmentCreateViewSubject.subscribe(aVoid -> {
            resetFabLocationToBottom(fabReplyWhim);
            fabReplyWhim.setVisibility(View.VISIBLE);
        }));
    }

    private void setUpThreadCreateFab(PublishSubject<Void> createStream, PublishSubject<Void> destroyStream) {
        mSubscriptions.add(destroyStream.subscribe(aVoid ->
                fabCreateThread.setVisibility(View.GONE)));
        mSubscriptions.add(createStream.subscribe(aVoid -> {
            resetFabLocationToBottom(fabCreateThread);
            fabCreateThread.setVisibility(View.VISIBLE);
        }));
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

    @OnClick(R.id.fab_create_thread)
    public void launchCreateThreadInBrowser() {
        WhirlpoolApp.getInstance().trackEvent(StringConstants.ANALYTIC_FAB, "Create Thread", "");
        Uri parse = Uri.parse(StringConstants.NEW_THREAD_URL + String.valueOf(mForumId));
        launchCustomTab(parse);
    }

    @Override
    public void OnWhimClicked(int id, String message, String sender) {
        WhirlpoolApp.getInstance().trackEvent(StringConstants.ANALYTIC_RECYCLEVIEW_CLICK, "View Individual Whims", "");
        whimId = id;
        prefetchWhimReplyPage();
        IndividualWhimFragment individualWhimFragment = IndividualWhimFragment.newInstance(message, sender);
        setUpWhimReplyFab(individualWhimFragment);
        startFragment(individualWhimFragment);
    }

    private void prefetchCreateThreadPage() {
        Uri parse = Uri.parse(StringConstants.NEW_THREAD_URL + String.valueOf(mForumId));
        prefetchUrl(parse);
    }

    private void prefetchWhimReplyPage() {
        Uri parse = Uri.parse(StringConstants.WHIM_REPLY_URL + String.valueOf(whimId));
        prefetchUrl(parse);
    }

    private void prefetchUrl(Uri uri) {
        mCustomTabsActivityHelper.mayLaunchUrl(uri);
    }

    private void prefetchBundle(List<Bundle> bundle) {
        mCustomTabsActivityHelper.mayLaunchUrl(null, null, bundle);
    }

    @OnClick(R.id.fab_reply_whim)
    public void launchReplyWhimInBrowser() {
        WhirlpoolApp.getInstance().trackEvent(StringConstants.ANALYTIC_FAB, "Reply Whim", "");
        Uri parse = Uri.parse(StringConstants.WHIM_REPLY_URL + String.valueOf(whimId));
        launchCustomTab(parse);
    }

    @Override
    public void onSearchClicked(String query, int forumId, int groupId) {
        WhirlpoolApp.getInstance().trackEvent(StringConstants.ANALYTIC_SEARCH, "Search", "");
        final SearchResultThreadFragment searchResultThreadFragment = SearchResultThreadFragment.newInstance(query, forumId, groupId);
        startFragment(searchResultThreadFragment);
    }

    private void launchLinkInBrowser(String link) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse(link));
        startActivity(browserIntent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mCustomTabsActivityHelper.bindCustomTabsService(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mCustomTabsActivityHelper.unbindCustomTabsService(this);
    }

    private void decodeBitmap(final int resource) {
        mSubscriptions.add(CustomTabsActivityHelper.decodeBitmap(this, resource)
                .observeOn(schedulerManager.GetMainScheduler())
                .subscribeOn(schedulerManager.GetIoScheduler())
                .subscribe(new Subscriber<Bitmap>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("decodeBitmap", "There was a problem decoding the bitmap " + e);
                    }

                    @Override
                    public void onNext(Bitmap bitmap) {
                        if (resource == R.drawable.ic_arrow_back_sample) {
                            mCustomTabsActivityHelper.setCloseBitmap(bitmap);
                        }
                    }
                }));
    }
}
