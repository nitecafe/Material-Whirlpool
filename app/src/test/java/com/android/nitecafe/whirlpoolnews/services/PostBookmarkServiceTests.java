package com.android.nitecafe.whirlpoolnews.services;

import android.content.SharedPreferences;

import com.android.nitecafe.whirlpoolnews.constants.StringConstants;
import com.android.nitecafe.whirlpoolnews.models.PostBookmark;
import com.android.nitecafe.whirlpoolnews.utilities.interfaces.IObjectSerializer;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PostBookmarkServiceTests {

    @Mock SharedPreferences mSharedPreferencesMock;
    @Mock SharedPreferences.Editor mEditorMock;
    @Mock IObjectSerializer objectSerializerMock;
    private PostBookmarkService _service;

    @Before
    public void Setup() {
        when(mSharedPreferencesMock.edit()).thenReturn(mEditorMock);
        when(mSharedPreferencesMock.getString(StringConstants.POST_BOOKMARK_PREFERENCE_KEY, "")).thenReturn("");
        _service = new PostBookmarkService(mSharedPreferencesMock, objectSerializerMock);
    }

    @Test
    public void onConstructor_NoDataInSharedPreference_ReturnEmptyList() {

        //assert
        Assert.assertEquals(0, _service.getPostBookmarks().size());
    }

    @Test
    public void onConstructor_WhenGotData_ReturnData() {

        //arrange
        PostBookmark bookmark = new PostBookmark("Name", 1, 1, 1, 1);
        when(mSharedPreferencesMock.getString(StringConstants.POST_BOOKMARK_PREFERENCE_KEY, "")).thenReturn("Something not empty");
        when(objectSerializerMock.deserializePostBookmarkList(anyString())).thenReturn(Arrays.asList(bookmark));

        //act
        PostBookmarkService service = new PostBookmarkService(mSharedPreferencesMock, objectSerializerMock);

        //assert
        assertBookmarkSame(bookmark, service.getPostBookmarks().get(0));
    }

    @Test
    public void addBookmark_WhenAdded_SaveToSharedPreferences() {

        //arrange
        PostBookmark bookmark = new PostBookmark("Name", 1, 1, 1, 1);
        String contentToSave = "SOMETHING";
        when(objectSerializerMock.serializeObject(any())).thenReturn(contentToSave);

        //act
        _service.addPostBookmark(bookmark);
        //assert
        verify(mEditorMock).putString(StringConstants.POST_BOOKMARK_PREFERENCE_KEY, contentToSave);
        assertBookmarkSame(bookmark, _service.getPostBookmarks().get(0));
    }

    @Test
    public void getPostBookmarks_WhenTwoAdded_ShouldHaveTwo() {

        //arrange
        PostBookmark bookmark = new PostBookmark("Name", 1, 1, 1, 1);
        PostBookmark bookmark2 = new PostBookmark("Name", 2, 2, 1, 1);
        String contentToSave = "SOMETHING";
        when(objectSerializerMock.serializeObject(any())).thenReturn(contentToSave);

        //act
        _service.addPostBookmark(bookmark);
        _service.addPostBookmark(bookmark2);

        //assert
        Assert.assertEquals(2, _service.getPostBookmarks().size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void addBookmark_WhenDuplicateAdded_ThrowIllegalArgumentException() {

        //arrange
        PostBookmark bookmark = new PostBookmark("Name", 1, 1, 1, 1);
        PostBookmark bookmark2 = new PostBookmark("Name", 1, 1, 1, 1);
        String contentToSave = "SOMETHING";
        when(objectSerializerMock.serializeObject(any())).thenReturn(contentToSave);
        _service.addPostBookmark(bookmark);

        //act
        _service.addPostBookmark(bookmark2);
        Assert.assertEquals(1, _service.getPostBookmarks().size());
    }

    @Test
    public void removeBookmark_WhenRemoved_SaveToSharedPreference() {

        //arrange
        PostBookmark bookmark = new PostBookmark("Name", 1, 1, 1, 1);
        String contentToSave = "SOMETHING";
        when(objectSerializerMock.serializeObject(any())).thenReturn(contentToSave);
        _service.addPostBookmark(bookmark);

        //act
        _service.removePostBookmark(1);

        //assert
        verify(mEditorMock, times(2)).putString(StringConstants.POST_BOOKMARK_PREFERENCE_KEY, contentToSave);
        Assert.assertEquals(0, _service.getPostBookmarks().size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void removeBookmark_WhenDoesNotExist_ThrowIllegalArgumentException() {

        //arrange
        String contentToSave = "SOMETHING";
        when(objectSerializerMock.serializeObject(any())).thenReturn(contentToSave);

        //act
        _service.removePostBookmark(1);
    }

    @Test
    public void isABookmark_WhenNot_ReturnFalse() {

        //act
        final boolean aBookmark = _service.isABookmark(1);

        //assert
        Assert.assertFalse(aBookmark);
    }

    @Test
    public void isABookmark_WhenYes_ReturnFalse() {

        //arrange
        PostBookmark bookmark = new PostBookmark("Name", 1, 1, 1, 1);
        _service.addPostBookmark(bookmark);

        //act
        final boolean aBookmark = _service.isABookmark(1);

        //assert
        Assert.assertTrue(aBookmark);
    }

    private void assertBookmarkSame(PostBookmark a, PostBookmark b) {
        Assert.assertEquals(a.getBookMarkName(), b.getBookMarkName());
        Assert.assertEquals(a.getPageLocated(), b.getPageLocated());
        Assert.assertEquals(a.getPositionOnPage(), b.getPageLocated());
        Assert.assertEquals(a.getPostId(), b.getPostId());
        Assert.assertEquals(a.getThreadId(), b.getThreadId());
    }
}
