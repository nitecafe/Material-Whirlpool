package com.android.nitecafe.whirlpoolnews.controllers;

import android.util.Log;

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
                        forumFragment.HideCenterProgressBar();
                        forumFragment.HideRefreshLoader();
                    }
                }, throwable -> {
                    if (forumFragment != null) {
                        forumFragment.DisplayErrorMessage();
                        forumFragment.HideCenterProgressBar();
                        forumFragment.HideRefreshLoader();
                    }
                    Log.e("forum get ",throwable.getMessage());
                });

    }

    public void attach(IForumFragment forumFragment) {
        this.forumFragment = forumFragment;
    }
}
