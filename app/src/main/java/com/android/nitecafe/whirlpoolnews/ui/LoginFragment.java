package com.android.nitecafe.whirlpoolnews.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.android.nitecafe.whirlpoolnews.R;
import com.android.nitecafe.whirlpoolnews.WhirlpoolApp;
import com.android.nitecafe.whirlpoolnews.controllers.LoginController;
import com.android.nitecafe.whirlpoolnews.interfaces.ILoginFragment;
import com.jakewharton.rxbinding.widget.RxTextView;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginFragment extends BaseFragment implements ILoginFragment {

    @Bind(R.id.input_apikey) EditText mApiKeyText;
    @Bind(R.id.btn_login) AppCompatButton saveButton;
    @Inject LoginController mLoginController;
    private OnShowHomeScreenListener listener;

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

    @Override public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnShowHomeScreenListener)
            listener = (OnShowHomeScreenListener) context;
    }

    @Override public void onDetach() {
        listener = null;
        super.onDetach();
    }

    @OnClick(R.id.btn_login)
    public void login() {
        mLoginController.login(mApiKeyText.getText().toString());
    }

    @Override public void showSavedMessage() {
        Snackbar.make(getView(), "API Key saved successfully.", Snackbar.LENGTH_SHORT).show();
    }

    @Override public void showHomeScreen() {
        listener.showHomeScreen();
    }

    public interface OnShowHomeScreenListener {

        void showHomeScreen();
    }
}
