package com.android.nitecafe.whirlpoolnews.interfaces;

import com.android.nitecafe.whirlpoolnews.models.News;

import java.util.List;

/**
 * Created by Graham-i5 on 1/31/2016.
 */
public interface INewsActivity {

    void DisplayNews(List<News> news);

    void HideCenterProgressBar();

    void DisplayErrorMessage();

    void HideRefreshLoader();
}
