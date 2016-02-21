package com.android.nitecafe.whirlpoolnews.controllers;

import com.android.nitecafe.whirlpoolnews.ui.interfaces.ILoginFragment;
import com.android.nitecafe.whirlpoolnews.web.interfaces.IWatchedThreadService;
import com.android.nitecafe.whirlpoolnews.web.interfaces.IWhirlpoolRestClient;
import com.android.nitecafe.whirlpoolnews.web.interfaces.IWhirlpoolRestService;

import javax.inject.Inject;
import javax.inject.Named;

import rx.subjects.PublishSubject;

public class LoginController {

    private ILoginFragment loginFragment;
    private IWhirlpoolRestClient whirlpoolRestClient;
    private IWhirlpoolRestService mWhirlpoolRestService;
    private IWatchedThreadService watchedThreadIdentifier;
    private PublishSubject<Void> whimSubject;

    @Inject
    public LoginController(IWhirlpoolRestClient whirlpoolRestClient,
                           IWhirlpoolRestService whirlpoolRestService,
                           IWatchedThreadService watchedThreadIdentifier,
                           @Named("whim") PublishSubject<Void> whimSubject) {
        this.whirlpoolRestClient = whirlpoolRestClient;
        mWhirlpoolRestService = whirlpoolRestService;
        this.watchedThreadIdentifier = watchedThreadIdentifier;
        this.whimSubject = whimSubject;
    }

    public void attachedView(ILoginFragment view) {
        loginFragment = view;
    }

    public void login(String apiKey) {
        if (loginFragment != null) loginFragment.showLoggingInProgressLoader();
        whirlpoolRestClient.setApiKey(apiKey);
        GetUserDetails();
    }

    private void updateAppWithUserData() {
        watchedThreadIdentifier.getWatchedThreads(); // refresh watched threads
        whimSubject.onNext(null);
    }

    private void GetUserDetails() {
        mWhirlpoolRestService.GetUserDetails().map(userDetailses ->
                userDetailses.getUSER().getNAME())
                .subscribe(
                        s -> {
                            if (loginFragment != null) {
                                whirlpoolRestClient.saveUserName(s);
                                loginFragment.updateUsername(s);
                                updateAppWithUserData();
                                loginFragment.hideProgressLoader();
                                loginFragment.showLoginSucessMessage(s);
                                loginFragment.showHomeScreen();
                            }
                        },
                        throwable ->
                        {
                            if (loginFragment != null) {
                                loginFragment.showLoginFailureMessage();
                                loginFragment.hideProgressLoader();
                            }
                        });
    }

}
