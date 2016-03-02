package com.android.nitecafe.whirlpoolnews.controllers;

import com.android.nitecafe.whirlpoolnews.models.RecentList;
import com.android.nitecafe.whirlpoolnews.ui.interfaces.IRecentFragment;
import com.android.nitecafe.whirlpoolnews.web.interfaces.IWatchedThreadService;
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
public class RecentControllerTests {

    @Mock IWhirlpoolRestService whirlpoolRestServiceMock;
    @Mock IWatchedThreadService watchedThreadServiceMock;
    @Mock IRecentFragment recentFragmentMock;
    private RecentController controller;

    @Before
    public void setup() {
        controller = new RecentController(whirlpoolRestServiceMock, watchedThreadServiceMock);
        controller.Attach(recentFragmentMock);
    }

    @Test
    public void GetRecent_WhenSuccess_DisplayRecent() {

        //arrange
        RecentList list = new RecentList();
        when(whirlpoolRestServiceMock.GetRecent()).thenReturn(Observable.just(list));

        //act
        controller.GetRecent();

        //assert
        verify(recentFragmentMock).DisplayRecent(list.getRECENT());
        verify(recentFragmentMock).HideRefreshLoader();
        verify(recentFragmentMock).HideCenterProgressBar();
    }

    @Test
    public void GetRecent_WhenFailure_DisplayErrorMessage() {

        //arrange
        when(whirlpoolRestServiceMock.GetRecent()).thenReturn(Observable.error(new IOException()));

        //act
        controller.GetRecent();

        //assert
        verify(recentFragmentMock).DisplayErrorMessage();
        verify(recentFragmentMock).HideRefreshLoader();
        verify(recentFragmentMock).HideCenterProgressBar();
    }

}
