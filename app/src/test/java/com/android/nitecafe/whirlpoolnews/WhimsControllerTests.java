package com.android.nitecafe.whirlpoolnews;

import com.android.nitecafe.whirlpoolnews.controllers.WhimsController;
import com.android.nitecafe.whirlpoolnews.interfaces.IWhirlpoolRestClient;
import com.android.nitecafe.whirlpoolnews.models.WhimsList;
import com.android.nitecafe.whirlpoolnews.ui.interfaces.IWhimsFragment;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;

import rx.Observable;
import rx.observers.TestSubscriber;
import rx.subjects.PublishSubject;

@RunWith(MockitoJUnitRunner.class)
public class WhimsControllerTests {

    @Mock IWhirlpoolRestClient whirlpoolRestClientMock;
    @Mock IWhimsFragment whimsFragmentMock;
    private WhimsController _controller;
    private TestSchedulerManager testSchedulerManager;
    private PublishSubject<Void> whimSubject;


    @Before
    public void setUp() {
        testSchedulerManager = new TestSchedulerManager();
        whimSubject = PublishSubject.create();
        _controller = new WhimsController(whirlpoolRestClientMock, testSchedulerManager, whimSubject);
        _controller.Attach(whimsFragmentMock);
    }

    @Test
    public void GetWhims_WhenCalled_RestClientServiceMethodCalled() {

        //arrange
        Mockito.when(whirlpoolRestClientMock.GetWhims()).thenReturn(Observable.<WhimsList>empty());

        //act
        _controller.GetWhims();

        //assert
        Mockito.verify(whirlpoolRestClientMock).GetWhims();
    }

    @Test
    public void GetWhims_WhenError_CallDisplayErrorMessage() {

        //arrange
        Mockito.when(whirlpoolRestClientMock.GetWhims()).thenReturn(Observable.error(new IOException()));

        //act
        _controller.GetWhims();
        testSchedulerManager.testScheduler.triggerActions();

        //assert
        Mockito.verify(whimsFragmentMock).ShowErrorMessage();
    }

    @Test
    public void GetWhims_WhenSuccess_DisplayWhims() {

        //arrange
        WhimsList whimsList = new WhimsList();
        Mockito.when(whirlpoolRestClientMock.GetWhims()).thenReturn(Observable.just(whimsList));

        //act
        _controller.GetWhims();
        testSchedulerManager.testScheduler.triggerActions();

        //assert
        Mockito.verify(whimsFragmentMock).DisplayWhims(whimsList.getWHIMS());
    }

    @Test
    public void GetWhims_WhenSuccess_HideProgressLoader() {

        //arrange
        WhimsList whimsList = new WhimsList();
        Mockito.when(whirlpoolRestClientMock.GetWhims()).thenReturn(Observable.just(whimsList));

        //act
        _controller.GetWhims();
        testSchedulerManager.testScheduler.triggerActions();

        //assert
        Mockito.verify(whimsFragmentMock).HideAllProgressLoader();
    }

    @Test
    public void MarkWhimAsRead_WhenCalled_RestClientCalled() {

        //arrange
        int whimId = 111;
        Mockito.when(whirlpoolRestClientMock.MarkWhimAsRead(whimId)).thenReturn(Observable.<Void>empty());

        //act
        _controller.MarkWhimAsRead(whimId);

        //assert
        Mockito.verify(whirlpoolRestClientMock).MarkWhimAsRead(whimId);
    }

    @Test
    public void MarkWhimAsRead_WhenSuccess_TriggerWhimSubject() {

        //arrange
        int whimId = 111;
        TestSubscriber<Void> testObserver = new TestSubscriber<>();
        whimSubject.subscribe(testObserver);
        Mockito.when(whirlpoolRestClientMock.MarkWhimAsRead(whimId)).thenReturn(Observable.just(null));

        //act
        _controller.MarkWhimAsRead(whimId);
        testSchedulerManager.testScheduler.triggerActions();

        //assert
        testObserver.assertValue(null);
    }

}
