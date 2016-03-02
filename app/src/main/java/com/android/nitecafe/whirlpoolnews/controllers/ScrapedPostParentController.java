package com.android.nitecafe.whirlpoolnews.controllers;

import com.android.nitecafe.whirlpoolnews.ui.interfaces.IScrapedPostParentFragment;
import com.android.nitecafe.whirlpoolnews.utilities.interfaces.IPreferencesGetter;
import com.android.nitecafe.whirlpoolnews.web.interfaces.IWatchedThreadService;
import com.android.nitecafe.whirlpoolnews.web.interfaces.IWhirlpoolRestService;

import javax.inject.Inject;

public class ScrapedPostParentController extends ThreadBaseController<IScrapedPostParentFragment> {

    private IWatchedThreadService mWatchedThreadIdentifier;
    private IPreferencesGetter preferencesGetter;
    private IScrapedPostParentFragment mFragment;

    @Inject
    public ScrapedPostParentController(IWhirlpoolRestService whirlpoolRestClient,
                                       IWatchedThreadService watchedThreadIdentifier,
                                       IPreferencesGetter preferencesGetter) {
        super(whirlpoolRestClient, watchedThreadIdentifier);
        mWatchedThreadIdentifier = watchedThreadIdentifier;
        this.preferencesGetter = preferencesGetter;
    }

    public boolean IsThreadWatched(int threadId) {
        return mWatchedThreadIdentifier.isThreadWatched(threadId);
    }

    @Override
    public void Attach(IScrapedPostParentFragment fragment) {
        mFragment = fragment;
        super.Attach(fragment);

    }

    @Override
    protected void onUnwatchThreadSuccess() {
        super.onUnwatchThreadSuccess();
        if (mFragment != null)
            mFragment.setUpToolbarActionButtons();
    }

    @Override
    protected void OnWatchThreadSuccess() {
        super.OnWatchThreadSuccess();
        if (mFragment != null)
            mFragment.setUpToolbarActionButtons();
    }

    public boolean shouldMarkThreadAsRead(int threadId) {
        return IsThreadWatched(threadId) && preferencesGetter.isAutoMarkAsReadLastPage();
    }
}
