package com.android.nitecafe.whirlpoolnews.controllers;

import com.android.nitecafe.whirlpoolnews.ui.interfaces.IScrapedPostParentFragment;
import com.android.nitecafe.whirlpoolnews.utilities.interfaces.IPreferencesGetter;
import com.android.nitecafe.whirlpoolnews.web.interfaces.IWatchedThreadService;
import com.android.nitecafe.whirlpoolnews.web.interfaces.IWhirlpoolRestService;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ScrapedPostParentControllerTests {

    @Mock IWhirlpoolRestService whirlpoolRestServiceMock;
    @Mock IWatchedThreadService watchedThreadServiceMock;
    @Mock IPreferencesGetter preferencesGetterMock;
    @Mock IScrapedPostParentFragment scrapedPostParentFragmentMock;
    private ScrapedPostParentController controller;

    @Before
    public void setup() {
        controller = new ScrapedPostParentController(whirlpoolRestServiceMock, watchedThreadServiceMock, preferencesGetterMock);
        controller.Attach(scrapedPostParentFragmentMock);
    }

    @Test
    public void IsThreadWatched_WhenCalled_CallThreadService() {

        //act
        controller.IsThreadWatched(1);

        //assert
        verify(watchedThreadServiceMock).isThreadWatched(1);
    }

    @Test
    public void shouldMarkThreadAsRead_WhenNewThreadIsWatchedAndAutoMarkIsOn_ReturnTrue() {

        //arrange
        when(watchedThreadServiceMock.isThreadWatched(1)).thenReturn(true);
        when(preferencesGetterMock.isAutoMarkAsReadLastPage()).thenReturn(true);

        //act
        boolean result = controller.shouldMarkThreadAsRead(1);

        //assert
        Assert.assertTrue(result);
    }

    @Test
    public void shouldMarkThreadAsRead_WhenNewThreadIsNotWatchedAndAutoMarkIsOn_ReturnTrue() {

        //arrange
        when(watchedThreadServiceMock.isThreadWatched(1)).thenReturn(false);
        when(preferencesGetterMock.isAutoMarkAsReadLastPage()).thenReturn(true);

        //act
        boolean result = controller.shouldMarkThreadAsRead(1);

        //assert
        Assert.assertFalse(result);
    }

    @Test
    public void shouldMarkThreadAsRead_WhenNewThreadIsWatchedAndAutoMarkIsOff_ReturnTrue() {

        //arrange
        when(watchedThreadServiceMock.isThreadWatched(1)).thenReturn(true);
        when(preferencesGetterMock.isAutoMarkAsReadLastPage()).thenReturn(false);

        //act
        boolean result = controller.shouldMarkThreadAsRead(1);

        //assert
        Assert.assertFalse(result);
    }

    @Test
    public void shouldMarkThreadAsRead_WhenNewThreadIsNotWatchedAndAutoMarkIsOff_ReturnTrue() {

        //arrange
        when(watchedThreadServiceMock.isThreadWatched(1)).thenReturn(false);
        when(preferencesGetterMock.isAutoMarkAsReadLastPage()).thenReturn(false);

        //act
        boolean result = controller.shouldMarkThreadAsRead(1);

        //assert
        Assert.assertFalse(result);
    }
}
