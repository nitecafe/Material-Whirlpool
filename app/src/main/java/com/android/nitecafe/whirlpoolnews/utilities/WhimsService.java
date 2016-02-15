package com.android.nitecafe.whirlpoolnews.utilities;

import com.android.nitecafe.whirlpoolnews.interfaces.IWhirlpoolRestClient;
import com.android.nitecafe.whirlpoolnews.scheduler.ISchedulerManager;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;

public class WhimsService {

    private IWhirlpoolRestClient whirlpoolRestClient;
    private ISchedulerManager schedulerManager;

    @Inject
    @Singleton
    public WhimsService(IWhirlpoolRestClient whirlpoolRestClient, ISchedulerManager schedulerManager) {
        this.whirlpoolRestClient = whirlpoolRestClient;
        this.schedulerManager = schedulerManager;
    }

    public Observable<Integer> GetNumberOfUnreadWhims() {
        return whirlpoolRestClient.GetWhims()
                .observeOn(schedulerManager.GetMainScheduler())
                .subscribeOn(schedulerManager.GetIoScheduler())
                .map(whimsList -> whimsList.getWHIMS())
                .flatMap(whims -> Observable.from(whims))
                .map(whim -> whim.getVIEWED())
                .filter(integer -> integer == 0)
                .count();
    }
}
