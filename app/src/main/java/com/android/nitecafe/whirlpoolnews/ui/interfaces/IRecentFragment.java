package com.android.nitecafe.whirlpoolnews.ui.interfaces;

import com.android.nitecafe.whirlpoolnews.models.Recent;

import java.util.List;

public interface IRecentFragment {
    void DisplayRecent(List<Recent> recents);

    void HideCenterProgressBar();

    void DisplayErrorMessage();

    void HideRefreshLoader();

    void ShowThreadWatchedSuccessfully();

    void ShowThreadWatchedFailureMessage();
}
