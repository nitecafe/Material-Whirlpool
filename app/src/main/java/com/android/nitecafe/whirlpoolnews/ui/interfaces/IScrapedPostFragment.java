package com.android.nitecafe.whirlpoolnews.ui.interfaces;

import com.android.nitecafe.whirlpoolnews.models.ScrapedPost;

import java.util.List;

public interface IScrapedPostFragment {
    void HideCenterProgressBar();

    void DisplayErrorMessage();

    void DisplayPosts(List<ScrapedPost> posts);

    void HideRefreshLoader();
}
