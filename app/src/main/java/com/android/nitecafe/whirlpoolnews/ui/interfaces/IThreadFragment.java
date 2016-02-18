package com.android.nitecafe.whirlpoolnews.ui.interfaces;

import com.android.nitecafe.whirlpoolnews.models.ForumThread;

import java.util.List;

public interface IThreadFragment extends IThreadActionMessageFragment {
    void HideCenterProgressBar();

    void DisplayErrorMessage();

    void HideRefreshLoader();

    void DisplayThreads(List<ForumThread> threads);
}
