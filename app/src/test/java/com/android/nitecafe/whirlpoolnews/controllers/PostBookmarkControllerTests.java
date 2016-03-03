package com.android.nitecafe.whirlpoolnews.controllers;

import com.android.nitecafe.whirlpoolnews.models.PostBookmark;
import com.android.nitecafe.whirlpoolnews.services.IPostBookmarkService;
import com.android.nitecafe.whirlpoolnews.ui.interfaces.IPostBookmarkFragment;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class PostBookmarkControllerTests {

    @Mock IPostBookmarkService postBookmarkServiceMock;
    @Mock IPostBookmarkFragment postBookmarkFragmentMock;
    private PostBookmarkController controller;

    @Before
    public void setup() {
        controller = new PostBookmarkController(postBookmarkServiceMock);
        controller.Attach(postBookmarkFragmentMock);
    }

    @Test
    public void GetPostBookmarks_WhenCalled_CallService() {

        //arrange
        List<PostBookmark> bookmarks = new ArrayList<>();
        Mockito.when(postBookmarkServiceMock.getPostBookmarks()).thenReturn(bookmarks);

        //act
        controller.GetPostBookmarks();

        //assert
        Mockito.verify(postBookmarkServiceMock).getPostBookmarks();
    }


    @Test
    public void GetPostBookmarks_WhenSuccess_DisplayPostBookmars() {

        //arrange
        List<PostBookmark> bookmarks = new ArrayList<>();
        Mockito.when(postBookmarkServiceMock.getPostBookmarks()).thenReturn(bookmarks);

        //act
        controller.GetPostBookmarks();

        //assert
        Mockito.verify(postBookmarkFragmentMock).HideCenterProgressBar();
        Mockito.verify(postBookmarkFragmentMock).DisplayPostBookmarks(bookmarks);
    }

    @Test
    public void RemovePostBookmark_WhenCalled_CallService() {

        //act
        controller.RemovePostBookmark(1);

        //assert
        Mockito.verify(postBookmarkServiceMock).removePostBookmark(1);
        Mockito.verify(postBookmarkFragmentMock).DisplayPostBookmarkRemovedMessage();
    }
}
