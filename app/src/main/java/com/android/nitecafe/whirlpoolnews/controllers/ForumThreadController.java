package com.android.nitecafe.whirlpoolnews.controllers;

import com.android.nitecafe.whirlpoolnews.constants.StringConstants;
import com.android.nitecafe.whirlpoolnews.ui.interfaces.IThreadFragment;
import com.android.nitecafe.whirlpoolnews.web.interfaces.IWatchedThreadService;
import com.android.nitecafe.whirlpoolnews.web.interfaces.IWhirlpoolRestService;

import javax.inject.Inject;

/**
 * For controlling threads obtained from the API, not the scrapped threads
 */
public class ForumThreadController extends ThreadBaseController<IThreadFragment> {

    private IWhirlpoolRestService whirlpoolRestService;
    private IThreadFragment threadFragment;

    @Inject
    public ForumThreadController(IWhirlpoolRestService whirlpoolRestService,
                                 IWatchedThreadService watchedThreadIdentifier) {
        super(whirlpoolRestService, watchedThreadIdentifier);
        this.whirlpoolRestService = whirlpoolRestService;
    }

    public void GetThreads(int forumId) {
        whirlpoolRestService.GetThreads(forumId, StringConstants.DEFAULT_THREAD_COUNT)
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

    @Override
    public void Attach(IThreadFragment threadFragment) {
        super.Attach(threadFragment);
        this.threadFragment = threadFragment;
    }

}
