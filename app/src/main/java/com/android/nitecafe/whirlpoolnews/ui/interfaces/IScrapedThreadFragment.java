package com.android.nitecafe.whirlpoolnews.ui.interfaces;

import com.android.nitecafe.whirlpoolnews.models.ScrapedThread;

import java.util.List;
import java.util.Map;

public interface IScrapedThreadFragment extends IThreadActionMessageFragment {
    void HideCenterProgressBar();

    void DisplayErrorMessage();

    void DisplayThreads(List<ScrapedThread> threads);

    void HideRefreshLoader();

    void ShowRefreshLoader();

    void SetupPageSpinnerDropDown(int pageCount, int pageNumber);

    void SetupGroupSpinnerDropDown(Map<String, Integer> groups, int groupId);
}
