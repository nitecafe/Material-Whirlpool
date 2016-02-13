package com.android.nitecafe.whirlpoolnews;

import android.content.SharedPreferences;

import com.android.nitecafe.whirlpoolnews.constants.StringConstants;
import com.android.nitecafe.whirlpoolnews.controllers.LoginController;
import com.android.nitecafe.whirlpoolnews.interfaces.IWhirlpoolRestClient;
import com.android.nitecafe.whirlpoolnews.ui.interfaces.ILoginFragment;
import com.android.nitecafe.whirlpoolnews.utilities.IWatchedThreadIdentifier;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class LoginControllerTests {

    @Mock ILoginFragment loginFragmentMock;
    @Mock IWhirlpoolRestClient whirlpoolRestClientMock;
    @Mock SharedPreferences sharedPreferencesMock;
    @Mock IWatchedThreadIdentifier watchedThreadIdentifier;
    @Mock SharedPreferences.Editor editorMock;
    private LoginController mLoginController;

    @Before
    public void setup() {
        mLoginController = new LoginController(whirlpoolRestClientMock, sharedPreferencesMock, watchedThreadIdentifier);
        Mockito.when(sharedPreferencesMock.edit()).thenReturn(editorMock);
        mLoginController.attachedView(loginFragmentMock);
    }

    @Test
    public void Login_WhenCalled_SetApiKeyInRestClient() {

        //arrange
        String apiKey = "1111-1111";

        //act
        mLoginController.login(apiKey);

        //assert
        Mockito.verify(whirlpoolRestClientMock).setApiKey(apiKey);
    }

    @Test
    public void Login_WhenCalled_SavesToSharedPreference() {

        //arrange
        String apiKey = "1111-1111";

        //act
        mLoginController.login(apiKey);

        //assert
        Mockito.verify(editorMock).putString(StringConstants.API_PREFERENCE_KEY, apiKey);
    }

    @Test
    public void Login_WhenCalled_ShowSavedMessage() {

        //arrange
        String apiKey = "1111-1111";

        //act
        mLoginController.login(apiKey);

        //assert
        Mockito.verify(loginFragmentMock).showSavedMessage();
    }

    @Test
    public void Login_WhenCalled_ShowsHomeScreen() {

        //arrange
        String apiKey = "1111-1111";

        //act
        mLoginController.login(apiKey);

        //assert
        Mockito.verify(loginFragmentMock).showHomeScreen();
    }
}
