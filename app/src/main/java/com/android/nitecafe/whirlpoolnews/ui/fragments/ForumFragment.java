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
import com.android.nitecafe.whirlpoolnews.controllers.ForumController;
import com.android.nitecafe.whirlpoolnews.models.Forum;
import com.android.nitecafe.whirlpoolnews.ui.adapters.ForumStickyHeaderAdapter;
import com.android.nitecafe.whirlpoolnews.ui.interfaces.IForumFragment;
import com.android.nitecafe.whirlpoolnews.ui.interfaces.IRecycleViewItemClick;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.marshalchen.ultimaterecyclerview.divideritemdecoration.HorizontalDividerItemDecoration;
import com.marshalchen.ultimaterecyclerview.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

public class ForumFragment extends BaseFragment implements IForumFragment, IRecycleViewItemClick {

    @Bind(R.id.forum_recycle_view) UltimateRecyclerView forumRecycleView;
    @Bind(R.id.forum_progress_loader) MaterialProgressBar mMaterialProgressBar;
    @Inject ForumController _controller;
    private ForumStickyHeaderAdapter stickyHeaderAdapter;
    private IOnForumClicked listener;

    @Override
    public void onDestroyView() {
        _controller.attach(null);
        super.onDestroyView();
    }

    @Override
    public void onResume() {
        super.onResume();
        WhirlpoolApp.getInstance().trackScreenView("Forum Fragment");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof IOnForumClicked)
            listener = (IOnForumClicked) context;
        else
            throw new ClassCastException("activity must implement IOnForumClicked.");
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
        View inflate = inflater.inflate(R.layout.fragment_forum, container, false);

        ButterKnife.bind(this, inflate);
        ((WhirlpoolApp) getActivity().getApplication()).getDaggerComponent().inject(this);
        _controller.attach(this);

        SetupRecycleView();

        loadForum();

        return inflate;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setToolbarTitle(getActivity().getString(R.string.title_discussion_forum));
    }

    private void SetupRecycleView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        forumRecycleView.setLayoutManager(layoutManager);

        stickyHeaderAdapter = new ForumStickyHeaderAdapter(this);

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
        Snackbar.make(forumRecycleView, R.string.message_check_connection, Snackbar.LENGTH_LONG)
                .setAction(R.string.action_message_retry, view -> loadForum())
                .show();
    }

    @Override
    public void HideRefreshLoader() {
        forumRecycleView.setRefreshing(false);
    }

    @Override
    public void OnItemClicked(int itemClicked, String title) {
        listener.onForumClicked(itemClicked, title);
    }

    public interface IOnForumClicked {
        void onForumClicked(int forumId, String title);
    }
}


