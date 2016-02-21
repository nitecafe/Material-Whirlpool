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
import com.android.nitecafe.whirlpoolnews.controllers.RecentController;
import com.android.nitecafe.whirlpoolnews.models.Recent;
import com.android.nitecafe.whirlpoolnews.ui.adapters.ThreadStickyHeaderAdapter;
import com.android.nitecafe.whirlpoolnews.ui.interfaces.IOnThreadClicked;
import com.android.nitecafe.whirlpoolnews.ui.interfaces.IRecentFragment;
import com.android.nitecafe.whirlpoolnews.utilities.StickyHeaderUtil;
import com.android.nitecafe.whirlpoolnews.utilities.WhirlpoolUtils;
import com.android.nitecafe.whirlpoolnews.web.interfaces.IWatchedThreadService;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.marshalchen.ultimaterecyclerview.divideritemdecoration.HorizontalDividerItemDecoration;
import com.marshalchen.ultimaterecyclerview.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

public class RecentFragment extends BaseFragment implements IRecentFragment {

    @Inject RecentController _controller;
    @Inject IWatchedThreadService mIWatchedThreadService;
    @Bind(R.id.recent_recycle_view) UltimateRecyclerView recentRecycleView;
    @Bind(R.id.recent_progress_loader) MaterialProgressBar mMaterialProgressBar;
    private ThreadStickyHeaderAdapter<Recent> stickyHeaderAdapter;
    private IOnThreadClicked listener;

    @Override
    public void onDestroyView() {
        _controller.Attach(null);
        super.onDestroyView();
    }

    @Override
    public void onResume() {
        super.onResume();
        WhirlpoolApp.getInstance().trackScreenView("Recent Threads Fragment");
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
        View inflate = inflater.inflate(R.layout.fragment_recent, container, false);

        ButterKnife.bind(this, inflate);
        ((WhirlpoolApp) getActivity().getApplication()).getDaggerComponent().inject(this);
        _controller.Attach(this);

        SetupRecycleView();

        loadRecent();

        return inflate;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setToolbarTitle(getActivity().getString(R.string.title_recent_posts));
    }

    private void SetupRecycleView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recentRecycleView.setLayoutManager(layoutManager);

        stickyHeaderAdapter = new ThreadStickyHeaderAdapter<>(mIWatchedThreadService, new StickyHeaderUtil());

        stickyHeaderAdapter.getOnThreadClickedObservable().subscribe(
                recent -> listener.OnThreadClicked(recent.getID(), recent.getTITLE(), WhirlpoolUtils.getNumberOfPage(recent.getREPLIES())));
        stickyHeaderAdapter.getOnWatchClickedObservable().subscribe(thread
                -> _controller.WatchThread(thread.getID()));
        stickyHeaderAdapter.getOnUnwatchedObservable().subscribe(recent ->
                _controller.UnwatchThread(recent.getID()));
        stickyHeaderAdapter.getOnMarkAsClickedObservable().subscribe(recent ->
                _controller.MarkThreadAsRead(recent.getID()));

        recentRecycleView.setAdapter(stickyHeaderAdapter);
        recentRecycleView.addItemDecoration(new StickyRecyclerHeadersDecoration(stickyHeaderAdapter));
        recentRecycleView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getContext()).build());

        recentRecycleView.setDefaultOnRefreshListener(this::loadRecent);
    }

    private void loadRecent() {
        _controller.GetRecent();
    }

    @Override
    public void DisplayRecent(List<Recent> recents) {
        stickyHeaderAdapter.SetThreads(recents);
    }

    @Override
    public void HideCenterProgressBar() {
        mMaterialProgressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void DisplayErrorMessage() {
        Snackbar.make(recentRecycleView, R.string.message_check_connection, Snackbar.LENGTH_LONG)
                .setAction("Retry", view -> loadRecent())
                .show();
    }

    @Override
    public void HideRefreshLoader() {
        recentRecycleView.setRefreshing(false);
    }
}

