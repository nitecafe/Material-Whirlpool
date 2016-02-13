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
import com.android.nitecafe.whirlpoolnews.controllers.WatchedController;
import com.android.nitecafe.whirlpoolnews.models.Watched;
import com.android.nitecafe.whirlpoolnews.ui.adapters.WatchedThreadAdapter;
import com.android.nitecafe.whirlpoolnews.ui.interfaces.IOnThreadClicked;
import com.android.nitecafe.whirlpoolnews.ui.interfaces.IWatchedFragment;
import com.android.nitecafe.whirlpoolnews.utilities.IWatchedThreadIdentifier;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.marshalchen.ultimaterecyclerview.divideritemdecoration.HorizontalDividerItemDecoration;
import com.marshalchen.ultimaterecyclerview.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

public class WatchedFragment extends BaseFragment implements IWatchedFragment {

    @Inject WatchedController _controller;
    @Inject IWatchedThreadIdentifier mIWatchedThreadIdentifier;
    @Bind(R.id.watched_recycle_view) UltimateRecyclerView watchedRecycleView;
    @Bind(R.id.watched_progress_loader) MaterialProgressBar mMaterialProgressBar;
    private WatchedThreadAdapter stickyHeaderAdapter;
    private IOnThreadClicked listener;

    @Override
    public void onDestroyView() {
        _controller.Attach(null);
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
        View inflate = inflater.inflate(R.layout.fragment_watched, container, false);

        ButterKnife.bind(this, inflate);
        ((WhirlpoolApp) getActivity().getApplication()).getDaggerComponent().inject(this);
        _controller.Attach(this);

        SetupRecycleView();

        loadWatched();

        return inflate;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setToolbarTitle("Watched Threads");
    }

    private void SetupRecycleView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        watchedRecycleView.setLayoutManager(layoutManager);

        stickyHeaderAdapter = new WatchedThreadAdapter(mIWatchedThreadIdentifier);

        stickyHeaderAdapter.getOnThreadClickedObservable().subscribe(watched1 -> {
            listener.OnWatchedThreadClicked(watched1.getID(), watched1.getTITLE(), watched1.getLASTPAGE(), watched1.getLASTREAD());
        });
        stickyHeaderAdapter.getOnUnwatchedObservable().subscribe(thread
                -> _controller.UnwatchThread(thread.getID()));
        stickyHeaderAdapter.getOnMarkAsClickedObservable().subscribe(
                watched -> _controller.MarkThreadAsRead(watched.getID()));

        watchedRecycleView.setAdapter(stickyHeaderAdapter);
        watchedRecycleView.addItemDecoration(new StickyRecyclerHeadersDecoration(stickyHeaderAdapter));
        watchedRecycleView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getContext()).showLastDivider().build());

        watchedRecycleView.setDefaultOnRefreshListener(this::loadWatched);
    }

    @Override
    public void loadWatched() {
        _controller.GetUnreadWatched();
    }

    @Override
    public void DisplayWatched(List<Watched> watcheds) {
        stickyHeaderAdapter.SetThreads(watcheds);
    }

    @Override
    public void HideCenterProgressBar() {
        mMaterialProgressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void DisplayErrorMessage() {
        Snackbar.make(watchedRecycleView, "Can't load. Please check connection.", Snackbar.LENGTH_LONG)
                .setAction("Retry", view -> loadWatched())
                .show();
    }

    @Override
    public void HideRefreshLoader() {
        watchedRecycleView.setRefreshing(false);
    }
}
