package com.android.nitecafe.whirlpoolnews.ui.interfaces;

import com.android.nitecafe.whirlpoolnews.models.ScrapedPost;

import java.util.List;

public interface IScrapedPostChildFragment extends IThreadActionMessageFragment {
    void HideCenterProgressBar();

    void DisplayErrorMessage();

    void DisplayPosts(List<ScrapedPost> posts);

    void SetTitle(String thread_title);

    void UpdatePageCount(int pageCount);

    void showAddedToBookmarkMessage();

    void showRemoveFromBookmarkMessage();

    void LaunchThreadInBrowser();
}
