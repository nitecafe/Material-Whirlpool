package com.android.nitecafe.whirlpoolnews.ui.fragments;

public class AllWatchedChildFragment extends WatchedChildFragment {

    @Override protected void loadWatchedCustom() {
        _controller.GetAllWatched();
    }
}
