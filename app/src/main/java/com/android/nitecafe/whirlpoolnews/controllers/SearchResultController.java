package com.android.nitecafe.whirlpoolnews.controllers;

import com.android.nitecafe.whirlpoolnews.interfaces.IWhirlpoolRestClient;
import com.android.nitecafe.whirlpoolnews.scheduler.ISchedulerManager;
import com.android.nitecafe.whirlpoolnews.ui.interfaces.ISearchResultFragment;
import com.android.nitecafe.whirlpoolnews.ui.interfaces.IThreadActionMessageFragment;
import com.android.nitecafe.whirlpoolnews.utilities.IWatchedThreadIdentifier;

import javax.inject.Inject;

public class SearchResultController extends ThreadBaseController<IThreadActionMessageFragment> {

    private ISearchResultFragment mSearchFragment;
    private IWhirlpoolRestClient whirlpoolRestClient;
    private ISchedulerManager mSchedulerManager;

    @Inject
    public SearchResultController(IWhirlpoolRestClient whirlpoolRestClient, ISchedulerManager schedulerManager, IWatchedThreadIdentifier watchedThreadIdentifier) {
        super(whirlpoolRestClient, schedulerManager, watchedThreadIdentifier);
        this.whirlpoolRestClient = whirlpoolRestClient;
        mSchedulerManager = schedulerManager;
    }

    public void Attach(ISearchResultFragment view) {
        super.Attach(view);
        mSearchFragment = view;
    }

    public void Search(String query, int forumId, int groupId) {
        whirlpoolRestClient.SearchThreads(forumId, groupId, query)
                .observeOn(mSchedulerManager.GetMainScheduler())
                .subscribeOn(mSchedulerManager.GetIoScheduler())
                .subscribe(scrapedThreads -> {
                    if (mSearchFragment != null) {
                        mSearchFragment.DisplaySearchResults(scrapedThreads);
                        mSearchFragment.HideSearchProgressBar();
                    }
                }, throwable -> {
                    mSearchFragment.ShowActionFailedMessage();
                    mSearchFragment.HideSearchProgressBar();
                });
    }
}
