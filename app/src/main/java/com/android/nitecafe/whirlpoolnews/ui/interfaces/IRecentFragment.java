package com.android.nitecafe.whirlpoolnews.ui.interfaces;

import com.android.nitecafe.whirlpoolnews.models.Recent;

import java.util.List;

public interface IRecentFragment extends IThreadActionMessageFragment {
    void DisplayRecent(List<Recent> recents);

    void HideCenterProgressBar();

    void DisplayErrorMessage();

    void HideRefreshLoader();

}
