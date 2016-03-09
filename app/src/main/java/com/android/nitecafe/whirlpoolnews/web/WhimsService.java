package com.android.nitecafe.whirlpoolnews.web;

import com.android.nitecafe.whirlpoolnews.models.Whim;
import com.android.nitecafe.whirlpoolnews.scheduler.ISchedulerManager;
import com.android.nitecafe.whirlpoolnews.utilities.WhirlpoolDateUtils;
import com.android.nitecafe.whirlpoolnews.web.interfaces.IWhirlpoolRestClient;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;

/**
 * At the start of the app, check how many unread whims
 * and display it as a badge on the navigation drawer.
 */
@Singleton
public class WhimsService implements IWhimsService {

    private IWhirlpoolRestClient whirlpoolRestClient;
    private ISchedulerManager schedulerManager;

    @Inject
    public WhimsService(IWhirlpoolRestClient whirlpoolRestClient, ISchedulerManager schedulerManager) {
        this.whirlpoolRestClient = whirlpoolRestClient;
        this.schedulerManager = schedulerManager;
    }

    @Override public Observable<Integer> GetNumberOfUnreadWhims() {
        return whirlpoolRestClient.GetWhims()
                .observeOn(schedulerManager.GetMainScheduler())
                .subscribeOn(schedulerManager.GetIoScheduler())
                .map(whimsList -> whimsList.getWHIMS())
                .flatMap(whims -> Observable.from(whims))
                .filter(whim -> whim.getVIEWED() == 0)
                .count();
    }

    @Override public Observable<List<Whim>> GetUnreadWhimsInInterval(long interval) {
        return whirlpoolRestClient.GetWhims()
                .map(whimsList1 -> whimsList1.getWHIMS())
                .flatMap(whims -> Observable.from(whims))
                .filter(whim -> whim.getVIEWED() == 0)
                .filter(whim1 -> WhirlpoolDateUtils.isTimeWithinDuration(whim1.getDATE(), interval))
                .toList();

    }
}
