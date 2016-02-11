package com.android.nitecafe.whirlpoolnews.ui.interfaces;

import com.android.nitecafe.whirlpoolnews.models.ScrapedThread;

import java.util.List;

public interface IScrapedThreadFragment {
    void HideCenterProgressBar();

    void DisplayErrorMessage();

    void DisplayThreads(List<ScrapedThread> threads);

    void HideRefreshLoader();
}
