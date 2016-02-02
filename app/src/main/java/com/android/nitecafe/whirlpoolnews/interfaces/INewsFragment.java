package com.android.nitecafe.whirlpoolnews.interfaces;

import com.android.nitecafe.whirlpoolnews.models.News;

import java.util.List;

public interface INewsFragment {

    void DisplayNews(List<News> news);

    void HideCenterProgressBar();

    void DisplayErrorMessage();

    void HideRefreshLoader();
}
