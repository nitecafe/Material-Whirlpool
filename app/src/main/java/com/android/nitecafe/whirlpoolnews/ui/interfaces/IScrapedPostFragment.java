package com.android.nitecafe.whirlpoolnews.ui.interfaces;

import com.android.nitecafe.whirlpoolnews.models.ScrapedPost;

import java.util.List;

public interface IScrapedPostFragment {
    void setUpToolbarActionButtons();

    void HideCenterProgressBar();

    void DisplayErrorMessage();

    void DisplayPosts(List<ScrapedPost> posts);

    void HideRefreshLoader();

    void SetupPageSpinnerDropDown(int pageCount, int page);

    void ShowRefreshLoader();

    void DisplayActionSuccessMessage();

    void DisplayActionUnsuccessfullyMessage();
}
