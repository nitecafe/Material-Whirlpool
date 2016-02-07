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
import com.android.nitecafe.whirlpoolnews.ui.interfaces.IRecentFragment;
import com.android.nitecafe.whirlpoolnews.ui.interfaces.IRecycleViewItemClick;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.marshalchen.ultimaterecyclerview.divideritemdecoration.HorizontalDividerItemDecoration;
import com.marshalchen.ultimaterecyclerview.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

public class RecentFragment extends BaseFragment implements IRecycleViewItemClick, IRecentFragment {

    @Inject RecentController _controller;
    @Bind(R.id.recent_recycle_view) UltimateRecyclerView recentRecycleView;
    @Bind(R.id.recent_progress_loader) MaterialProgressBar mMaterialProgressBar;
    private ThreadStickyHeaderAdapter<Recent> stickyHeaderAdapter;
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
        View inflate = inflater.inflate(R.layout.fragment_recent, container, false);

        ButterKnife.bind(this, inflate);
        ((WhirlpoolApp) getActivity().getApplication()).getDaggerComponent().inject(this);
        _controller.attach(this);

        SetupRecycleView();

        loadRecent();

        return inflate;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setToolbarTitle("Discussion Forum");
    }

    private void SetupRecycleView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recentRecycleView.setLayoutManager(layoutManager);

        stickyHeaderAdapter = new ThreadStickyHeaderAdapter<>(this);

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
        stickyHeaderAdapter.setThreads(recents);
    }

    @Override
    public void HideCenterProgressBar() {
        mMaterialProgressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void DisplayErrorMessage() {
        Snackbar.make(recentRecycleView, "Can't load. Please check connection.", Snackbar.LENGTH_LONG)
                .setAction("Retry", view -> loadRecent())
                .show();
    }

    @Override
    public void HideRefreshLoader() {
        recentRecycleView.setRefreshing(false);
    }

    @Override
    public void OnItemClicked(int itemClicked, String threadTitle) {
        listener.OnThreadClicked(itemClicked, threadTitle);
    }

    public interface IOnThreadClicked {
        void OnThreadClicked(int threadId, String threadTitle);
    }
}
