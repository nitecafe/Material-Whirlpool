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
import com.android.nitecafe.whirlpoolnews.ui.adapters.ThreadStickyHeaderAdapter;
import com.android.nitecafe.whirlpoolnews.ui.adapters.WatchedThreadAdapter;
import com.android.nitecafe.whirlpoolnews.ui.interfaces.IOnThreadClicked;
import com.android.nitecafe.whirlpoolnews.ui.interfaces.IRecycleViewItemClick;
import com.android.nitecafe.whirlpoolnews.ui.interfaces.IWatchedFragment;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.marshalchen.ultimaterecyclerview.divideritemdecoration.HorizontalDividerItemDecoration;
import com.marshalchen.ultimaterecyclerview.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

public class WatchedFragment extends BaseFragment implements IRecycleViewItemClick, IWatchedFragment {

    @Inject WatchedController _controller;
    @Bind(R.id.watched_recycle_view) UltimateRecyclerView watchedRecycleView;
    @Bind(R.id.watched_progress_loader) MaterialProgressBar mMaterialProgressBar;
    private ThreadStickyHeaderAdapter<Watched> stickyHeaderAdapter;
    private IOnThreadClicked listener;

    @Override
    public void onDestroyView() {
        _controller.attach(null);
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
        _controller.attach(this);

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

        stickyHeaderAdapter = new WatchedThreadAdapter(this);

        watchedRecycleView.setAdapter(stickyHeaderAdapter);
        watchedRecycleView.addItemDecoration(new StickyRecyclerHeadersDecoration(stickyHeaderAdapter));
        watchedRecycleView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getContext()).build());

        watchedRecycleView.setDefaultOnRefreshListener(this::loadWatched);
    }

    private void loadWatched() {
        _controller.GetWatched();
    }

    @Override
    public void DisplayWatched(List<Watched> watcheds) {
        stickyHeaderAdapter.setThreads(watcheds);
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

    @Override
    public void OnItemClicked(int itemClicked, String title) {
        listener.OnThreadClicked(itemClicked, title);
    }
}
