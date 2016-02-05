package com.android.nitecafe.whirlpoolnews.ui.interfaces;

import com.android.nitecafe.whirlpoolnews.models.Watched;

import java.util.List;

/**
 * Created by grahamgoh on 5/02/16.
 */
public interface IWatchedFragment {
    void DisplayWatched(List<Watched> watcheds);

    void HideCenterProgressBar();

    void DisplayErrorMessage();

    void HideRefreshLoader();
}
