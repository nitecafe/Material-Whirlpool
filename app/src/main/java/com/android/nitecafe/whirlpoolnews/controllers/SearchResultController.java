package com.android.nitecafe.whirlpoolnews.controllers;

import com.android.nitecafe.whirlpoolnews.interfaces.IWhirlpoolRestClient;
import com.android.nitecafe.whirlpoolnews.scheduler.ISchedulerManager;
import com.android.nitecafe.whirlpoolnews.ui.fragments.IThreadActionMessageFragment;
import com.android.nitecafe.whirlpoolnews.utilities.IWatchedThreadIdentifier;

import javax.inject.Inject;

public class SearchResultController extends ThreadBaseController<IThreadActionMessageFragment> {

    @Inject
    public SearchResultController(IWhirlpoolRestClient whirlpoolRestClient, ISchedulerManager schedulerManager, IWatchedThreadIdentifier watchedThreadIdentifier) {
        super(whirlpoolRestClient, schedulerManager, watchedThreadIdentifier);
    }
}
