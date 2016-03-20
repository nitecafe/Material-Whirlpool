package com.android.nitecafe.whirlpoolnews.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.android.nitecafe.whirlpoolnews.R;
import com.android.nitecafe.whirlpoolnews.WhirlpoolApp;
import com.android.nitecafe.whirlpoolnews.ui.interfaces.IOnSearchClicked;
import com.android.nitecafe.whirlpoolnews.ui.interfaces.IOnThreadClicked;
import com.jakewharton.rxbinding.widget.RxTextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SearchFragment extends BaseFragment {

    @Bind(R.id.input_search_query) EditText mQueryText;
    @Bind(R.id.btn_search) AppCompatButton searchButton;
    private IOnSearchClicked listener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View inflate = inflater.inflate(R.layout.fragment_search, container, false);
        ButterKnife.bind(this, inflate);
        ((WhirlpoolApp) getActivity().getApplication()).getDaggerComponent().inject(this);
        setToolbarTitle("");

        mSubscriptions.add(RxTextView.textChangeEvents(mQueryText).subscribe(event -> {
            if (event.text().length() > 1)
                searchButton.setEnabled(true);
            else
                searchButton.setEnabled(false);
        }));

        return inflate;
    }

    @Override
    public void onResume() {
        super.onResume();
        WhirlpoolApp.getInstance().trackScreenView("Search Fragment");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof IOnThreadClicked)
            listener = (IOnSearchClicked) context;
        else
            throw new ClassCastException("Activity must implement IOnSearchClicked");
    }

    @Override
    public void onDetach() {
        listener = null;
        super.onDetach();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setToolbarTitle(getActivity().getString(R.string.title_search));
    }

    @OnClick(R.id.btn_search)
    public void Search() {
        listener.onSearchClicked(mQueryText.getText().toString(), 0, -1);
    }
}
