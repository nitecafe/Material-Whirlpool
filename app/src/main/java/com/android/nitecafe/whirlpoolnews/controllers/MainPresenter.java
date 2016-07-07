package com.android.nitecafe.whirlpoolnews.controllers;

import android.util.Log;

import com.android.nitecafe.whirlpoolnews.web.interfaces.IWatchedThreadService;
import com.android.nitecafe.whirlpoolnews.web.interfaces.IWhirlpoolRestService;

import javax.inject.Inject;

public class MainPresenter {

    private final IWhirlpoolRestService whirlpoolRestService;
    private final IWatchedThreadService watchedThreadIdentifier;

    @Inject
    public MainPresenter(IWhirlpoolRestService whirlpoolRestService,
                         IWatchedThreadService watchedThreadIdentifier) {
        this.whirlpoolRestService = whirlpoolRestService;
        this.watchedThreadIdentifier = watchedThreadIdentifier;
    }

    public void MarkThreadAsRead(int threadId) {
        whirlpoolRestService
                .MarkThreadAsRead(threadId)
                .subscribe(aVoid -> {
                    Log.d("Main", "Marked as read");
                }, throwable -> {
                    Log.d("Main", "Failed to mark as read");
                });
    }

    public boolean IsThreadWatched(int threadId) {
        return watchedThreadIdentifier.isThreadWatched(threadId);
    }
}
