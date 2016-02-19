package com.android.nitecafe.whirlpoolnews.controllers;

import com.android.nitecafe.whirlpoolnews.ui.interfaces.IWatchedFragment;
import com.android.nitecafe.whirlpoolnews.utilities.IWatchedThreadIdentifier;
import com.android.nitecafe.whirlpoolnews.web.IWhirlpoolRestService;

import javax.inject.Inject;

public class WatchedController extends ThreadBaseController<IWatchedFragment> {

    private IWhirlpoolRestService whirlpoolRestService;
    private IWatchedFragment watchedFragment;

    @Inject
    public WatchedController(IWhirlpoolRestService whirlpoolRestService,
                             IWatchedThreadIdentifier watchedThreadIdentifier) {
        super(whirlpoolRestService, watchedThreadIdentifier);
        this.whirlpoolRestService = whirlpoolRestService;
    }

    @Override
    public void Attach(IWatchedFragment baseFragment) {
        super.Attach(baseFragment);
        this.watchedFragment = baseFragment;
    }

    public void GetUnreadWatched() {
        whirlpoolRestService.GetUnreadWatched()
                .subscribe(watchedList -> {
                    if (watchedFragment != null) {
                        watchedFragment.DisplayWatched(watchedList.getWATCHED());
                        HideAllProgressBar();
                    }
                }, throwable -> {
                    if (watchedFragment != null) {
                        watchedFragment.DisplayErrorMessage();
                        HideAllProgressBar();
                    }
                });
    }

    public void GetAllWatched() {
        whirlpoolRestService.GetAllWatched()
                .subscribe(watchedList -> {
                    if (watchedFragment != null) {
                        watchedFragment.DisplayWatched(watchedList.getWATCHED());
                        HideAllProgressBar();
                    }
                }, throwable -> {
                    if (watchedFragment != null) {
                        watchedFragment.DisplayErrorMessage();
                        HideAllProgressBar();
                    }
                });
    }

    @Override
    protected void onUnwatchThreadSuccess() {
        super.onUnwatchThreadSuccess();
        if (watchedFragment != null)
            watchedFragment.loadWatched();
    }

    @Override
    protected void onMarkThreadAsReadSuccess() {
        super.onMarkThreadAsReadSuccess();
        if (watchedFragment != null)
            watchedFragment.loadWatched();
    }

    private void HideAllProgressBar() {
        watchedFragment.HideCenterProgressBar();
        watchedFragment.HideRefreshLoader();
    }
}
