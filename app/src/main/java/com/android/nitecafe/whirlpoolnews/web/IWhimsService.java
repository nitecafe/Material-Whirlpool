package com.android.nitecafe.whirlpoolnews.web;

import com.android.nitecafe.whirlpoolnews.models.Whim;

import java.util.List;

import rx.Observable;

/**
 * Created by grahamgoh on 9/03/16.
 */
public interface IWhimsService {
    Observable<Integer> GetNumberOfUnreadWhims();

    Observable<List<Whim>> GetUnreadWhimsInInterval(long interval);
}
