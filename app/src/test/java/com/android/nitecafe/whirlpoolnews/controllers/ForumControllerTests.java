package com.android.nitecafe.whirlpoolnews.controllers;

import com.android.nitecafe.whirlpoolnews.models.Forum;
import com.android.nitecafe.whirlpoolnews.models.ForumList;
import com.android.nitecafe.whirlpoolnews.ui.interfaces.IForumFragment;
import com.android.nitecafe.whirlpoolnews.utilities.interfaces.IFavouriteThreadService;
import com.android.nitecafe.whirlpoolnews.web.interfaces.IWhirlpoolRestService;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import rx.Observable;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ForumControllerTests {

    @Mock IWhirlpoolRestService whirlpoolRestServiceMock;
    @Mock IFavouriteThreadService favouriteThreadServiceMock;
    @Mock IForumFragment forumFragmentMock;
    private ForumController _controllerToTest;


    @Before
    public void setup() {
        _controllerToTest = new ForumController(whirlpoolRestServiceMock, favouriteThreadServiceMock);
        _controllerToTest.attach(forumFragmentMock);
    }

    @Test
    public void GetForum_WhenCalled_CallServiceGetForum() {

        //arrange
        ForumList forumList = new ForumList();
        when(whirlpoolRestServiceMock.GetForum()).thenReturn(Observable.just(forumList));

        //act
        _controllerToTest.getForum();

        //assert
        verify(whirlpoolRestServiceMock).GetForum();
    }

    @Test
    public void GetForum_WhenLoaded_ShowForumsOnView() {

        //arrange
        ForumList forumList = new ForumList();
        when(whirlpoolRestServiceMock.GetForum()).thenReturn(Observable.just(forumList));

        //act
        _controllerToTest.getForum();

        //assert
        verify(forumFragmentMock).DisplayForum(forumList.getFORUM());
    }

    @Test
    public void GetForum_WhenLoaded_HideAllLoader() {

        //arrange
        ForumList forumList = new ForumList();
        when(whirlpoolRestServiceMock.GetForum()).thenReturn(Observable.just(forumList));

        //act
        _controllerToTest.getForum();

        //assert
        verify(forumFragmentMock).HideCenterProgressBar();
        verify(forumFragmentMock).HideRefreshLoader();
    }

    @Test
    public void GetForum_OnException_DisplayErrorMessage() {

        //arrange
        when(whirlpoolRestServiceMock.GetForum()).thenReturn(Observable.error(new IOException()));

        //act
        _controllerToTest.getForum();

        //assert
        verify(forumFragmentMock).DisplayErrorMessage();
    }

    @Test
    public void GetForum_OnException_HideAllLoader() {

        //arrange
        when(whirlpoolRestServiceMock.GetForum()).thenReturn(Observable.error(new IOException()));

        //act
        _controllerToTest.getForum();

        //assert
        verify(forumFragmentMock).HideCenterProgressBar();
        verify(forumFragmentMock).HideRefreshLoader();
    }

    @Test
    public void getCombinedFavouriteSection_WhenHasFavourite_IncludesCorrectForums() {

        //arrange
        Forum fav1 = new Forum();
        fav1.setID(1);
        fav1.setTITLE("Favourite 1");

        ForumList forumList = new ForumList();
        Forum normalForum = new Forum();
        normalForum.setID(2);
        normalForum.setTITLE("Normal");
        forumList.getFORUM().add(normalForum);

        when(favouriteThreadServiceMock.getListOfFavouritesThreadIds()).thenReturn(Arrays.asList(fav1));
        when(whirlpoolRestServiceMock.GetForum()).thenReturn(Observable.just(forumList));
        _controllerToTest.getForum();

        //act
        List<Forum> combinedFavouriteSection = _controllerToTest.getCombinedFavouriteSection();

        //assert
        Assert.assertTrue(combinedFavouriteSection.size() == 2);
        Assert.assertTrue(combinedFavouriteSection.contains(fav1));
        Assert.assertTrue(combinedFavouriteSection.contains(normalForum));
    }

    @Test
    public void AddToFavouriteList_WhenCalled_CallCorrectMethods() {

        //arrange
        int id = 1;
        String title = "Internet";

        //act
        _controllerToTest.AddToFavouriteList(id, title);

        //assert
        verify(favouriteThreadServiceMock).addThreadToFavourite(id, title);
        verify(forumFragmentMock).UpdateFavouriteSection();
        verify(forumFragmentMock).DisplayAddToFavouriteForumMessage();
    }

    @Test
    public void RemoveFromFavouriteList_WhenCalled_CallCorrectMethods() {

        //arrange
        int id = 1;

        //act
        _controllerToTest.RemoveFromFavouriteList(id);

        //assert
        verify(favouriteThreadServiceMock).removeThreadFromFavourite(id);
        verify(forumFragmentMock).UpdateFavouriteSection();
        verify(forumFragmentMock).DisplayRemoveFromFavouriteForumMessage();
    }
}
