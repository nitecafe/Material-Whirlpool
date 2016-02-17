package com.android.nitecafe.whirlpoolnews.controllers;

import android.util.Log;

import com.android.nitecafe.whirlpoolnews.interfaces.IWhirlpoolRestClient;
import com.android.nitecafe.whirlpoolnews.scheduler.ISchedulerManager;
import com.android.nitecafe.whirlpoolnews.ui.interfaces.IWhimsFragment;

import javax.inject.Inject;
import javax.inject.Named;

import rx.subjects.PublishSubject;

public class WhimsController {

    private IWhirlpoolRestClient whirlpoolRestClient;
    private ISchedulerManager schedulerManager;
    private PublishSubject<Void> whimSubject;
    private IWhimsFragment whimsFragment;

    @Inject
    public WhimsController(IWhirlpoolRestClient whirlpoolRestClient,
                           ISchedulerManager schedulerManager,
                           @Named("whim") PublishSubject<Void> whimSubject) {
        this.whirlpoolRestClient = whirlpoolRestClient;
        this.schedulerManager = schedulerManager;
        this.whimSubject = whimSubject;
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
        whirlpoolRestClient.MarkWhimAsRead(whimId)
                .observeOn(schedulerManager.GetMainScheduler())
                .subscribeOn(schedulerManager.GetIoScheduler())
                .subscribe(aVoid -> {
                    whimSubject.onNext(null);
                }, throwable -> {
                    Log.e("WhimsController", "Failed to load mark whims as read");
                });
    }
}
