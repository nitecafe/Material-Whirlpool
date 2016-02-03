package com.android.nitecafe.whirlpoolnews.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.nitecafe.whirlpoolnews.R;
import com.android.nitecafe.whirlpoolnews.WhirlpoolApp;
import com.android.nitecafe.whirlpoolnews.controllers.ForumController;
import com.android.nitecafe.whirlpoolnews.models.Forum;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.marshalchen.ultimaterecyclerview.UltimateViewAdapter;
import com.marshalchen.ultimaterecyclerview.divideritemdecoration.HorizontalDividerItemDecoration;
import com.marshalchen.ultimaterecyclerview.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

public class ForumFragment extends BaseFragment implements IForumFragment {

    @Bind(R.id.forum_recycle_view) UltimateRecyclerView forumRecycleView;
    @Bind(R.id.forum_progress_loader) MaterialProgressBar mMaterialProgressBar;
    @Inject ForumController _controller;
    private StickyHeaderAdapter stickyHeaderAdapter;

    @Override
    public void onDestroyView() {
        _controller.attach(null);
        super.onDestroyView();
    }

    @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View inflate = inflater.inflate(R.layout.fragment_forum, container, false);

        ButterKnife.bind(this, inflate);
        ((WhirlpoolApp) getActivity().getApplication()).getDaggerComponent().inject(this);
        _controller.attach(this);

        SetupRecycleView();

        loadForum();

        return inflate;
    }

    @Override public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setToolbarTitle("Discussion Forum");
    }

    private void SetupRecycleView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        forumRecycleView.setLayoutManager(layoutManager);

        stickyHeaderAdapter = new StickyHeaderAdapter();

        forumRecycleView.setAdapter(stickyHeaderAdapter);
        forumRecycleView.addItemDecoration(new StickyRecyclerHeadersDecoration(stickyHeaderAdapter));
        forumRecycleView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getContext()).build());
    }

    private void loadForum() {
        _controller.getForum();
    }

    @Override
    public void DisplayForum(List<Forum> forums) {
        stickyHeaderAdapter.setForum(forums);
    }

    @Override
    public void HideCenterProgressBar() {
        mMaterialProgressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void DisplayErrorMessage() {
        Snackbar.make(forumRecycleView, "Can't load. Please check connection.", Snackbar.LENGTH_LONG)
                .setAction("Retry", view -> loadForum())
                .show();
    }

    @Subscribe
    public void OnItemClicked(String newsId) {
    }
}


