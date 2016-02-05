package com.android.nitecafe.whirlpoolnews.controllers;

import com.android.nitecafe.whirlpoolnews.interfaces.IWhirlpoolRestClient;
import com.android.nitecafe.whirlpoolnews.scheduler.ISchedulerManager;
import com.android.nitecafe.whirlpoolnews.ui.interfaces.IForumFragment;

import javax.inject.Inject;
import javax.inject.Singleton;

public class ForumController {

    private ISchedulerManager schedulerManager;
    private IForumFragment forumFragment;
    private IWhirlpoolRestClient whirlpoolRestClient;

    @Inject
    @Singleton
    public ForumController(IWhirlpoolRestClient whirlpoolRestClient, ISchedulerManager schedulerManager) {
        this.whirlpoolRestClient = whirlpoolRestClient;
        this.schedulerManager = schedulerManager;
    }

    public void getForum() {
        whirlpoolRestClient.GetForum()
                .observeOn(schedulerManager.GetMainScheduler())
                .subscribeOn(schedulerManager.GetIoScheduler())
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
