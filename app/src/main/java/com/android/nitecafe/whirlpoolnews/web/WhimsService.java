package com.android.nitecafe.whirlpoolnews.web;

import com.android.nitecafe.whirlpoolnews.models.Whim;
import com.android.nitecafe.whirlpoolnews.utilities.WhirlpoolDateUtils;
import com.android.nitecafe.whirlpoolnews.web.interfaces.IWhimsService;
import com.android.nitecafe.whirlpoolnews.web.interfaces.IWhirlpoolRestClient;
import com.android.nitecafe.whirlpoolnews.web.interfaces.IWhirlpoolRestService;

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

    private IWhirlpoolRestService whirlpoolRestService;
    private IWhirlpoolRestClient whirlpoolRestClient;

    @Inject
    public WhimsService(IWhirlpoolRestService whirlpoolRestService, IWhirlpoolRestClient whirlpoolRestClient) {
        this.whirlpoolRestService = whirlpoolRestService;
        this.whirlpoolRestClient = whirlpoolRestClient;
    }

    /**
     * Runs in app
     */
    @Override public Observable<Integer> GetNumberOfUnreadWhims() {
        return whirlpoolRestService.GetWhims()
                .map(whimsList -> whimsList.getWHIMS())
                .flatMap(whims -> Observable.from(whims))
                .filter(whim -> whim.getVIEWED() == 0)
                .count();
    }

    /**
     * Runs in background service
     */
    @Override public Observable<List<Whim>> GetUnreadWhimsInInterval(long interval) {
        return whirlpoolRestClient.GetWhims()
                .map(whimsList1 -> whimsList1.getWHIMS())
                .flatMap(whims -> Observable.from(whims))
                .filter(whim -> whim.getVIEWED() == 0)
                .filter(whim1 -> WhirlpoolDateUtils.isTimeWithinDuration(whim1.getDATE(), interval))
                .toList();

    }

    @Override
    public Observable<List<Whim>> GetUnreadWhims() {
        return whirlpoolRestClient.GetWhims()
                .map(whimsList1 -> whimsList1.getWHIMS())
                .flatMap(whims -> Observable.from(whims))
                .filter(whim -> whim.getVIEWED() == 0)
                .toList();
    }
}
