package com.android.nitecafe.whirlpoolnews.ui.interfaces;

import com.android.nitecafe.whirlpoolnews.models.PostBookmark;

import java.util.List;

public interface IPostBookmarkFragment {
    void DisplayPostBookmarks(List<PostBookmark> postBookmarks);

    void HideCenterProgressBar();

    void DisplayErrorMessage();

    void DisplayPostBookmarkRemovedMessage();
}
