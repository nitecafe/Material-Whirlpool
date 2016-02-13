package com.android.nitecafe.whirlpoolnews.ui.interfaces;

import com.android.nitecafe.whirlpoolnews.models.ScrapedThread;
import com.android.nitecafe.whirlpoolnews.ui.fragments.IBaseFragment;

import java.util.List;

public interface IScrapedThreadFragment extends IBaseFragment {
    void HideCenterProgressBar();

    void DisplayErrorMessage();

    void DisplayThreads(List<ScrapedThread> threads);

    void HideRefreshLoader();
}
