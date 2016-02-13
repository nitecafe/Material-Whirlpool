package com.android.nitecafe.whirlpoolnews.ui.interfaces;

import com.android.nitecafe.whirlpoolnews.models.Recent;
import com.android.nitecafe.whirlpoolnews.ui.fragments.IBaseFragment;

import java.util.List;

public interface IRecentFragment extends IBaseFragment {
    void DisplayRecent(List<Recent> recents);

    void HideCenterProgressBar();

    void DisplayErrorMessage();

    void HideRefreshLoader();

}
