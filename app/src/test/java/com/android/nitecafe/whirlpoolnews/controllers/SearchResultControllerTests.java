package com.android.nitecafe.whirlpoolnews.controllers;


import com.android.nitecafe.whirlpoolnews.models.ScrapedThread;
import com.android.nitecafe.whirlpoolnews.ui.interfaces.ISearchResultFragment;
import com.android.nitecafe.whirlpoolnews.web.interfaces.IWatchedThreadService;
import com.android.nitecafe.whirlpoolnews.web.interfaces.IWhirlpoolRestService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import rx.Observable;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SearchResultControllerTests {

    @Mock IWhirlpoolRestService whirlpoolRestServiceMock;
    @Mock IWatchedThreadService watchedThreadServiceMock;
    @Mock ISearchResultFragment searchResultFragmentMock;
    private SearchResultController controller;

    @Before
    public void setup() {
        controller = new SearchResultController(whirlpoolRestServiceMock, watchedThreadServiceMock);
        controller.Attach(searchResultFragmentMock);
    }

    @Test
    public void Search_WhenSuccess_DisplaySearchResults() {

        //arrange
        ScrapedThread thread = new ScrapedThread(1, "G", new Date(), "lastposter", "forum", 1);
        List<ScrapedThread> scrapedThreads = Arrays.asList(thread);
        when(whirlpoolRestServiceMock.SearchThreads(1, 1, "q")).thenReturn(Observable.just(scrapedThreads));

        //act
        controller.Search("q", 1, 1);

        //assert
        verify(searchResultFragmentMock).DisplaySearchResults(scrapedThreads);
        verify(searchResultFragmentMock).HideSearchProgressBar();
    }

    @Test
    public void Search_WhenSFailure_ShowActionFailedMessage() {

        //arrange
        when(whirlpoolRestServiceMock.SearchThreads(1, 1, "q")).thenReturn(Observable.error(new IOException()));

        //act
        controller.Search("q", 1, 1);

        //assert
        verify(searchResultFragmentMock).ShowActionFailedMessage();
        verify(searchResultFragmentMock).HideSearchProgressBar();
    }
}
