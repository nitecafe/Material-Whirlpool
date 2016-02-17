package com.android.nitecafe.whirlpoolnews.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.nitecafe.whirlpoolnews.R;
import com.android.nitecafe.whirlpoolnews.WhirlpoolApp;
import com.android.nitecafe.whirlpoolnews.controllers.PopularThreadsController;
import com.android.nitecafe.whirlpoolnews.models.ScrapedThread;
import com.android.nitecafe.whirlpoolnews.ui.adapters.PopularScrapedStickyThreadAdapter;
import com.android.nitecafe.whirlpoolnews.ui.adapters.ScrapedThreadAdapter;
import com.android.nitecafe.whirlpoolnews.ui.interfaces.IOnThreadClicked;
import com.android.nitecafe.whirlpoolnews.ui.interfaces.IPopularFragment;
import com.android.nitecafe.whirlpoolnews.utilities.IWatchedThreadIdentifier;
import com.android.nitecafe.whirlpoolnews.utilities.StickyHeaderUtil;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.marshalchen.ultimaterecyclerview.divideritemdecoration.HorizontalDividerItemDecoration;
import com.marshalchen.ultimaterecyclerview.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

public class PopularThreadFragment extends BaseFragment implements IPopularFragment {

    @Inject PopularThreadsController popularThreadsController;
    @Inject IWatchedThreadIdentifier watchedThreadIdentifier;
    @Bind(R.id.popular_thread_progress_loader) MaterialProgressBar progressBar;
    @Bind(R.id.popular_thread_recycle_view) UltimateRecyclerView recyclerView;
    private ScrapedThreadAdapter popularThreadAdapter;
    private IOnThreadClicked listener;

    @Override
    public void onDestroyView() {
        popularThreadsController.Attach(null);
        super.onDestroyView();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof IOnThreadClicked)
            listener = (IOnThreadClicked) context;
        else
            throw new ClassCastException("Activity must implement IOnThreadClicked");
    }

    @Override
    public void onDetach() {
        listener = null;
        super.onDetach();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View inflate = inflater.inflate(R.layout.fragment_scraped_popular_thread, container, false);

        ButterKnife.bind(this, inflate);
        ((WhirlpoolApp) getActivity().getApplication()).getDaggerComponent().inject(this);
        popularThreadsController.Attach(this);

        SetupRecycleView();
        loadPopularThreads();

        return inflate;
    }

    private void SetupRecycleView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        popularThreadAdapter = new PopularScrapedStickyThreadAdapter(watchedThreadIdentifier, new StickyHeaderUtil());
        popularThreadAdapter.getOnThreadClickedObservable()
                .subscribe(scrapedThread -> listener.OnThreadClicked(scrapedThread.getID(), scrapedThread.getTitle()));
        popularThreadAdapter.getOnWatchClickedObservable().subscribe(thread
                -> popularThreadsController.WatchThread(thread.getID()));
        popularThreadAdapter.getOnUnwatchedObservable().subscribe(recent ->
                popularThreadsController.UnwatchThread(recent.getID()));
        popularThreadAdapter.getOnMarkAsClickedObservable().subscribe(recent ->
                popularThreadsController.MarkThreadAsRead(recent.getID()));

        recyclerView.setAdapter(popularThreadAdapter);
        recyclerView.addItemDecoration(new StickyRecyclerHeadersDecoration(popularThreadAdapter));
        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getContext()).showLastDivider().build());

        recyclerView.setDefaultOnRefreshListener(this::loadPopularThreads);
    }

    private void loadPopularThreads() {
        popularThreadsController.GetPopularThreads();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setToolbarTitle("Popular Threads");
    }

    @Override public void DisplayPopularThreads(ArrayList<ScrapedThread> threadList) {
        popularThreadAdapter.SetThreads(threadList);
    }

    @Override public void DisplayErrorMessage() {
        Snackbar.make(getView(), "Something went wrong. Try again", Snackbar.LENGTH_LONG)
                .show();
    }

    @Override public void HideCenterProgressBar() {
        progressBar.setVisibility(View.GONE);
    }
}
