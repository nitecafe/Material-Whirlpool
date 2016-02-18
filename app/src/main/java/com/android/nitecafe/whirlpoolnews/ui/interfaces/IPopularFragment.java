package com.android.nitecafe.whirlpoolnews.ui.interfaces;

import com.android.nitecafe.whirlpoolnews.models.ScrapedThread;

import java.util.ArrayList;

public interface IPopularFragment extends IThreadActionMessageFragment {

    void DisplayPopularThreads(ArrayList<ScrapedThread> threadList);

    void DisplayErrorMessage();

    void HideCenterProgressBar();
}
