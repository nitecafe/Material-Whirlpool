package com.android.nitecafe.whirlpoolnews.ui.interfaces;

import com.android.nitecafe.whirlpoolnews.models.Watched;

import java.util.List;

public interface IWatchedFragment {
    void loadWatched();

    void DisplayWatched(List<Watched> watcheds);

    void HideCenterProgressBar();

    void DisplayErrorMessage();

    void HideRefreshLoader();

    void ShowActionSuccessMessage();

    void ShowActionFailedMessage();
}
