package com.android.nitecafe.whirlpoolnews;

import com.android.nitecafe.whirlpoolnews.controllers.NewsController;
import com.android.nitecafe.whirlpoolnews.models.NewsList;
import com.android.nitecafe.whirlpoolnews.ui.interfaces.INewsFragment;
import com.android.nitecafe.whirlpoolnews.web.IWhirlpoolRestService;

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

    @Mock IWhirlpoolRestService whirlpoolRestService;
    @Mock INewsFragment newsActivity;
    private NewsController _controller;

    @Before
    public void Setup() {
        _controller = new NewsController(whirlpoolRestService);
        _controller.Attach(newsActivity);
    }

    @Test
    public void GetNews_WhenSuccess_HideAllProgressBar() {

        //arrange
        NewsList newsList = new NewsList();
        when(whirlpoolRestService.GetNews()).thenReturn(Observable.just(newsList));

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
        when(whirlpoolRestService.GetNews()).thenReturn(Observable.just(newsList));

        //act
        _controller.GetNews();

        //assert
        verify(newsActivity).DisplayNews(anyList());
    }

    @Test
    public void GetNews_WhenFailure_HideAllProgressBar() {

        //arrange
        when(whirlpoolRestService.GetNews()).thenReturn(Observable.error(new Exception()));

        //act
        _controller.GetNews();

        //assert
        verify(newsActivity).HideCenterProgressBar();
        verify(newsActivity).HideRefreshLoader();

    }


    @Test
    public void GetNews_WhenFailure_ShowErrorMessage() {

        //arrange
        when(whirlpoolRestService.GetNews()).thenReturn(Observable.error(new Exception()));

        //act
        _controller.GetNews();

        //assert
        verify(newsActivity).DisplayErrorMessage();
    }
}


