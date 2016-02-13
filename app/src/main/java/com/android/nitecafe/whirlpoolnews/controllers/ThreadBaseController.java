package com.android.nitecafe.whirlpoolnews.controllers;

import com.android.nitecafe.whirlpoolnews.interfaces.IWhirlpoolRestClient;
import com.android.nitecafe.whirlpoolnews.scheduler.ISchedulerManager;
import com.android.nitecafe.whirlpoolnews.ui.fragments.IThreadActionMessageFragment;
import com.android.nitecafe.whirlpoolnews.utilities.IWatchedThreadIdentifier;

public abstract class ThreadBaseController<T extends IThreadActionMessageFragment> {

    private IWhirlpoolRestClient mWhirlpoolRestClient;
    private ISchedulerManager mSchedulerManager;
    private IWatchedThreadIdentifier mWatchedThreadIdentifier;
    private IThreadActionMessageFragment mBaseFragment;

    public ThreadBaseController(IWhirlpoolRestClient whirlpoolRestClient,
                                ISchedulerManager schedulerManager,
                                IWatchedThreadIdentifier watchedThreadIdentifier) {
        mWhirlpoolRestClient = whirlpoolRestClient;
        mSchedulerManager = schedulerManager;
        mWatchedThreadIdentifier = watchedThreadIdentifier;
    }

    public void UnwatchThread(int threadId) {
        mWhirlpoolRestClient.SetThreadAsUnwatch(threadId)
                .observeOn(mSchedulerManager.GetMainScheduler())
                .subscribeOn(mSchedulerManager.GetIoScheduler())
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
        mWhirlpoolRestClient.MarkThreadAsRead(threadId)
                .observeOn(mSchedulerManager.GetMainScheduler())
                .subscribeOn(mSchedulerManager.GetIoScheduler())
                .subscribe(aVoid -> {
                            onMarkThreadAsReadSuccess();
                        },
                        throwable ->
                                onMarkThreadAsReadFailure()
                );
    }

    public void WatchThread(int threadId) {
        mWhirlpoolRestClient.SetThreadAsWatch(threadId)
                .observeOn(mSchedulerManager.GetMainScheduler())
                .subscribeOn(mSchedulerManager.GetIoScheduler())
                .subscribe(aVoid -> {
                            mWatchedThreadIdentifier.addThreadToWatch(threadId);
                            OnWatchThreadSuccess();
                        },
                        throwable ->
                                OnWatchThreadFailure()
                );
    }

    protected void onUnWatchThreadFailure() {
        mBaseFragment.ShowActionFailedMessage();
    }

    protected void onUnwatchThreadSuccess() {
        mBaseFragment.ShowThreadUnWatchedSuccessMessage();
    }

    protected void OnWatchThreadSuccess() {
        mBaseFragment.ShowThreadWatchedSuccessMessage();
    }

    protected void OnWatchThreadFailure() {
        mBaseFragment.ShowActionFailedMessage();
    }

    protected void onMarkThreadAsReadSuccess() {
        mBaseFragment.ShowMarkAsReadSuccessMessage();
    }

    protected void onMarkThreadAsReadFailure() {
        mBaseFragment.ShowActionFailedMessage();
    }
}
