package com.android.nitecafe.whirlpoolnews.ui.fragments;

import com.android.nitecafe.whirlpoolnews.WhirlpoolApp;

public class AllWatchedChildFragment extends WatchedChildFragment {

    @Override protected void loadWatchedCustom() {
        _controller.GetAllWatched();
    }

    @Override
    protected void startGoogleAnalytic() {
        WhirlpoolApp.getInstance().trackScreenView("All Watched Fragment");
    }
}
