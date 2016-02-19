package com.android.nitecafe.whirlpoolnews.controllers;

import com.android.nitecafe.whirlpoolnews.ui.interfaces.IRecentFragment;
import com.android.nitecafe.whirlpoolnews.web.interfaces.IWatchedThreadService;
import com.android.nitecafe.whirlpoolnews.web.interfaces.IWhirlpoolRestService;

import javax.inject.Inject;

public class RecentController extends ThreadBaseController<IRecentFragment> {

    private IWhirlpoolRestService whirlpoolRestService;
    private IRecentFragment recentFragment;

    @Inject
    public RecentController(IWhirlpoolRestService whirlpoolRestService,
                            IWatchedThreadService watchedThreadIdentifier) {
        super(whirlpoolRestService, watchedThreadIdentifier);
        this.whirlpoolRestService = whirlpoolRestService;
    }

    public void GetRecent() {
        whirlpoolRestService.GetRecent()
                .subscribe(recentList -> {
                    if (recentFragment != null) {
                        recentFragment.DisplayRecent(recentList.getRECENT());
                        HideAllProgressBar();
                    }
                }, throwable -> {
                    if (recentFragment != null) {
                        recentFragment.DisplayErrorMessage();
                        HideAllProgressBar();
                    }
                });
    }

    private void HideAllProgressBar() {
        recentFragment.HideCenterProgressBar();
        recentFragment.HideRefreshLoader();
    }

    @Override
    public void Attach(IRecentFragment recentFragment) {
        super.Attach(recentFragment);
        this.recentFragment = recentFragment;
    }

}
