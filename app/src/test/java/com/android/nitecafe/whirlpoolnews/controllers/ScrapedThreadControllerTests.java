package com.android.nitecafe.whirlpoolnews.controllers;


import com.android.nitecafe.whirlpoolnews.models.ScrapedThread;
import com.android.nitecafe.whirlpoolnews.models.ScrapedThreadList;
import com.android.nitecafe.whirlpoolnews.ui.interfaces.IScrapedThreadFragment;
import com.android.nitecafe.whirlpoolnews.web.interfaces.IWatchedThreadService;
import com.android.nitecafe.whirlpoolnews.web.interfaces.IWhirlpoolRestService;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import rx.Observable;

@RunWith(MockitoJUnitRunner.class)
public class ScrapedThreadControllerTests {

    @Mock IWhirlpoolRestService mIWhirlpoolRestClientMock;
    @Mock IWatchedThreadService mIWatchedThreadServiceMock;
    @Mock IScrapedThreadFragment mFragmentMock;
    private ScrapedThreadController controller;

    @Before
    public void Setup() {
        controller = new ScrapedThreadController(mIWhirlpoolRestClientMock, mIWatchedThreadServiceMock);
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

    @Test
    public void GetScrapedThreads_WhenOnlyThreadGroup_SetupSpinnerWithOneGroups() {

        //arrange
        int forumId = 111;
        int groupId = 222;
        ScrapedThreadList threadList = new ScrapedThreadList(forumId, "forum");
        Map<String, Integer> groupsMap = new HashMap<>();
        groupsMap.put("NBN", 123);
        threadList.setGroups(groupsMap);
        ScrapedThread scrapedThread = new ScrapedThread(1, "title", new Date(), "Graham", "forum", forumId);
        ArrayList<ScrapedThread> scrapedThreads = new ArrayList<>();
        scrapedThreads.add(scrapedThread);
        threadList.setThreads(scrapedThreads);
        Mockito.when(mIWhirlpoolRestClientMock.GetScrapedThreads(forumId, 1, groupId)).
                thenReturn(Observable.just(threadList));

        //act
        controller.GetScrapedThreads(forumId, groupId);

        //assert
        ArgumentCaptor<HashMap> captor = ArgumentCaptor.forClass(HashMap.class);
        Mockito.verify(mFragmentMock).SetupGroupSpinnerDropDown(captor.capture(), Mockito.anyInt());
        Assert.assertEquals(1, captor.getValue().size());
    }

    @Test
    public void GetScrapedThreads_WhenNoThreadGroup_SetupSpinnerWithNoGroups() {

        //arrange
        int forumId = 111;
        int groupId = 222;
        ScrapedThreadList threadList = new ScrapedThreadList(forumId, "forum");
        Map<String, Integer> groupsMap = new HashMap<>();
        threadList.setGroups(groupsMap);
        ScrapedThread scrapedThread = new ScrapedThread(1, "title", new Date(), "Graham", "forum", forumId);
        ArrayList<ScrapedThread> scrapedThreads = new ArrayList<>();
        scrapedThreads.add(scrapedThread);
        threadList.setThreads(scrapedThreads);
        Mockito.when(mIWhirlpoolRestClientMock.GetScrapedThreads(forumId, 1, groupId)).
                thenReturn(Observable.just(threadList));

        //act
        controller.GetScrapedThreads(forumId, groupId);

        //assert
        ArgumentCaptor<HashMap> captor = ArgumentCaptor.forClass(HashMap.class);
        Mockito.verify(mFragmentMock).SetupGroupSpinnerDropDown(captor.capture(), Mockito.anyInt());
        Assert.assertEquals(0, captor.getValue().size());

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

        //assert
        Assert.assertEquals(pageCount, controller.getTotalPage());
    }
}
