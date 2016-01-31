package com.android.nitecafe.whirlpoolnews.scheduler;

import rx.Scheduler;

/**
 * Created by grahamgoh on 31/01/16.
 */
public interface ISchedulerManager {
    Scheduler GetMainScheduler();

    Scheduler GetIoScheduler();
}
