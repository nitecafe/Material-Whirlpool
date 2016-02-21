package com.android.nitecafe.whirlpoolnews;

import android.content.SharedPreferences;

import com.android.nitecafe.whirlpoolnews.controllers.LoginController;
import com.android.nitecafe.whirlpoolnews.models.UserDetails;
import com.android.nitecafe.whirlpoolnews.models.UserDetailsList;
import com.android.nitecafe.whirlpoolnews.ui.interfaces.ILoginFragment;
import com.android.nitecafe.whirlpoolnews.web.interfaces.IWatchedThreadService;
import com.android.nitecafe.whirlpoolnews.web.interfaces.IWhirlpoolRestClient;
import com.android.nitecafe.whirlpoolnews.web.interfaces.IWhirlpoolRestService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import rx.Observable;
import rx.observers.TestSubscriber;
import rx.subjects.PublishSubject;

@RunWith(MockitoJUnitRunner.class)
public class LoginControllerTests {

    @Mock ILoginFragment loginFragmentMock;
    @Mock IWhirlpoolRestClient whirlpoolRestClientMock;
    @Mock IWhirlpoolRestService mIWhirlpoolRestServiceMock;
    @Mock SharedPreferences sharedPreferencesMock;
    @Mock IWatchedThreadService watchedThreadIdentifier;
    @Mock SharedPreferences.Editor editorMock;
    private LoginController mLoginController;
    private PublishSubject<Void> whimSubject;

    @Before
    public void setup() {
        whimSubject = PublishSubject.create();
        mLoginController = new LoginController(whirlpoolRestClientMock, mIWhirlpoolRestServiceMock, watchedThreadIdentifier, whimSubject);
        Mockito.when(sharedPreferencesMock.edit()).thenReturn(editorMock);
        mLoginController.attachedView(loginFragmentMock);
    }

    @Test
    public void Login_WhenCalled_SetApiKeyInRestClient() {

        //arrange
        Mockito.when(mIWhirlpoolRestServiceMock.GetUserDetails()).thenReturn(Observable.<UserDetailsList>empty());
        String apiKey = "1111-1111";

        //act
        mLoginController.login(apiKey);

        //assert
        Mockito.verify(whirlpoolRestClientMock).setApiKey(apiKey);
    }

    @Test
    public void Login_WhenCalled_ShowProgressLoader() {

        //arrange
        Mockito.when(mIWhirlpoolRestServiceMock.GetUserDetails()).thenReturn(Observable.<UserDetailsList>empty());
        String apiKey = "1111-1111";

        //act
        mLoginController.login(apiKey);

        //assert
        Mockito.verify(loginFragmentMock).showLoggingInProgressLoader();
    }

    @Test
    public void Login_WhenCalled_GetUserDetails() {

        //arrange
        Mockito.when(mIWhirlpoolRestServiceMock.GetUserDetails()).thenReturn(Observable.<UserDetailsList>empty());
        String apiKey = "1111-1111";

        //act
        mLoginController.login(apiKey);

        //assert
        Mockito.verify(mIWhirlpoolRestServiceMock).GetUserDetails();
    }

    @Test
    public void Login_WhenSuccess_ShowsHomeScreen() {

        //arrange
        UserDetailsList list = new UserDetailsList();
        UserDetails userDetails = new UserDetails();
        userDetails.setNAME("David");
        list.setUSER(userDetails);
        Mockito.when(mIWhirlpoolRestServiceMock.GetUserDetails()).thenReturn(Observable.just(list));
        String apiKey = "1111-1111";

        //act
        mLoginController.login(apiKey);

        //assert
        Mockito.verify(loginFragmentMock).showHomeScreen();
    }

    @Test
    public void Login_WhenSuccess_UpdateUserName() {

        //arrange
        UserDetailsList list = new UserDetailsList();
        UserDetails userDetails = new UserDetails();
        userDetails.setNAME("David");
        list.setUSER(userDetails);
        Mockito.when(mIWhirlpoolRestServiceMock.GetUserDetails()).thenReturn(Observable.just(list));
        String apiKey = "1111-1111";

        //act
        mLoginController.login(apiKey);

        //assert
        Mockito.verify(loginFragmentMock).updateUsername("David");
    }

    @Test
    public void Login_WhenCalled_TriggerWhimSubject() {

        //arrange
        UserDetailsList list = new UserDetailsList();
        UserDetails userDetails = new UserDetails();
        userDetails.setNAME("David");
        list.setUSER(userDetails);
        Mockito.when(mIWhirlpoolRestServiceMock.GetUserDetails()).thenReturn(Observable.just(list));
        String apiKey = "1111-1111";
        TestSubscriber<Void> testSubscriber = new TestSubscriber<>();
        whimSubject.subscribe(testSubscriber);

        //act
        mLoginController.login(apiKey);

        //assert
        testSubscriber.assertValue(null);
    }
}
