package com.android.nitecafe.whirlpoolnews.controllers;

import com.android.nitecafe.whirlpoolnews.ui.interfaces.IPopularFragment;
import com.android.nitecafe.whirlpoolnews.utilities.IWatchedThreadIdentifier;
import com.android.nitecafe.whirlpoolnews.web.IWhirlpoolRestService;

import javax.inject.Inject;

public class PopularThreadsController extends ThreadBaseController<IPopularFragment> {

    private IWhirlpoolRestService whirlpoolRestService;
    private IPopularFragment popularFragment;

    @Inject
    public PopularThreadsController(IWhirlpoolRestService whirlpoolRestService,
                                    IWatchedThreadIdentifier watchedThreadIdentifier) {
        super(whirlpoolRestService, watchedThreadIdentifier);
        this.whirlpoolRestService = whirlpoolRestService;
    }

    public void GetPopularThreads() {
        whirlpoolRestService.GetPopularThreads()
                .subscribe(threadList -> {
                    if (popularFragment != null) popularFragment.DisplayPopularThreads(threadList);
                    HideAllProgressBar();
                }, throwable -> {
                    if (popularFragment != null) popularFragment.DisplayErrorMessage();
                    HideAllProgressBar();
                });
    }

    private void HideAllProgressBar() {
        if (popularFragment == null)
            return;

        popularFragment.HideCenterProgressBar();
    }

    @Override
    public void Attach(IPopularFragment view) {
        super.Attach(view);
        popularFragment = view;
    }
}


