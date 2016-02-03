package com.android.nitecafe.whirlpoolnews.controllers;

import com.android.nitecafe.whirlpoolnews.interfaces.IWhirlpoolService;
import com.android.nitecafe.whirlpoolnews.scheduler.ISchedulerManager;
import com.android.nitecafe.whirlpoolnews.ui.IForumFragment;

import javax.inject.Inject;
import javax.inject.Singleton;

public class ForumController {

    private IWhirlpoolService mWhirlpoolService;
    private ISchedulerManager schedulerManager;
    private IForumFragment forumFragment;

    @Inject
    @Singleton
    public ForumController(IWhirlpoolService whirlpoolServiceMock, ISchedulerManager schedulerManager) {
        mWhirlpoolService = whirlpoolServiceMock;
        this.schedulerManager = schedulerManager;
    }

    public void getForum() {
        mWhirlpoolService.GetForum()
                .observeOn(schedulerManager.GetMainScheduler())
                .subscribeOn(schedulerManager.GetIoScheduler())
                .subscribe(forumList -> {
                    if (forumFragment != null) {
                        forumFragment.DisplayForum(forumList.getFORUM());
                        forumFragment.HideCenterProgressBar();
                    }
                }, throwable -> {
                    if (forumFragment != null) {
                        forumFragment.DisplayErrorMessage();
                        forumFragment.HideCenterProgressBar();
                    }
                });

    }

    public void attach(IForumFragment forumFragment) {
        this.forumFragment = forumFragment;
    }
}
