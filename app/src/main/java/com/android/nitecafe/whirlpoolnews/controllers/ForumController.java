package com.android.nitecafe.whirlpoolnews.controllers;

import com.android.nitecafe.whirlpoolnews.ui.interfaces.IForumFragment;
import com.android.nitecafe.whirlpoolnews.web.interfaces.IWhirlpoolRestService;

import javax.inject.Inject;

public class ForumController {

    private IForumFragment forumFragment;
    private IWhirlpoolRestService whirlpoolRestService;

    @Inject
    public ForumController(IWhirlpoolRestService whirlpoolRestClient) {
        this.whirlpoolRestService = whirlpoolRestClient;
    }

    public void getForum() {
        whirlpoolRestService.GetForum()
                .subscribe(forumList -> {
                    if (forumFragment != null) {
                        forumFragment.DisplayForum(forumList.getFORUM());
                        hideAllLoader();
                    }
                }, throwable -> {
                    if (forumFragment != null) {
                        forumFragment.DisplayErrorMessage();
                        hideAllLoader();
                    }
                });
    }

    private void hideAllLoader() {
        forumFragment.HideCenterProgressBar();
        forumFragment.HideRefreshLoader();
    }

    public void attach(IForumFragment forumFragment) {
        this.forumFragment = forumFragment;
    }
}
