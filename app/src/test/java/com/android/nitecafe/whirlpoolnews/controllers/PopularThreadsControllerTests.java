package com.android.nitecafe.whirlpoolnews.controllers;

import com.android.nitecafe.whirlpoolnews.models.ScrapedThread;
import com.android.nitecafe.whirlpoolnews.ui.interfaces.IPopularFragment;
import com.android.nitecafe.whirlpoolnews.web.interfaces.IWatchedThreadService;
import com.android.nitecafe.whirlpoolnews.web.interfaces.IWhirlpoolRestService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.util.ArrayList;

import rx.Observable;

@RunWith(MockitoJUnitRunner.class)
public class PopularThreadsControllerTests {

    @Mock IWhirlpoolRestService mIWhirlpoolRestClientMock;
    @Mock IWatchedThreadService mIWatchedThreadServiceMock;
    @Mock IPopularFragment mIPopularFragmentMock;
    private PopularThreadsController controller;


    @Before
    public void Setup() {
        controller = new PopularThreadsController(mIWhirlpoolRestClientMock, mIWatchedThreadServiceMock);
        controller.Attach(mIPopularFragmentMock);
    }

    @Test
    public void WatchThread_WhenCalled_ShowThreadWatchedSuccessMessage() {

        //arrange
        int threadId = 111;

        Mockito.when(mIWhirlpoolRestClientMock.SetThreadAsWatch(threadId)).thenReturn(Observable.just(null));

        //act
        controller.WatchThread(threadId);

        //assert
        Mockito.verify(mIPopularFragmentMock).ShowThreadWatchedSuccessMessage();
    }

    @Test
    public void GetPopularThreads_WhenSuccess_DisplayPopularThreads() {

        //arrange
        ArrayList<ScrapedThread> threads = new ArrayList<>();
        Mockito.when(mIWhirlpoolRestClientMock.GetPopularThreads()).thenReturn(Observable.just(threads));

        //act
        controller.GetPopularThreads();

        //assert
        Mockito.verify(mIPopularFragmentMock).DisplayPopularThreads(threads);
        Mockito.verify(mIPopularFragmentMock).HideCenterProgressBar();
    }

    @Test
    public void GetPopularThreads_WhenFailure_DisplayErrorMessage() {

        //arrange
        Mockito.when(mIWhirlpoolRestClientMock.GetPopularThreads()).thenReturn(Observable.error(new IOException()));

        //act
        controller.GetPopularThreads();

        //assert
        Mockito.verify(mIPopularFragmentMock).DisplayErrorMessage();
        Mockito.verify(mIPopularFragmentMock).HideCenterProgressBar();
    }

}
