package com.android.nitecafe.whirlpoolnews.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.android.nitecafe.whirlpoolnews.R;
import com.android.nitecafe.whirlpoolnews.WhirlpoolApp;
import com.android.nitecafe.whirlpoolnews.controllers.LoginController;
import com.android.nitecafe.whirlpoolnews.interfaces.ILoginFragment;
import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.jakewharton.rxbinding.widget.TextViewTextChangeEvent;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;

public class LoginFragment extends BaseFragment implements ILoginFragment {

    @Bind(R.id.input_apikey) EditText mApiKeyText;
    @Bind(R.id.btn_login) AppCompatButton saveButton;
    @Inject LoginController mLoginController;

    @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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

        return inflate;
    }

    @OnClick(R.id.btn_login)
    public void login() {
        mLoginController.login(mApiKeyText.getText().toString());
    }

    @Override public void ShowErrorMessage() {

    }

    @Override public void ShowLoggingInLoader() {

    }

    @Override public void HideLoggingInLoadere() {

    }
}
