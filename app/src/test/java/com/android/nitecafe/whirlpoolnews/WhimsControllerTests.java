package com.android.nitecafe.whirlpoolnews;

import com.android.nitecafe.whirlpoolnews.controllers.WhimsController;
import com.android.nitecafe.whirlpoolnews.models.WhimsList;
import com.android.nitecafe.whirlpoolnews.ui.interfaces.IWhimsFragment;
import com.android.nitecafe.whirlpoolnews.web.IWhirlpoolRestService;

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

    @Mock IWhirlpoolRestService whirlpoolRestService;
    @Mock IWhimsFragment whimsFragmentMock;
    private WhimsController _controller;
    private PublishSubject<Void> whimSubject;


    @Before
    public void setUp() {
        whimSubject = PublishSubject.create();
        _controller = new WhimsController(whirlpoolRestService, whimSubject);
        _controller.Attach(whimsFragmentMock);
    }

    @Test
    public void GetWhims_WhenCalled_RestClientServiceMethodCalled() {

        //arrange
        Mockito.when(whirlpoolRestService.GetWhims()).thenReturn(Observable.<WhimsList>empty());

        //act
        _controller.GetWhims();

        //assert
        Mockito.verify(whirlpoolRestService).GetWhims();
    }

    @Test
    public void GetWhims_WhenError_CallDisplayErrorMessage() {

        //arrange
        Mockito.when(whirlpoolRestService.GetWhims()).thenReturn(Observable.error(new IOException()));

        //act
        _controller.GetWhims();

        //assert
        Mockito.verify(whimsFragmentMock).ShowErrorMessage();
    }

    @Test
    public void GetWhims_WhenSuccess_DisplayWhims() {

        //arrange
        WhimsList whimsList = new WhimsList();
        Mockito.when(whirlpoolRestService.GetWhims()).thenReturn(Observable.just(whimsList));

        //act
        _controller.GetWhims();

        //assert
        Mockito.verify(whimsFragmentMock).DisplayWhims(whimsList.getWHIMS());
    }

    @Test
    public void GetWhims_WhenSuccess_HideProgressLoader() {

        //arrange
        WhimsList whimsList = new WhimsList();
        Mockito.when(whirlpoolRestService.GetWhims()).thenReturn(Observable.just(whimsList));

        //act
        _controller.GetWhims();

        //assert
        Mockito.verify(whimsFragmentMock).HideAllProgressLoader();
    }

    @Test
    public void MarkWhimAsRead_WhenCalled_RestClientCalled() {

        //arrange
        int whimId = 111;
        Mockito.when(whirlpoolRestService.MarkWhimAsRead(whimId)).thenReturn(Observable.<Void>empty());

        //act
        _controller.MarkWhimAsRead(whimId);

        //assert
        Mockito.verify(whirlpoolRestService).MarkWhimAsRead(whimId);
    }

    @Test
    public void MarkWhimAsRead_WhenSuccess_TriggerWhimSubject() {

        //arrange
        int whimId = 111;
        TestSubscriber<Void> testObserver = new TestSubscriber<>();
        whimSubject.subscribe(testObserver);
        Mockito.when(whirlpoolRestService.MarkWhimAsRead(whimId)).thenReturn(Observable.just(null));

        //act
        _controller.MarkWhimAsRead(whimId);

        //assert
        testObserver.assertValue(null);
    }

}
