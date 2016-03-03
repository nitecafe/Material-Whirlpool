package com.android.nitecafe.whirlpoolnews.controllers;

import com.android.nitecafe.whirlpoolnews.constants.StringConstants;
import com.android.nitecafe.whirlpoolnews.models.ForumThreadList;
import com.android.nitecafe.whirlpoolnews.ui.interfaces.IThreadFragment;
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

@RunWith(MockitoJUnitRunner.class)
public class ForumThreadControllerTests {

    @Mock IWhirlpoolRestService whirlpoolRestServiceMock;
    @Mock IWatchedThreadService watchedThreadServiceMock;
    @Mock IThreadFragment threadFragmentMock;
    private ForumThreadController controller;


    @Before
    public void setup() {
        controller = new ForumThreadController(whirlpoolRestServiceMock, watchedThreadServiceMock);
        controller.Attach(threadFragmentMock);
    }

    @Test
    public void GetThreads_WhenSuccess_DisplayThreads() {

        //arrange
        ForumThreadList threads = new ForumThreadList();
        Mockito.when(whirlpoolRestServiceMock.GetThreads(1, StringConstants.DEFAULT_THREAD_COUNT)).thenReturn(Observable.just(threads));

        //act
        controller.GetThreads(1);

        //assert
        Mockito.verify(threadFragmentMock).DisplayThreads(threads.getTHREADS());
        Mockito.verify(threadFragmentMock).HideCenterProgressBar();
        Mockito.verify(threadFragmentMock).HideRefreshLoader();

    }

    @Test
    public void GetThreads_WhenFailure_DisplayErrorMessage() {

        //arrange
        Mockito.when(whirlpoolRestServiceMock.GetThreads(1, StringConstants.DEFAULT_THREAD_COUNT)).thenReturn(Observable.error(new IOException()));

        //act
        controller.GetThreads(1);

        //assert
        Mockito.verify(threadFragmentMock).DisplayErrorMessage();
        Mockito.verify(threadFragmentMock).HideCenterProgressBar();
        Mockito.verify(threadFragmentMock).HideRefreshLoader();
    }

}
