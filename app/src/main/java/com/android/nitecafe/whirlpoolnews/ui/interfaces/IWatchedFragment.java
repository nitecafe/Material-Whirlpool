package com.android.nitecafe.whirlpoolnews.ui.interfaces;

import com.android.nitecafe.whirlpoolnews.models.Watched;

import java.util.List;

public interface IWatchedFragment extends IThreadActionMessageFragment {
    void loadWatched();

    void DisplayWatched(List<Watched> watcheds);

    void HideCenterProgressBar();

    void DisplayErrorMessage();

    void HideRefreshLoader();
}
