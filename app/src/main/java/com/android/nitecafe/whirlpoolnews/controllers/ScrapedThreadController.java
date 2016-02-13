package com.android.nitecafe.whirlpoolnews.controllers;

import com.android.nitecafe.whirlpoolnews.interfaces.IWhirlpoolRestClient;
import com.android.nitecafe.whirlpoolnews.scheduler.ISchedulerManager;
import com.android.nitecafe.whirlpoolnews.ui.interfaces.IScrapedThreadFragment;
import com.android.nitecafe.whirlpoolnews.utilities.IWatchedThreadIdentifier;

import javax.inject.Inject;
import javax.inject.Singleton;

public class ScrapedThreadController extends ThreadBaseController<IScrapedThreadFragment> {

    private IWhirlpoolRestClient whirlpoolRestClient;
    private ISchedulerManager schedulerManager;
    private IScrapedThreadFragment threadFragment;
    private int currentPage = 1;

    @Inject
    @Singleton
    public ScrapedThreadController(IWhirlpoolRestClient whirlpoolRestClient,
                                   ISchedulerManager schedulerManager,
                                   IWatchedThreadIdentifier watchedThreadIdentifier) {
        super(whirlpoolRestClient, schedulerManager, watchedThreadIdentifier);
        this.whirlpoolRestClient = whirlpoolRestClient;
        this.schedulerManager = schedulerManager;
    }

    public void GetScrapedThreads(int forumId, int groupId) {
        loadScrapedThreads(forumId, currentPage, groupId);
    }

    protected void loadScrapedThreads(int forumId, int pageNumber, int groupId) {
        whirlpoolRestClient.GetScrapedThreads(forumId, pageNumber, groupId)
                .observeOn(schedulerManager.GetMainScheduler())
                .subscribeOn(schedulerManager.GetIoScheduler())
                .subscribe(scrapedThreads -> {
                    if (threadFragment != null) {
                        threadFragment.DisplayThreads(scrapedThreads.getThreads());
                        HideAllProgressBar();
                    }
                }, throwable -> {
                    if (threadFragment != null) {
                        threadFragment.DisplayErrorMessage();
                        HideAllProgressBar();
                    }
                });
    }

    public void loadNextPage(int forumId, int groupId) {
        loadScrapedThreads(forumId, ++currentPage, groupId);
    }

    public void loadPreviousPage(int forumId, int groupId) {
        if (currentPage > 1)
            loadScrapedThreads(forumId, --currentPage, groupId);
    }

    private void HideAllProgressBar() {
        threadFragment.HideCenterProgressBar();
        threadFragment.HideRefreshLoader();
    }

    @Override
    public void Attach(IScrapedThreadFragment threadFragment) {
        super.Attach(threadFragment);
        this.threadFragment = threadFragment;
    }
}
