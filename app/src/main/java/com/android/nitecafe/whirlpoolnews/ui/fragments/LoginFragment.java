package com.android.nitecafe.whirlpoolnews.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.afollestad.materialdialogs.MaterialDialog;
import com.android.nitecafe.whirlpoolnews.R;
import com.android.nitecafe.whirlpoolnews.WhirlpoolApp;
import com.android.nitecafe.whirlpoolnews.controllers.LoginController;
import com.android.nitecafe.whirlpoolnews.ui.interfaces.ILoginFragment;
import com.jakewharton.rxbinding.widget.RxTextView;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.subjects.PublishSubject;

public class LoginFragment extends BaseFragment implements ILoginFragment {

    public PublishSubject<String> UserNameSubject = PublishSubject.create();
    @Bind(R.id.input_apikey) EditText mApiKeyText;
    @Bind(R.id.btn_login) AppCompatButton saveButton;
    @Inject LoginController mLoginController;
    private OnShowHomeScreenListener listener;
    private MaterialDialog progressLoader;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View inflate = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.bind(this, inflate);
        ((WhirlpoolApp) getActivity().getApplication()).getDaggerComponent().inject(this);
        setToolbarTitle("");
        mLoginController.attachedView(this);

        RxTextView.textChangeEvents(mApiKeyText).subscribe(event -> {
            if (event.text().length() > 3)
                saveButton.setEnabled(true);
            else
                saveButton.setEnabled(false);
        });

        progressLoader = new MaterialDialog.Builder(getContext())
                .title("Logging In")
                .content("Verifying API Key")
                .progress(true, 0)
                .build();

        return inflate;
    }

    @Override
    public void onDestroyView() {
        mLoginController.attachedView(null);
        super.onDestroyView();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnShowHomeScreenListener)
            listener = (OnShowHomeScreenListener) context;
        else
            throw new ClassCastException("Activity must implement OnShowHomeScreenListener");
    }

    @Override
    public void onDetach() {
        listener = null;
        UserNameSubject.onCompleted();
        super.onDetach();
    }

    @OnClick(R.id.btn_login)
    public void login() {
        mLoginController.login(mApiKeyText.getText().toString());
    }

    @Override
    public void showHomeScreen() {
        listener.showHomeScreen();
    }

    @Override
    public void updateUsername(String s) {
        UserNameSubject.onNext(s);
    }

    @Override
    public void showLoginFailureMessage() {
        Snackbar.make(getView(), R.string.message_login_failure, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showLoggingInProgressLoader() {
        progressLoader.show();
    }

    @Override
    public void hideProgressLoader() {
        progressLoader.hide();
    }

    @Override
    public void showLoginSucessMessage(String username) {
        Snackbar.make(getView(), "Welcome " + username + ". You are awesome for using this app.", Snackbar.LENGTH_LONG).show();
    }

    public interface OnShowHomeScreenListener {

        void showHomeScreen();
    }
}
