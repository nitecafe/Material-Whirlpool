package com.android.nitecafe.whirlpoolnews.controllers;

import com.android.nitecafe.whirlpoolnews.interfaces.IWhirlpoolRestClient;
import com.android.nitecafe.whirlpoolnews.scheduler.ISchedulerManager;
import com.android.nitecafe.whirlpoolnews.ui.interfaces.IScrapedThreadFragment;
import com.android.nitecafe.whirlpoolnews.utilities.IWatchedThreadIdentifier;

import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ScrapedThreadController extends ThreadBaseController<IScrapedThreadFragment> {

    private IWhirlpoolRestClient whirlpoolRestClient;
    private ISchedulerManager schedulerManager;
    private IScrapedThreadFragment threadFragment;
    private int currentPage = 1;
    private int mPageCount;
    private Map<String, Integer> groups;

    @Inject
    public ScrapedThreadController(IWhirlpoolRestClient whirlpoolRestClient,
                                   ISchedulerManager schedulerManager,
                                   IWatchedThreadIdentifier watchedThreadIdentifier) {
        super(whirlpoolRestClient, schedulerManager, watchedThreadIdentifier);
        this.whirlpoolRestClient = whirlpoolRestClient;
        this.schedulerManager = schedulerManager;
    }

    public int getTotalPage() {
        return mPageCount;
    }

    public void GetScrapedThreads(int forumId, int groupId) {

        if (forumId < 1)
            throw new IllegalArgumentException("Need valid thread id or groupId");

        GetScrapedThreads(forumId, currentPage, groupId);
    }

    public void GetScrapedThreads(int forumId, int pageNumber, int groupId) {
        whirlpoolRestClient.GetScrapedThreads(forumId, pageNumber, groupId)
                .observeOn(schedulerManager.GetMainScheduler())
                .subscribeOn(schedulerManager.GetIoScheduler())
                .subscribe(scrapedThreads -> {
                    mPageCount = scrapedThreads.getPageCount();
                    if (threadFragment != null) {
                        threadFragment.DisplayThreads(scrapedThreads.getThreads());
                        threadFragment.SetupPageSpinnerDropDown(mPageCount, pageNumber);
                        saveGroups(scrapedThreads.getGroups());
                        threadFragment.SetupGroupSpinnerDropDown(groups, groupId);
                        currentPage = pageNumber;
                        HideAllProgressBar();
                    }
                }, throwable -> {
                    if (threadFragment != null) {
                        threadFragment.DisplayErrorMessage();
                        HideAllProgressBar();
                    }
                });
    }

    // keep copy of groups for when configuration changes
    private void saveGroups(Map<String, Integer> threadGroup) {
        if (threadGroup.size() > 0)
            groups = threadGroup;
    }

    public void loadNextPage(int forumId, int groupId) {
        if (IsAtLastPage())
            throw new IllegalArgumentException("Current page is the last page.");

        threadFragment.ShowRefreshLoader();
        GetScrapedThreads(forumId, ++currentPage, groupId);
    }

    public void loadPreviousPage(int forumId, int groupId) {
        if (IsAtFirstPage())
            throw new IllegalArgumentException("Current page is the first page.");

        threadFragment.ShowRefreshLoader();
        GetScrapedThreads(forumId, --currentPage, groupId);
    }

    public boolean IsAtLastPage() {
        return currentPage == mPageCount;
    }

    public boolean IsAtFirstPage() {
        return currentPage == 1;
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
