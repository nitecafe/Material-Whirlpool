package com.android.nitecafe.whirlpoolnews;


import com.android.nitecafe.whirlpoolnews.controllers.ScrapedThreadController;
import com.android.nitecafe.whirlpoolnews.interfaces.IWhirlpoolRestClient;
import com.android.nitecafe.whirlpoolnews.models.ScrapedThreadList;
import com.android.nitecafe.whirlpoolnews.ui.interfaces.IScrapedThreadFragment;
import com.android.nitecafe.whirlpoolnews.utilities.IWatchedThreadIdentifier;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import rx.Observable;

@RunWith(MockitoJUnitRunner.class)
public class ScrapedThreadControllerTests {

    @Mock IWhirlpoolRestClient mIWhirlpoolRestClientMock;
    @Mock IWatchedThreadIdentifier mIWatchedThreadIdentifierMock;
    @Mock IScrapedThreadFragment mFragmentMock;
    private ScrapedThreadController controller;
    private TestSchedulerManager testSchedulerManager;

    @Before
    public void Setup() {
        testSchedulerManager = new TestSchedulerManager();
        controller = new ScrapedThreadController(mIWhirlpoolRestClientMock, testSchedulerManager, mIWatchedThreadIdentifierMock);
        controller.Attach(mFragmentMock);
    }

    @Test
    public void GetScrapedThreads_WhenCalled_CallRestClient() {

        //arrange
        int forumId = 111;
        int groupId = 222;
        Mockito.when(mIWhirlpoolRestClientMock.GetScrapedThreads(forumId, 1, groupId)).thenReturn(Observable.<ScrapedThreadList>empty());

        //act
        controller.GetScrapedThreads(forumId, groupId);

        //assert
        Mockito.verify(mIWhirlpoolRestClientMock).GetScrapedThreads(forumId, 1, groupId);
    }

    @Test(expected = IllegalArgumentException.class)
    public void GetScrapedThreads_InvalidThreadId_ThrowIllegalArgumentException() {

        //arrange
        int forumId = -1;
        int groupId = 222;

        //act
        controller.GetScrapedThreads(forumId, groupId);
    }

    @Test
    public void GetTotalPage_LastPageIs100_Return100() {

        //arrange
        int forumId = 111;
        int groupId = 222;
        int pageCount = 100;
        ScrapedThreadList scrapedThreadList = new ScrapedThreadList(forumId, "Title");
        scrapedThreadList.setPageCount(pageCount);
        Mockito.when(mIWhirlpoolRestClientMock.GetScrapedThreads(forumId, 1, groupId)).thenReturn(Observable.just(scrapedThreadList));

        //act
        controller.GetScrapedThreads(forumId, groupId);
        testSchedulerManager.testScheduler.triggerActions();

        //assert
        Assert.assertEquals(pageCount, controller.getTotalPage());
    }
}
