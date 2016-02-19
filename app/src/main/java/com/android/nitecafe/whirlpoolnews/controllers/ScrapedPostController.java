package com.android.nitecafe.whirlpoolnews.controllers;

import com.android.nitecafe.whirlpoolnews.ui.interfaces.IScrapedPostFragment;
import com.android.nitecafe.whirlpoolnews.utilities.IWatchedThreadIdentifier;
import com.android.nitecafe.whirlpoolnews.web.IWhirlpoolRestService;

import javax.inject.Inject;

public class ScrapedPostController extends ThreadBaseController<IScrapedPostFragment> {

    private IWhirlpoolRestService whirlpoolRestService;
    private IWatchedThreadIdentifier watchedThreadIdentifier;
    private IScrapedPostFragment postFragment;
    private int currentPage = 1;
    private int mPageCount;

    @Inject
    public ScrapedPostController(IWhirlpoolRestService whirlpoolRestService,
                                 IWatchedThreadIdentifier watchedThreadIdentifier) {
        super(whirlpoolRestService, watchedThreadIdentifier);
        this.whirlpoolRestService = whirlpoolRestService;
        this.watchedThreadIdentifier = watchedThreadIdentifier;
    }

    public void GetScrapedPosts(int threadId, int page) {

        if (page < 1 || threadId < 1)
            throw new IllegalArgumentException("Need valid thread id or page number");

        currentPage = page;
        loadScrapedPosts(threadId, currentPage);
    }

    public boolean IsThreadWatched(int threadId) {
        return watchedThreadIdentifier.isThreadWatched(threadId);
    }

    private void loadScrapedPosts(int threadId, int page) {
        whirlpoolRestService.GetScrapedPosts(threadId, page)
                .subscribe(posts -> {
                    mPageCount = posts.getPageCount();
                    if (postFragment != null) {
                        postFragment.DisplayPosts(posts.getScrapedPosts());
                        postFragment.SetTitle(posts.getThread_title());
                        postFragment.SetupPageSpinnerDropDown(mPageCount, page);
                        HideAllProgressBar();
                    }
                }, throwable -> {
                    if (postFragment != null) {
                        postFragment.DisplayErrorMessage();
                        HideAllProgressBar();
                    }
                });
    }

    public void GoToLastPage(int threadId, int lastPage) {
        GetScrapedPosts(threadId, lastPage);
    }

    public void GoToFirstPage(int threadId) {
        GetScrapedPosts(threadId, 1);
    }

    public void loadNextPage(int threadId) {
        if (IsAtLastPage())
            throw new IllegalArgumentException("Current page is the last page.");

        postFragment.ShowRefreshLoader();
        loadScrapedPosts(threadId, ++currentPage);
    }

    public boolean IsAtLastPage() {
        return currentPage == mPageCount;
    }

    public void loadPreviousPage(int threadId) {
        if (IsAtFirstPage())
            throw new IllegalArgumentException("Current page is the first page.");

        postFragment.ShowRefreshLoader();
        loadScrapedPosts(threadId, --currentPage);
    }

    public boolean IsAtFirstPage() {
        return currentPage == 1;
    }

    private void HideAllProgressBar() {
        postFragment.HideCenterProgressBar();
        postFragment.HideRefreshLoader();
    }

    public void attach(IScrapedPostFragment postFragment) {
        super.Attach(postFragment);
        this.postFragment = postFragment;
    }

    @Override
    protected void onUnwatchThreadSuccess() {
        super.onUnwatchThreadSuccess();
        if (postFragment != null)
            postFragment.setUpToolbarActionButtons();
    }

    @Override
    protected void OnWatchThreadSuccess() {
        super.OnWatchThreadSuccess();
        if (postFragment != null)
            postFragment.setUpToolbarActionButtons();
    }

}
