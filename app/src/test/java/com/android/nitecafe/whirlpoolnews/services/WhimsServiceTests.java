package com.android.nitecafe.whirlpoolnews.services;


import com.android.nitecafe.whirlpoolnews.TestSchedulerManager;
import com.android.nitecafe.whirlpoolnews.models.Whim;
import com.android.nitecafe.whirlpoolnews.models.WhimsList;
import com.android.nitecafe.whirlpoolnews.web.WhimsService;
import com.android.nitecafe.whirlpoolnews.web.interfaces.IWhirlpoolRestClient;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;

import rx.Observable;
import rx.observers.TestObserver;

@RunWith(MockitoJUnitRunner.class)
public class WhimsServiceTests {

    @Mock IWhirlpoolRestClient whirlpoolRestClient;
    private TestSchedulerManager testSchedulerManager;
    private WhimsService _service;

    @Before
    public void setUp() {
        testSchedulerManager = new TestSchedulerManager();
        _service = new WhimsService(whirlpoolRestClient, testSchedulerManager);
    }

    @Test
    public void GetNumberOfUnreadWhims_WhenNoWhims_Return0() {

        //arrange
        TestObserver<Integer> testObserver = new TestObserver<>();
        WhimsList whimsList = new WhimsList();
        Mockito.when(whirlpoolRestClient.GetWhims()).thenReturn(Observable.just(whimsList));

        //act
        _service.GetNumberOfUnreadWhims().subscribe(testObserver);
        testSchedulerManager.testScheduler.triggerActions();

        //assert
        testObserver.assertReceivedOnNext(Arrays.asList(0));
    }

    @Test
    public void GetNumberOfUnreadWhims_WhenOneReadWhims_Return0() {

        //arrange
        TestObserver<Integer> testObserver = new TestObserver<>();
        WhimsList whimsList = new WhimsList();
        Whim whim = new Whim();
        whim.setVIEWED(1);
        whimsList.setWHIMS(Arrays.asList(whim));
        Mockito.when(whirlpoolRestClient.GetWhims()).thenReturn(Observable.just(whimsList));

        //act
        _service.GetNumberOfUnreadWhims().subscribe(testObserver);
        testSchedulerManager.testScheduler.triggerActions();

        //assert
        testObserver.assertReceivedOnNext(Arrays.asList(0));
    }

    @Test
    public void GetNumberOfUnreadWhims_WhenOneReadAndOneUnreadWhims_Return1() {

        //arrange
        TestObserver<Integer> testObserver = new TestObserver<>();
        WhimsList whimsList = new WhimsList();
        Whim whim = new Whim();
        whim.setVIEWED(1);
        Whim whim2 = new Whim();
        whim2.setVIEWED(0);
        whimsList.setWHIMS(Arrays.asList(whim, whim2));
        Mockito.when(whirlpoolRestClient.GetWhims()).thenReturn(Observable.just(whimsList));

        //act
        _service.GetNumberOfUnreadWhims().subscribe(testObserver);
        testSchedulerManager.testScheduler.triggerActions();

        //assert
        testObserver.assertReceivedOnNext(Arrays.asList(1));
    }

    @Test
    public void GetNumberOfUnreadWhims_WhenTwoNewWhims_Return2() {

        //arrange
        TestObserver<Integer> testObserver = new TestObserver<>();
        WhimsList whimsList = new WhimsList();
        Whim whim1 = new Whim();
        whim1.setVIEWED(0);
        Whim whim2 = new Whim();
        whim2.setVIEWED(0);
        whimsList.setWHIMS(Arrays.asList(whim1, whim2));
        Mockito.when(whirlpoolRestClient.GetWhims()).thenReturn(Observable.just(whimsList));

        //act
        _service.GetNumberOfUnreadWhims().subscribe(testObserver);
        testSchedulerManager.testScheduler.triggerActions();

        //assert
        testObserver.assertReceivedOnNext(Arrays.asList(2));
    }

}
