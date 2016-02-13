package com.android.nitecafe.whirlpoolnews.ui.interfaces;

import com.android.nitecafe.whirlpoolnews.models.ForumThread;
import com.android.nitecafe.whirlpoolnews.ui.fragments.IBaseFragment;

import java.util.List;

public interface IThreadFragment extends IBaseFragment {
    void HideCenterProgressBar();

    void DisplayErrorMessage();

    void HideRefreshLoader();

    void DisplayThreads(List<ForumThread> threads);
}
