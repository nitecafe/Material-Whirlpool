package com.android.nitecafe.whirlpoolnews.controllers;

import com.android.nitecafe.whirlpoolnews.models.PostBookmark;
import com.android.nitecafe.whirlpoolnews.services.IPostBookmarkService;
import com.android.nitecafe.whirlpoolnews.ui.interfaces.IPostBookmarkFragment;

import java.util.List;

import javax.inject.Inject;

public class PostBookmarkController {

    private IPostBookmarkFragment postBookmarkFragment;
    private IPostBookmarkService postBookmarkService;

    @Inject
    public PostBookmarkController(IPostBookmarkService postBookmarkService) {
        this.postBookmarkService = postBookmarkService;
    }

    public void Attach(IPostBookmarkFragment postBookmarkFragment) {
        this.postBookmarkFragment = postBookmarkFragment;
    }

    public void GetPostBookmarks() {
        List<PostBookmark> postBookmarks = postBookmarkService.getPostBookmarks();
        if (postBookmarkFragment != null) {
            postBookmarkFragment.DisplayPostBookmarks(postBookmarks);
            postBookmarkFragment.HideCenterProgressBar();
        }
    }

    public void RemovePostBookmark(int postId) {
        postBookmarkService.removePostBookmark(postId);
        GetPostBookmarks();
        if (postBookmarkFragment != null) {
            postBookmarkFragment.DisplayPostBookmarkRemovedMessage();
        }
    }
}
