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
import com.android.nitecafe.whirlpoolnews.controllers.ForumThreadController;
import com.android.nitecafe.whirlpoolnews.models.ForumThread;
import com.android.nitecafe.whirlpoolnews.ui.adapters.ForumThreadAdapter;
import com.android.nitecafe.whirlpoolnews.ui.interfaces.IOnThreadClicked;
import com.android.nitecafe.whirlpoolnews.ui.interfaces.IRecycleViewItemClick;
import com.android.nitecafe.whirlpoolnews.ui.interfaces.IThreadFragment;
import com.android.nitecafe.whirlpoolnews.utilities.IWatchedThreadIdentifier;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.marshalchen.ultimaterecyclerview.divideritemdecoration.HorizontalDividerItemDecoration;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

public class ThreadFragment extends BaseFragment implements IRecycleViewItemClick, IThreadFragment {

    public static final String FORUM_ID = "ForumId";
    public static final String FORUM_NAME = "ForumTitle";
    @Inject ForumThreadController _controller;
    @Inject IWatchedThreadIdentifier mIWatchedThreadIdentifier;
    @Bind(R.id.thread_recycle_view) UltimateRecyclerView mRecycleView;
    @Bind(R.id.thread_progress_loader) MaterialProgressBar mMaterialProgressBar;
    private IOnThreadClicked listener;
    private int mForumId;
    private String mForumTitle;
    private ForumThreadAdapter<ForumThread> forumThreadAdapter;

    public static ThreadFragment newInstance(int forumId, String forumName) {
        ThreadFragment fragment = new ThreadFragment();
        Bundle args = new Bundle();
        args.putInt(FORUM_ID, forumId);
        args.putString(FORUM_NAME, forumName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mForumId = getArguments().getInt(FORUM_ID, 0);
        mForumTitle = getArguments().getString(FORUM_NAME, "");
    }

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
        View inflate = inflater.inflate(R.layout.fragment_thread, container, false);

        ButterKnife.bind(this, inflate);
        ((WhirlpoolApp) getActivity().getApplication()).getDaggerComponent().inject(this);
        _controller.attach(this);

        SetupRecycleView();

        loadThreads();

        return inflate;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setToolbarTitle(mForumTitle);
    }

    private void SetupRecycleView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecycleView.setLayoutManager(layoutManager);

        forumThreadAdapter = new ForumThreadAdapter(mIWatchedThreadIdentifier);
        forumThreadAdapter.getOnThreadClickedObservable().subscribe(
                thread -> listener.OnThreadClicked(thread.getID(), thread.getTITLE()));
        mRecycleView.setAdapter(forumThreadAdapter);
        mRecycleView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getContext()).showLastDivider().build());

        mRecycleView.setDefaultOnRefreshListener(this::loadThreads);
    }

    private void loadThreads() {
        _controller.GetThreads(mForumId);
    }

    @Override
    public void HideCenterProgressBar() {
        mMaterialProgressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void DisplayErrorMessage() {
        Snackbar.make(mRecycleView, "Can't load. Please check connection.", Snackbar.LENGTH_LONG)
                .setAction("Retry", view -> loadThreads())
                .show();
    }

    @Override
    public void DisplayThreads(List<ForumThread> threads) {
        forumThreadAdapter.SetThreads(threads);
    }

    @Override
    public void HideRefreshLoader() {
        mRecycleView.setRefreshing(false);
    }

    @Override
    public void OnItemClicked(int itemClicked, String threadTitle) {
        listener.OnThreadClicked(itemClicked, threadTitle);
    }
}
