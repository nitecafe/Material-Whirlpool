package com.android.nitecafe.whirlpoolnews.controllers;

import com.android.nitecafe.whirlpoolnews.constants.StringConstants;
import com.android.nitecafe.whirlpoolnews.interfaces.IWhirlpoolRestClient;
import com.android.nitecafe.whirlpoolnews.scheduler.ISchedulerManager;
import com.android.nitecafe.whirlpoolnews.ui.interfaces.IThreadFragment;

import javax.inject.Inject;
import javax.inject.Singleton;

public class ForumThreadController {

    private IWhirlpoolRestClient whirlpoolRestClient;
    private ISchedulerManager schedulerManager;
    private IThreadFragment threadFragment;

    @Inject
    @Singleton
    public ForumThreadController(IWhirlpoolRestClient whirlpoolRestClient, ISchedulerManager schedulerManager) {
        this.whirlpoolRestClient = whirlpoolRestClient;
        this.schedulerManager = schedulerManager;
    }

    public void GetThreads(int forumId) {
        whirlpoolRestClient.GetThreads(forumId, StringConstants.DEFAULT_THREAD_COUNT)
                .observeOn(schedulerManager.GetMainScheduler())
                .subscribeOn(schedulerManager.GetIoScheduler())
                .subscribe(threadLIst -> {
                    if (threadFragment != null) {
                        threadFragment.DisplayThreads(threadLIst.getTHREADS());
                        HideAllProgressBar();
                    }
                }, throwable -> {
                    if (threadFragment != null) {
                        threadFragment.DisplayErrorMessage();
                        HideAllProgressBar();
                    }
                });
    }

    private void HideAllProgressBar() {
        threadFragment.HideCenterProgressBar();
        threadFragment.HideRefreshLoader();
    }

    public void attach(IThreadFragment threadFragment) {
        this.threadFragment = threadFragment;
    }

}
