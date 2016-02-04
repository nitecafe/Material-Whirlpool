package com.android.nitecafe.whirlpoolnews;

import com.android.nitecafe.whirlpoolnews.scheduler.ISchedulerManager;

import rx.Scheduler;
import rx.schedulers.Schedulers;
import rx.schedulers.TestScheduler;

public class TestSchedulerManager implements ISchedulerManager {

    public TestScheduler testScheduler = new TestScheduler();

    @Override public Scheduler GetMainScheduler() {
        return testScheduler;
    }

    @Override public Scheduler GetIoScheduler() {
        return testScheduler;
    }
}
