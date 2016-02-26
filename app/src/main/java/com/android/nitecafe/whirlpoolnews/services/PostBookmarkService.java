package com.android.nitecafe.whirlpoolnews.services;

import android.content.SharedPreferences;

import com.android.nitecafe.whirlpoolnews.constants.StringConstants;
import com.android.nitecafe.whirlpoolnews.models.PostBookmark;
import com.android.nitecafe.whirlpoolnews.utilities.interfaces.IObjectSerializer;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class PostBookmarkService implements IPostBookmarkService {

    private List<PostBookmark> mPostBookmarks = new ArrayList<>();
    private SharedPreferences mSharedPreferences;
    private IObjectSerializer mObjectSerializerMock;

    @Inject
    public PostBookmarkService(SharedPreferences sharedPreferences, IObjectSerializer objectSerializerMock) {
        mSharedPreferences = sharedPreferences;
        mObjectSerializerMock = objectSerializerMock;

        loadExistingPostBookmarks();
    }

    private void loadExistingPostBookmarks() {
        final String bookmarkList = mSharedPreferences.getString(StringConstants.POST_BOOKMARK_PREFERENCE_KEY, "");
        if (!bookmarkList.isEmpty())
            mPostBookmarks = mObjectSerializerMock.deserializePostBookmarkList(bookmarkList);
    }


    @Override
    public void addPostBookmark(PostBookmark bookmark) {
        if (mPostBookmarks.contains(bookmark))
            throw new IllegalArgumentException("Post bookmark already exists");

        mPostBookmarks.add(bookmark);
        saveBookmarksToPreference();
    }

    private void saveBookmarksToPreference() {
        final String s = mObjectSerializerMock.serializeObject(mPostBookmarks);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(StringConstants.POST_BOOKMARK_PREFERENCE_KEY, s);
        editor.apply();
    }

    @Override
    public void removePostBookmark(int postId) {
        if (!isABookmark(postId))
            throw new IllegalArgumentException("Post bookmark does not exists");

        removeBookmark(postId);
        saveBookmarksToPreference();
    }

    @Override
    public List<PostBookmark> getPostBookmarks() {
        return mPostBookmarks;
    }

    @Override
    public boolean isABookmark(int postId) {
        for (PostBookmark p : mPostBookmarks) {
            if (p.getPostId() == postId)
                return true;
        }
        return false;
    }

    private void removeBookmark(int postId) {
        for (PostBookmark p : mPostBookmarks) {
            if (p.getPostId() == postId) {
                mPostBookmarks.remove(p);
                return;
            }
        }
    }
}
