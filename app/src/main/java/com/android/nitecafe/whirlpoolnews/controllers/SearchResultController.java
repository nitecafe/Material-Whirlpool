package com.android.nitecafe.whirlpoolnews.controllers;

import com.android.nitecafe.whirlpoolnews.ui.interfaces.ISearchResultFragment;
import com.android.nitecafe.whirlpoolnews.ui.interfaces.IThreadActionMessageFragment;
import com.android.nitecafe.whirlpoolnews.web.interfaces.IWatchedThreadService;
import com.android.nitecafe.whirlpoolnews.web.interfaces.IWhirlpoolRestService;

import javax.inject.Inject;

public class SearchResultController extends ThreadBaseController<IThreadActionMessageFragment> {

    private ISearchResultFragment mSearchFragment;
    private IWhirlpoolRestService whirlpoolRestService;

    @Inject
    public SearchResultController(IWhirlpoolRestService whirlpoolRestService,
                                  IWatchedThreadService watchedThreadIdentifier) {
        super(whirlpoolRestService, watchedThreadIdentifier);
        this.whirlpoolRestService = whirlpoolRestService;
    }

    public void Attach(ISearchResultFragment view) {
        super.Attach(view);
        mSearchFragment = view;
    }

    public void Search(String query, int forumId, int groupId) {
        whirlpoolRestService.SearchThreads(forumId, groupId, query)
                .subscribe(scrapedThreads -> {
                    if (mSearchFragment != null) {
                        mSearchFragment.DisplaySearchResults(scrapedThreads);
                        mSearchFragment.HideSearchProgressBar();
                    }
                }, throwable -> {
                    mSearchFragment.ShowActionFailedMessage();
                    mSearchFragment.HideSearchProgressBar();
                });
    }
}
