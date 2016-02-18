package com.android.nitecafe.whirlpoolnews;

import com.android.nitecafe.whirlpoolnews.controllers.PopularThreadsController;
import com.android.nitecafe.whirlpoolnews.interfaces.IWhirlpoolRestClient;
import com.android.nitecafe.whirlpoolnews.ui.interfaces.IPopularFragment;
import com.android.nitecafe.whirlpoolnews.utilities.IWatchedThreadIdentifier;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import rx.Observable;

@RunWith(MockitoJUnitRunner.class)
public class PopularThreadsControllerTests {

    @Mock IWhirlpoolRestClient mIWhirlpoolRestClientMock;
    @Mock IWatchedThreadIdentifier mIWatchedThreadIdentifierMock;
    @Mock IPopularFragment mIPopularFragmentMock;
    private PopularThreadsController controller;
    private TestSchedulerManager testSchedulerManager;


    @Before
    public void Setup() {
        testSchedulerManager = new TestSchedulerManager();
        controller = new PopularThreadsController(mIWhirlpoolRestClientMock, testSchedulerManager, mIWatchedThreadIdentifierMock);
        controller.Attach(mIPopularFragmentMock);
    }

    @Test
    public void WatchThread_WhenCalled_ShowThreadWatchedSuccessMessage() {

        //arrange
        int threadId = 111;

        Mockito.when(mIWhirlpoolRestClientMock.SetThreadAsWatch(threadId)).thenReturn(Observable.just(null));

        //act
        controller.WatchThread(threadId);
        testSchedulerManager.testScheduler.triggerActions();

        //assert
        Mockito.verify(mIPopularFragmentMock).ShowThreadWatchedSuccessMessage();
    }

}
