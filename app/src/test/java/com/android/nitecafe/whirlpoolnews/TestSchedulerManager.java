package com.android.nitecafe.whirlpoolnews;

import com.android.nitecafe.whirlpoolnews.scheduler.ISchedulerManager;

import rx.Scheduler;
import rx.schedulers.Schedulers;

/**
 * Created by grahamgoh on 31/01/16.
 */
public class TestSchedulerManager implements ISchedulerManager {
    @Override public Scheduler GetMainScheduler() {
        return Schedulers.io();
    }

    @Override public Scheduler GetIoScheduler() {
        return Schedulers.io();
    }
}
