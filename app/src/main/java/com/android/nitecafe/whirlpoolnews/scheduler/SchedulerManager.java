package com.android.nitecafe.whirlpoolnews.scheduler;

import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SchedulerManager implements ISchedulerManager {

    @Override public Scheduler GetMainScheduler(){
        return AndroidSchedulers.mainThread();
    }

    @Override public Scheduler GetIoScheduler(){
        return Schedulers.io();
    }
}
