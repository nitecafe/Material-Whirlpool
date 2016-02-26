package com.android.nitecafe.whirlpoolnews.controllers;

import com.android.nitecafe.whirlpoolnews.ui.interfaces.IThreadActionMessageFragment;
import com.android.nitecafe.whirlpoolnews.web.interfaces.IWatchedThreadService;
import com.android.nitecafe.whirlpoolnews.web.interfaces.IWhirlpoolRestService;

public abstract class ThreadBaseController<T extends IThreadActionMessageFragment> {

    private IWhirlpoolRestService whirlpoolRestService;
    private IWatchedThreadService mWatchedThreadIdentifier;
    private IThreadActionMessageFragment mBaseFragment;

    public ThreadBaseController(IWhirlpoolRestService whirlpoolRestService,
                                IWatchedThreadService watchedThreadIdentifier) {
        this.whirlpoolRestService = whirlpoolRestService;
        mWatchedThreadIdentifier = watchedThreadIdentifier;
    }

    public void UnwatchThread(int threadId) {
        whirlpoolRestService.SetThreadAsUnwatch(threadId)
                .subscribe(
                        aVoid -> {
                            mWatchedThreadIdentifier.removeThreadFromWatch(threadId);
                            onUnwatchThreadSuccess();
                        },
                        throwable ->
                                onUnWatchThreadFailure()
                );
    }

    public void Attach(T baseFragment) {
        mBaseFragment = baseFragment;
    }

    public void MarkThreadAsRead(int threadId) {
        whirlpoolRestService.MarkThreadAsRead(threadId)
                .subscribe(aVoid -> {
                            onMarkThreadAsReadSuccess();
                        },
                        throwable ->
                                onMarkThreadAsReadFailure()
                );
    }

    public void MarkThreadAsReadNoNotification(int threadId) {
        whirlpoolRestService.MarkThreadAsRead(threadId)
                .subscribe(aVoid -> {
                        },
                        throwable ->
                                onMarkThreadAsReadFailure()
                );
    }

    public void WatchThread(int threadId) {
        whirlpoolRestService.SetThreadAsWatch(threadId)
                .subscribe(aVoid -> {
                            mWatchedThreadIdentifier.addThreadToWatch(threadId);
                            OnWatchThreadSuccess();
                        },
                        throwable ->
                                OnWatchThreadFailure()
                );
    }

    protected void onUnWatchThreadFailure() {
        if (mBaseFragment != null) mBaseFragment.ShowActionFailedMessage();
    }

    protected void onUnwatchThreadSuccess() {
        if (mBaseFragment != null) mBaseFragment.ShowThreadUnWatchedSuccessMessage();
    }

    protected void OnWatchThreadSuccess() {
        if (mBaseFragment != null) mBaseFragment.ShowThreadWatchedSuccessMessage();
    }

    protected void OnWatchThreadFailure() {
        if (mBaseFragment != null) mBaseFragment.ShowActionFailedMessage();
    }

    protected void onMarkThreadAsReadSuccess() {
        if (mBaseFragment != null) mBaseFragment.ShowMarkAsReadSuccessMessage();
    }

    protected void onMarkThreadAsReadFailure() {
        if (mBaseFragment != null) mBaseFragment.ShowActionFailedMessage();
    }
}
