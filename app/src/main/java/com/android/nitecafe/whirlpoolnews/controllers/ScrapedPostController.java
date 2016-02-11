package com.android.nitecafe.whirlpoolnews.controllers;

import com.android.nitecafe.whirlpoolnews.interfaces.IWhirlpoolRestClient;
import com.android.nitecafe.whirlpoolnews.scheduler.ISchedulerManager;
import com.android.nitecafe.whirlpoolnews.ui.interfaces.IScrapedPostFragment;
import com.android.nitecafe.whirlpoolnews.ui.fragments.ScrapedPostFragment;

import javax.inject.Inject;
import javax.inject.Singleton;

public class ScrapedPostController {

    private IWhirlpoolRestClient whirlpoolRestClient;
    private ISchedulerManager schedulerManager;
    private IScrapedPostFragment postFragment;
    private int currentPage = 1;

    @Inject
    @Singleton
    public ScrapedPostController(IWhirlpoolRestClient whirlpoolRestClient, ISchedulerManager schedulerManager) {
        this.whirlpoolRestClient = whirlpoolRestClient;
        this.schedulerManager = schedulerManager;
    }

    public void GetScrapedPosts(int threadId, int page) {
        currentPage = page;
        loadScrapedPosts(threadId, currentPage);
    }

    private void loadScrapedPosts(int threadId, int page) {
        whirlpoolRestClient.GetScrapedPosts(threadId, page)
                .observeOn(schedulerManager.GetMainScheduler())
                .subscribeOn(schedulerManager.GetIoScheduler())
                .subscribe(posts -> {
                    if (postFragment != null) {
                        postFragment.DisplayPosts(posts.getScrapedPosts());
                        HideAllProgressBar();
                    }
                }, throwable -> {
                    if (postFragment != null) {
                        postFragment.DisplayErrorMessage();
                        HideAllProgressBar();
                    }
                });
    }

    public void loadNextPage(int threadId) {
        loadScrapedPosts(threadId, ++currentPage);
    }

    public void loadPreviousPage(int threadId) {
        if (currentPage > 1)
            loadScrapedPosts(threadId, --currentPage);
    }

    private void HideAllProgressBar() {
        postFragment.HideCenterProgressBar();
        postFragment.HideRefreshLoader();
    }

    public void attach(ScrapedPostFragment postFragment) {
        this.postFragment = postFragment;
    }

}
