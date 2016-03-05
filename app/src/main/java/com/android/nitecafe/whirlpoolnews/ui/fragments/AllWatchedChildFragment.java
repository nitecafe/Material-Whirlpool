package com.android.nitecafe.whirlpoolnews.ui.fragments;

import com.android.nitecafe.whirlpoolnews.WhirlpoolApp;

public class AllWatchedChildFragment extends WatchedChildFragment {

    @Override
    public void loadWatched() {
        _controller.GetAllWatched();
    }

    @Override
    protected void startGoogleAnalytic() {
        WhirlpoolApp.getInstance().trackScreenView("All Watched Fragment");
    }
}
