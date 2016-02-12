package com.android.nitecafe.whirlpoolnews;

import com.android.nitecafe.whirlpoolnews.interfaces.IWhirlpoolRestClient;
import com.android.nitecafe.whirlpoolnews.utilities.WatchedThreadIdentifier;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class WatchedThreadIdentifierTests {

    @Mock IWhirlpoolRestClient whirlpoolRestClient;
    private WatchedThreadIdentifier _classToTest;

    @Before
    public void setUp() {
        TestSchedulerManager testSchedulerManager = new TestSchedulerManager();
        _classToTest = new WatchedThreadIdentifier(whirlpoolRestClient, testSchedulerManager);
    }

    @Test
    public void IsThreadWatched_ThreadIsWatched_ReturnTrue() {

        //arrange
        int threadId = 1111;
        List<Integer> watchedThreadList = new ArrayList<>();
        watchedThreadList.add(threadId);
        _classToTest.setWatchedThreads(watchedThreadList);

        //act
        boolean threadWatched = _classToTest.isThreadWatched(threadId);

        //assert
        Assert.assertTrue(threadWatched);
    }

    @Test
    public void IsThreadWatched_ThreadNotWatched_ReturnFalse() {

        //arrange
        int threadId = 1111;

        //act
        boolean threadWatched = _classToTest.isThreadWatched(threadId);

        //assert
        Assert.assertFalse(threadWatched);
    }
}
