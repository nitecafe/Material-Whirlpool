package com.android.nitecafe.whirlpoolnews.services;

import android.content.SharedPreferences;

import com.android.nitecafe.whirlpoolnews.constants.StringConstants;
import com.android.nitecafe.whirlpoolnews.utilities.FavouriteThreadService;
import com.android.nitecafe.whirlpoolnews.utilities.interfaces.IObjectSerializer;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.HashMap;

import static org.mockito.Matchers.anyMapOf;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FavouriteThreadServiceTests {

    @Mock SharedPreferences sharedPreferencesMock;
    @Mock IObjectSerializer objectSerializerMock;
    @Mock SharedPreferences.Editor editorMock;
    private FavouriteThreadService serviceToTest;

    @Before
    public void Seteup() {
        String content = "";
        when(sharedPreferencesMock.getString(StringConstants.FAVOURITE_THREAD_KEY, "")).thenReturn(content);
        when(objectSerializerMock.deserializeHashMap(StringConstants.FAVOURITE_THREAD_KEY)).thenReturn(new HashMap<>());
        when(sharedPreferencesMock.edit()).thenReturn(editorMock);
        serviceToTest = new FavouriteThreadService(sharedPreferencesMock, objectSerializerMock);
    }

    @Test
    public void FavouriteThreadService_SavedFavouriteExist_RestoreFromSharedPreference() {

        //arrange
        String content = "Something";
        when(sharedPreferencesMock.getString(StringConstants.FAVOURITE_THREAD_KEY, "")).thenReturn(content);
        when(objectSerializerMock.deserializeHashMap(StringConstants.FAVOURITE_THREAD_KEY)).thenReturn(new HashMap<>());

        //act
        FavouriteThreadService service = new FavouriteThreadService(sharedPreferencesMock, objectSerializerMock);

        //assert
        verify(objectSerializerMock).deserializeHashMap(content);
    }

    @Test
    public void FavouriteThreadService_SavedFavouriteDoesntExist_DoNotRestoreFromSharedPreference() {

        //arrange
        String content = "";
        when(sharedPreferencesMock.getString(StringConstants.FAVOURITE_THREAD_KEY, "")).thenReturn(content);
        when(objectSerializerMock.deserializeHashMap(StringConstants.FAVOURITE_THREAD_KEY)).thenReturn(new HashMap<>());

        //act
        FavouriteThreadService service = new FavouriteThreadService(sharedPreferencesMock, objectSerializerMock);

        //assert
        verify(objectSerializerMock, never()).deserializeHashMap("");
    }

    @Test
    public void addThreadToFavourite_NewEntry_AddToSharedPreference() {

        int id = 1;
        String title = "title";

        //act
        serviceToTest.addThreadToFavourite(id, title);

        //assert
        verify(objectSerializerMock).serializeObject(anyMapOf(Integer.class, String.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void removeThreadFromFavourite_ForumDoesNotExist_ThrowException() {

        //arrange
        int id = 1;

        //act
        serviceToTest.removeThreadFromFavourite(id);
    }
}
