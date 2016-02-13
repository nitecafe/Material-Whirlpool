package com.android.nitecafe.whirlpoolnews.ui.interfaces;

import com.android.nitecafe.whirlpoolnews.models.Watched;
import com.android.nitecafe.whirlpoolnews.ui.fragments.IBaseFragment;

import java.util.List;

public interface IWatchedFragment extends IBaseFragment{
    void loadWatched();

    void DisplayWatched(List<Watched> watcheds);

    void HideCenterProgressBar();

    void DisplayErrorMessage();

    void HideRefreshLoader();

}
