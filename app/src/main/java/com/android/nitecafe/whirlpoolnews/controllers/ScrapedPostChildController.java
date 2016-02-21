package com.android.nitecafe.whirlpoolnews.controllers;

import com.android.nitecafe.whirlpoolnews.ui.interfaces.IScrapedPostChildFragment;
import com.android.nitecafe.whirlpoolnews.web.interfaces.IWatchedThreadService;
import com.android.nitecafe.whirlpoolnews.web.interfaces.IWhirlpoolRestService;

import javax.inject.Inject;

public class ScrapedPostChildController extends ThreadBaseController<IScrapedPostChildFragment> {

    private IWhirlpoolRestService whirlpoolRestClient;
    private IScrapedPostChildFragment postFragment;

    @Inject
    public ScrapedPostChildController(IWhirlpoolRestService whirlpoolRestClient,
                                      IWatchedThreadService watchedThreadIdentifier) {
        super(whirlpoolRestClient, watchedThreadIdentifier);
        this.whirlpoolRestClient = whirlpoolRestClient;
    }

    public void GetScrapedPosts(int threadId, int page) {

        // -1 is last page
        if (page < -1 || threadId < 1)
            throw new IllegalArgumentException("Need valid thread id or page number");

        loadScrapedPosts(threadId, page);
    }

    private void loadScrapedPosts(int threadId, int page) {
        whirlpoolRestClient.GetScrapedPosts(threadId, page)
                .subscribe(posts -> {
                    if (postFragment != null) {
                        postFragment.DisplayPosts(posts.getScrapedPosts());
                        postFragment.SetTitle(posts.getThread_title());
                        postFragment.HideCenterProgressBar();
                        postFragment.UpdatePageCount(posts.getPageCount());
                    }
                }, throwable -> {
                    if (postFragment != null) {
                        postFragment.DisplayErrorMessage();
                        postFragment.HideCenterProgressBar();
                    }
                });
    }

    public void attach(IScrapedPostChildFragment postFragment) {
        super.Attach(postFragment);
        this.postFragment = postFragment;
    }
}
