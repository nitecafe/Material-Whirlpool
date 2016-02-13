package com.android.nitecafe.whirlpoolnews.ui.interfaces;

import com.android.nitecafe.whirlpoolnews.models.ScrapedPost;
import com.android.nitecafe.whirlpoolnews.ui.fragments.IThreadActionMessageFragment;

import java.util.List;

public interface IScrapedPostFragment extends IThreadActionMessageFragment {
    void setUpToolbarActionButtons();

    void HideCenterProgressBar();

    void DisplayErrorMessage();

    void DisplayPosts(List<ScrapedPost> posts);

    void HideRefreshLoader();

    void SetupPageSpinnerDropDown(int pageCount, int page);

    void ShowRefreshLoader();
}
