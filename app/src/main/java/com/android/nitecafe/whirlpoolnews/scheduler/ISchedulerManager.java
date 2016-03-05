package com.android.nitecafe.whirlpoolnews.scheduler;

import rx.Scheduler;

public interface ISchedulerManager {
    Scheduler GetMainScheduler();

    Scheduler GetIoScheduler();
}
