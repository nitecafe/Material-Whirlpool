package com.android.nitecafe.whirlpoolnews.ui.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.android.nitecafe.whirlpoolnews.R;
import com.android.nitecafe.whirlpoolnews.WhirlpoolApp;
import com.android.nitecafe.whirlpoolnews.controllers.SearchController;
import com.android.nitecafe.whirlpoolnews.models.ScrapedThread;
import com.android.nitecafe.whirlpoolnews.ui.interfaces.ISearchFragment;
import com.jakewharton.rxbinding.widget.RxTextView;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.subjects.BehaviorSubject;

public class SearchFragment extends BaseFragment implements ISearchFragment {

    @Bind(R.id.input_search_query) EditText mQueryText;
    @Bind(R.id.btn_search) AppCompatButton searchButton;
    @Inject SearchController mSearchController;
    @Inject @Named("search") BehaviorSubject<List<ScrapedThread>> searchResultSubject;

    private ProgressDialog searchDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View inflate = inflater.inflate(R.layout.fragment_search, container, false);
        ButterKnife.bind(this, inflate);
        ((WhirlpoolApp) getActivity().getApplication()).getDaggerComponent().inject(this);
        setToolbarTitle("");
        mSearchController.attachedView(this);

        RxTextView.textChangeEvents(mQueryText).subscribe(event -> {
            if (event.text().length() > 1)
                searchButton.setEnabled(true);
            else
                searchButton.setEnabled(false);
        });

        searchDialog = new ProgressDialog(getContext(), R.style.MaterialBaseTheme_AlertDialog);
        searchDialog.setIndeterminate(true);
        searchDialog.setTitle("Please Wait");
        searchDialog.setMessage("Searching...");

        return inflate;
    }

    @Override
    public void onDestroyView() {
        mSearchController.attachedView(null);
        super.onDestroyView();
    }

    @OnClick(R.id.btn_search)
    public void Search() {
        ShowSearchProgressBar();
        mSearchController.Search(mQueryText.getText().toString(), 0, -1);
    }

    private void ShowSearchProgressBar() {
        searchDialog.show();
    }


    @Override
    public void DisplaySearchResults(List<ScrapedThread> scrapedThreads) {
        searchResultSubject.onNext(scrapedThreads);
    }

    @Override
    public void HideSearchProgressBar() {
        searchDialog.hide();
    }
}
