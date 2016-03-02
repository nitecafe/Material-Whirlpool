package com.android.nitecafe.whirlpoolnews.controllers;

import com.android.nitecafe.whirlpoolnews.models.WatchedList;
import com.android.nitecafe.whirlpoolnews.ui.interfaces.IWatchedFragment;
import com.android.nitecafe.whirlpoolnews.web.interfaces.IWatchedThreadService;
import com.android.nitecafe.whirlpoolnews.web.interfaces.IWhirlpoolRestService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;

import rx.Observable;

import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class WatchedControllerTests {

    @Mock IWhirlpoolRestService whirlpoolRestServiceMock;
    @Mock IWatchedThreadService watchedThreadServiceMock;
    @Mock IWatchedFragment watchedFragmentMock;
    private WatchedController controller;


    @Before
    public void setup() {
        controller = new WatchedController(whirlpoolRestServiceMock, watchedThreadServiceMock);
        controller.Attach(watchedFragmentMock);
    }

    @Test
    public void GetUnreadWatched_WhenSuccess_DisplaysWatched() {

        //arrange
        WatchedList list = new WatchedList();
        Mockito.when(whirlpoolRestServiceMock.GetUnreadWatched()).thenReturn(Observable.just(list));

        //act
        controller.GetUnreadWatched();

        //arrange
        verify(watchedFragmentMock).DisplayWatched(list.getWATCHED());
        verify(watchedFragmentMock).HideCenterProgressBar();
        verify(watchedFragmentMock).HideRefreshLoader();
    }

    @Test
    public void GetAllWatched_WhenSuccess_DisplaysWatched() {

        //arrange
        WatchedList list = new WatchedList();
        Mockito.when(whirlpoolRestServiceMock.GetAllWatched()).thenReturn(Observable.just(list));

        //act
        controller.GetAllWatched();

        //arrange
        verify(watchedFragmentMock).DisplayWatched(list.getWATCHED());
        verify(watchedFragmentMock).HideCenterProgressBar();
        verify(watchedFragmentMock).HideRefreshLoader();
    }

    @Test
    public void GetUnreadWatched_WhenFailure_DisplaysErrorMessage() {

        //arrange
        Mockito.when(whirlpoolRestServiceMock.GetUnreadWatched()).thenReturn(Observable.error(new IOException()));

        //act
        controller.GetUnreadWatched();

        //arrange
        verify(watchedFragmentMock).DisplayErrorMessage();
        verify(watchedFragmentMock).HideCenterProgressBar();
        verify(watchedFragmentMock).HideRefreshLoader();
    }

    @Test
    public void GetAllWatched_WhenFailure_DisplaysErrorMessage() {

        //arrange
        Mockito.when(whirlpoolRestServiceMock.GetAllWatched()).thenReturn(Observable.error(new IOException()));

        //act
        controller.GetAllWatched();

        //arrange
        verify(watchedFragmentMock).DisplayErrorMessage();
        verify(watchedFragmentMock).HideCenterProgressBar();
        verify(watchedFragmentMock).HideRefreshLoader();
    }

}
