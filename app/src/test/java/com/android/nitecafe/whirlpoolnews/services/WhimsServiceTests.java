package com.android.nitecafe.whirlpoolnews.services;


import com.android.nitecafe.whirlpoolnews.models.Whim;
import com.android.nitecafe.whirlpoolnews.models.WhimsList;
import com.android.nitecafe.whirlpoolnews.web.WhimsService;
import com.android.nitecafe.whirlpoolnews.web.interfaces.IWhirlpoolRestClient;
import com.android.nitecafe.whirlpoolnews.web.interfaces.IWhirlpoolRestService;

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

    @Mock IWhirlpoolRestService whirlpoolRestService;
    @Mock IWhirlpoolRestClient whirlpoolRestClientMock;
    private WhimsService _service;

    @Before
    public void setUp() {
        _service = new WhimsService(whirlpoolRestService, whirlpoolRestClientMock);
    }

    @Test
    public void GetNumberOfUnreadWhims_WhenNoWhims_Return0() {

        //arrange
        TestObserver<Integer> testObserver = new TestObserver<>();
        WhimsList whimsList = new WhimsList();
        Mockito.when(whirlpoolRestService.GetWhims()).thenReturn(Observable.just(whimsList));

        //act
        _service.GetNumberOfUnreadWhims().subscribe(testObserver);

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
        Mockito.when(whirlpoolRestService.GetWhims()).thenReturn(Observable.just(whimsList));

        //act
        _service.GetNumberOfUnreadWhims().subscribe(testObserver);

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
        Mockito.when(whirlpoolRestService.GetWhims()).thenReturn(Observable.just(whimsList));

        //act
        _service.GetNumberOfUnreadWhims().subscribe(testObserver);

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
        Mockito.when(whirlpoolRestService.GetWhims()).thenReturn(Observable.just(whimsList));

        //act
        _service.GetNumberOfUnreadWhims().subscribe(testObserver);

        //assert
        testObserver.assertReceivedOnNext(Arrays.asList(2));
    }

    @Test
    public void GetUnreadWhimsInInterval_WhenWithinInterval_ReturnWhim() {

        //arrange
//        long interval = 60 * 1000;
//        TestObserver<List<Whim>> testObserver = new TestObserver<>();
//        WhimsList whimsList = new WhimsList();
//        Whim whim1 = new Whim();
//        whim1.setVIEWED(0);
//
//        SimpleDateFormat date_format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss+SSSS", Locale.US);
//        Calendar instance = Calendar.getInstance();
//        String format = date_format.format(instance.getTime());
//        Date localDateFromString = WhirlpoolDateUtils.getLocalDateFromString(format);
//        whim1.setDATE(date_format.format(localDateFromString));
//        whimsList.setWHIMS(Arrays.asList(whim1));
//        Mockito.when(whirlpoolRestClientMock.GetWhims()).thenReturn(Observable.just(whimsList));
//
//        //act
//        _service.GetUnreadWhimsInInterval(interval).subscribe(testObserver);
//
//        //assert
//        List<List<Whim>> onNextEvents = testObserver.getOnNextEvents();
//        Assert.assertEquals(whim1, onNextEvents.get(0).get(0));
    }

}
