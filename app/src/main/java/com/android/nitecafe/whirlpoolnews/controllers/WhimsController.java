package com.android.nitecafe.whirlpoolnews.controllers;

import android.util.Log;

import com.android.nitecafe.whirlpoolnews.interfaces.IWhirlpoolRestClient;
import com.android.nitecafe.whirlpoolnews.scheduler.ISchedulerManager;
import com.android.nitecafe.whirlpoolnews.ui.interfaces.IWhimsFragment;

import javax.inject.Inject;
import javax.inject.Singleton;

public class WhimsController {

    private IWhirlpoolRestClient whirlpoolRestClient;
    private ISchedulerManager schedulerManager;
    private IWhimsFragment whimsFragment;

    @Inject
    @Singleton
    public WhimsController(IWhirlpoolRestClient whirlpoolRestClient, ISchedulerManager schedulerManager) {
        this.whirlpoolRestClient = whirlpoolRestClient;
        this.schedulerManager = schedulerManager;
    }

    public void GetWhims() {
        whirlpoolRestClient.GetWhims()
                .observeOn(schedulerManager.GetMainScheduler())
                .subscribeOn(schedulerManager.GetIoScheduler())
                .subscribe(whimsList -> {
                    if (whimsFragment != null) {
                        whimsFragment.DisplayWhims(whimsList.getWHIMS());
                        whimsFragment.HideAllProgressLoader();
                    }
                }, throwable -> {
                    if (whimsFragment != null) {
                        whimsFragment.ShowErrorMessage();
                        whimsFragment.HideAllProgressLoader();
                    }

                });
    }

    public void Attach(IWhimsFragment whimsFragment) {
        this.whimsFragment = whimsFragment;
    }

    public void MarkWhimAsRead(int whimId) {
        whirlpoolRestClient.MarkThreadAsRead(whimId)
                .observeOn(schedulerManager.GetMainScheduler())
                .subscribeOn(schedulerManager.GetIoScheduler())
                .subscribe(aVoid -> {
                }, throwable -> {
                    Log.e("WhimsController", "Failed to load mark whims as read");
                });
    }
}
