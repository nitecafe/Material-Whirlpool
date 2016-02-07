package com.android.nitecafe.whirlpoolnews.ui.fragments;

import com.android.nitecafe.whirlpoolnews.models.ForumThread;

import java.util.List;

public interface IThreadFragment {
    void HideCenterProgressBar();

    void DisplayErrorMessage();

    void HideRefreshLoader();

    void DisplayThreads(List<ForumThread> threads);
}
