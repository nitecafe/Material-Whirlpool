package com.android.nitecafe.whirlpoolnews.controllers;

import android.util.Log;

import com.android.nitecafe.whirlpoolnews.ui.interfaces.IWhimsFragment;
import com.android.nitecafe.whirlpoolnews.web.interfaces.IWhirlpoolRestService;

import javax.inject.Inject;
import javax.inject.Named;

import rx.subjects.PublishSubject;

public class WhimsController {

    private IWhirlpoolRestService whirlpoolRestService;
    private PublishSubject<Void> whimSubject;
    private IWhimsFragment whimsFragment;

    @Inject
    public WhimsController(IWhirlpoolRestService whirlpoolRestService,
                           @Named("whim") PublishSubject<Void> whimSubject) {
        this.whirlpoolRestService = whirlpoolRestService;
        this.whimSubject = whimSubject;
    }

    public void GetWhims() {
        whirlpoolRestService.GetWhims()
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
        whirlpoolRestService.MarkWhimAsRead(whimId)
                .subscribe(aVoid -> {
                    whimSubject.onNext(null);
                }, throwable -> {
                    Log.e("WhimsController", "Failed to load mark whims as read");
                });
    }
}
