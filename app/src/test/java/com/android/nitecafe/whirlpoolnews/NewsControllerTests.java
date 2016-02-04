package com.android.nitecafe.whirlpoolnews;

import com.android.nitecafe.whirlpoolnews.controllers.NewsController;
import com.android.nitecafe.whirlpoolnews.ui.interfaces.INewsFragment;
import com.android.nitecafe.whirlpoolnews.interfaces.IWhirlpoolRestClient;
import com.android.nitecafe.whirlpoolnews.models.NewsList;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import rx.Observable;

import static org.mockito.Mockito.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class NewsControllerTests {

    @Mock IWhirlpoolRestClient whirlpoolRestClient;
    @Mock INewsFragment newsActivity;
    private NewsController _controller;

    @Before
    public void Setup() {
        _controller = new NewsController(whirlpoolRestClient, new TestSchedulerManager());
        _controller.Attach(newsActivity);
    }

    @Test
    public void GetNews_WhenSuccess_HideAllProgressBar() {

        //arrange
        NewsList newsList = new NewsList();
        when(whirlpoolRestClient.GetNews()).thenReturn(Observable.just(newsList));

        //act
        _controller.GetNews();

        //assert
        verify(newsActivity).HideCenterProgressBar();
        verify(newsActivity).HideRefreshLoader();
    }

    @Test
    public void GetNews_WhenSuccess_ShowNews() {

        //arrange
        NewsList newsList = new NewsList();
        when(whirlpoolRestClient.GetNews()).thenReturn(Observable.just(newsList));

        //act
        _controller.GetNews();

        //assert
        verify(newsActivity).DisplayNews(anyList());
        verify(newsActivity).HideRefreshLoader();
    }

//    @Test
//    public void GetNews_WhenFailure_HideAllProgressBar() {
//
//        //arrange
//        when(whirlpoolRestClient.GetNews()).thenReturn(Observable.error(new Exception()));
//
//        //act
//        _controller.GetNews();
//
//        //assert
//        verify(newsActivity, times(1)).HideCenterProgressBar();
//        verify(newsActivity, times(1)).HideRefreshLoader();
//
//    }

//
//    @Test
//    public void GetNews_WhenFailure_ShowErrorMessage() {
//
//        //arrange
//        when(whirlpoolRestClient.GetNews()).thenReturn();
//
//        //act
//        _controller.GetNews();
//
//        //assert
//        verify(newsActivity, times(1)).DisplayErrorMessage();
//    }
}


