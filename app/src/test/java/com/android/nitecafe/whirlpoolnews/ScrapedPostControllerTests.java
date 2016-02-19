package com.android.nitecafe.whirlpoolnews;

import com.android.nitecafe.whirlpoolnews.controllers.ScrapedPostController;
import com.android.nitecafe.whirlpoolnews.models.ScrapedPostList;
import com.android.nitecafe.whirlpoolnews.ui.interfaces.IScrapedPostFragment;
import com.android.nitecafe.whirlpoolnews.web.interfaces.IWatchedThreadService;
import com.android.nitecafe.whirlpoolnews.web.interfaces.IWhirlpoolRestService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import rx.Observable;

@RunWith(MockitoJUnitRunner.class)
public class ScrapedPostControllerTests {

    @Mock IWhirlpoolRestService whirlpoolRestClientMock;
    @Mock IScrapedPostFragment mIScrapedPostFragmentMock;
    @Mock IWatchedThreadService watchedThreadIdentifier;
    private ScrapedPostController _controller;

    @Before
    public void setUp() {
        _controller = new ScrapedPostController(whirlpoolRestClientMock, watchedThreadIdentifier);
        _controller.attach(mIScrapedPostFragmentMock);
    }

    @Test
    public void GetScrapedPosts_LoadPage3_ServiceCallExpects3() {

        //arrange
        int threadId = 111;
        int pageToLoad = 3;
        Mockito.when(whirlpoolRestClientMock.GetScrapedPosts(threadId, pageToLoad))
                .thenReturn(Observable.<ScrapedPostList>empty());

        //act
        _controller.GetScrapedPosts(threadId, pageToLoad);

        //assert
        Mockito.verify(whirlpoolRestClientMock).GetScrapedPosts(threadId, pageToLoad);
    }

    @Test(expected = IllegalArgumentException.class)
    public void GetScrapedPosts_InvalidPageNumber_ExpectsException() {

        //arrange
        int threadId = 111;
        int pageToLoad = -1;

        //act
        _controller.GetScrapedPosts(threadId, pageToLoad);
    }

    @Test(expected = IllegalArgumentException.class)
    public void GetScrapedPosts_InvalidThreadId_ExpectsException() {

        //arrange
        int threadId = -111;
        int pageToLoad = 3;

        //act
        _controller.GetScrapedPosts(threadId, pageToLoad);
    }

    @Test
    public void loadNextPage_PageIs1_NextPageShouldBe2() {

        //arrange
        int threadId = 111;
        int pageToLoad = 1;
        Mockito.when(whirlpoolRestClientMock.GetScrapedPosts(threadId, pageToLoad))
                .thenReturn(Observable.<ScrapedPostList>empty());
        Mockito.when(whirlpoolRestClientMock.GetScrapedPosts(threadId, pageToLoad + 1))
                .thenReturn(Observable.<ScrapedPostList>empty());
        _controller.GetScrapedPosts(threadId, pageToLoad);

        //act
        _controller.loadNextPage(threadId);

        //assert
        Mockito.verify(whirlpoolRestClientMock).GetScrapedPosts(threadId, pageToLoad + 1);
    }

    @Test
    public void loadPreviousPage_PageIs3_PreviousPageShouldBe2() {

        //arrange
        int threadId = 111;
        int pageToLoad = 3;
        Mockito.when(whirlpoolRestClientMock.GetScrapedPosts(threadId, pageToLoad))
                .thenReturn(Observable.<ScrapedPostList>empty());
        Mockito.when(whirlpoolRestClientMock.GetScrapedPosts(threadId, pageToLoad - 1))
                .thenReturn(Observable.<ScrapedPostList>empty());
        _controller.GetScrapedPosts(threadId, pageToLoad);

        //act
        _controller.loadPreviousPage(threadId);

        //assert
        Mockito.verify(whirlpoolRestClientMock).GetScrapedPosts(threadId, pageToLoad - 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void loadNextPage_PageIsLastPage_ExpectException() {

        //arrange
        int threadId = 111;
        int pageToLoad = 3;
        ScrapedPostList postList = new ScrapedPostList(threadId, "Thread Title");
        postList.setPageCount(3);
        Mockito.when(whirlpoolRestClientMock.GetScrapedPosts(threadId, pageToLoad))
                .thenReturn(Observable.just(postList));
        _controller.GetScrapedPosts(threadId, pageToLoad);

        //act
        _controller.loadNextPage(threadId);
    }

    @Test(expected = IllegalArgumentException.class)
    public void loadPreviousPage_PageIsFirstPage_ExpectException() {

        //arrange
        int threadId = 111;
        int pageToLoad = 1;
        Mockito.when(whirlpoolRestClientMock.GetScrapedPosts(threadId, pageToLoad))
                .thenReturn(Observable.<ScrapedPostList>empty());
        _controller.GetScrapedPosts(threadId, pageToLoad);

        //act
        _controller.loadPreviousPage(threadId);
    }
}
