package com.android.nitecafe.whirlpoolnews.services;

import com.android.nitecafe.whirlpoolnews.models.PostBookmark;

import java.util.List;

public interface IPostBookmarkService {
    void addPostBookmark(PostBookmark bookmark);

    void removePostBookmark(int postId);

    List<PostBookmark> getPostBookmarks();

    boolean isABookmark(int postId);
}
