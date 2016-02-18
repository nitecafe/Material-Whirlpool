package com.android.nitecafe.whirlpoolnews.controllers;

import com.android.nitecafe.whirlpoolnews.interfaces.IWhirlpoolRestClient;
import com.android.nitecafe.whirlpoolnews.scheduler.ISchedulerManager;
import com.android.nitecafe.whirlpoolnews.ui.interfaces.ISearchFragment;

import javax.inject.Inject;

public class SearchController {

    private ISearchFragment mSearchFragment;
    private IWhirlpoolRestClient whirlpoolRestClient;
    private ISchedulerManager mSchedulerManager;

    @Inject
    public SearchController(IWhirlpoolRestClient whirlpoolRestClient, ISchedulerManager schedulerManager) {
        this.whirlpoolRestClient = whirlpoolRestClient;
        mSchedulerManager = schedulerManager;
    }

    public void attachedView(ISearchFragment view) {
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
