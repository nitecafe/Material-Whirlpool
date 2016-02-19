package com.android.nitecafe.whirlpoolnews;

import com.android.nitecafe.whirlpoolnews.controllers.ForumController;
import com.android.nitecafe.whirlpoolnews.models.ForumList;
import com.android.nitecafe.whirlpoolnews.ui.interfaces.IForumFragment;
import com.android.nitecafe.whirlpoolnews.web.interfaces.IWhirlpoolRestService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;

import rx.Observable;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ForumControllerTests {

    @Mock IWhirlpoolRestService whirlpoolRestServiceMock;
    @Mock IForumFragment forumFragmentMock;
    private ForumController _controllerToTest;


    @Before
    public void setup() {
        _controllerToTest = new ForumController(whirlpoolRestServiceMock);
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
}
