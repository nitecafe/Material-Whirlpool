package com.android.nitecafe.whirlpoolnews.controllers;

import com.android.nitecafe.whirlpoolnews.models.PostBookmark;
import com.android.nitecafe.whirlpoolnews.services.IPostBookmarkService;
import com.android.nitecafe.whirlpoolnews.ui.interfaces.IScrapedPostChildFragment;
import com.android.nitecafe.whirlpoolnews.utilities.WhirlpoolExceptionHandler;
import com.android.nitecafe.whirlpoolnews.utilities.interfaces.IPreferencesGetter;
import com.android.nitecafe.whirlpoolnews.web.interfaces.IWatchedThreadService;
import com.android.nitecafe.whirlpoolnews.web.interfaces.IWhirlpoolRestService;

import javax.inject.Inject;

public class ScrapedPostChildController extends ThreadBaseController<IScrapedPostChildFragment> {

    private IWhirlpoolRestService whirlpoolRestClient;
    private IPostBookmarkService mPostBookmarkService;
    private IPreferencesGetter preferencesGetter;
    private IScrapedPostChildFragment postFragment;

    @Inject
    public ScrapedPostChildController(IWhirlpoolRestService whirlpoolRestClient,
                                      IWatchedThreadService watchedThreadIdentifier,
                                      IPostBookmarkService postBookmarkService, IPreferencesGetter preferencesGetter) {
        super(whirlpoolRestClient, watchedThreadIdentifier);
        this.whirlpoolRestClient = whirlpoolRestClient;
        mPostBookmarkService = postBookmarkService;
        this.preferencesGetter = preferencesGetter;
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
                        if (WhirlpoolExceptionHandler.isPrivateForumException(throwable))
                            postFragment.LaunchThreadInBrowser();
                        else {
                            postFragment.DisplayErrorMessage();
                        }
                        postFragment.HideCenterProgressBar();
                    }
                });
    }

    public void attach(IScrapedPostChildFragment postFragment) {
        super.Attach(postFragment);
        this.postFragment = postFragment;
    }

    public void addToPostBookmark(PostBookmark bookmark) {
        mPostBookmarkService.addPostBookmark(bookmark);
        if (postFragment != null)
            postFragment.showAddedToBookmarkMessage();
    }

    public boolean isABookmark(int postId) {
        return mPostBookmarkService.isABookmark(postId);
    }

    public void removeFromBookmark(int postId) {
        mPostBookmarkService.removePostBookmark(postId);

        if (postFragment != null)
            postFragment.showRemoveFromBookmarkMessage();
    }

    public boolean isDarkTheme() {
        return preferencesGetter.isDarkThemeOn();
    }
}
