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
import com.android.nitecafe.whirlpoolnews.controllers.ScrapedThreadController;
import com.android.nitecafe.whirlpoolnews.models.ScrapedThread;
import com.android.nitecafe.whirlpoolnews.ui.adapters.ScrapedThreadAdapter;
import com.android.nitecafe.whirlpoolnews.ui.interfaces.IOnThreadClicked;
import com.android.nitecafe.whirlpoolnews.ui.interfaces.IScrapedThreadFragment;
import com.android.nitecafe.whirlpoolnews.utilities.IWatchedThreadIdentifier;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.marshalchen.ultimaterecyclerview.divideritemdecoration.HorizontalDividerItemDecoration;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

public class ScrapedThreadFragment extends BaseFragment implements IScrapedThreadFragment {

    public static final String FORUM_ID = "ForumId";
    public static final String FORUM_NAME = "ForumTitle";
    public static final String FORUM_GROUP_ID = "ForumGroupId";
    @Inject ScrapedThreadController _controller;
    @Inject IWatchedThreadIdentifier mIWatchedThreadIdentifier;
    @Bind(R.id.thread_recycle_view) UltimateRecyclerView mRecycleView;
    @Bind(R.id.thread_progress_loader) MaterialProgressBar mMaterialProgressBar;
    private IOnThreadClicked listener;
    private int mForumId;
    private String mForumTitle;
    private ScrapedThreadAdapter forumThreadAdapter;
    private int mGroupId;

    public static ScrapedThreadFragment newInstance(int forumId, String forumName, int groupId) {
        ScrapedThreadFragment fragment = new ScrapedThreadFragment();
        Bundle args = new Bundle();
        args.putInt(FORUM_ID, forumId);
        args.putString(FORUM_NAME, forumName);
        args.putInt(FORUM_GROUP_ID, groupId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mForumId = getArguments().getInt(FORUM_ID, 0);
        mForumTitle = getArguments().getString(FORUM_NAME, "");
        mGroupId = getArguments().getInt(FORUM_GROUP_ID, 0);
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
        View inflate = inflater.inflate(R.layout.fragment_scraped_thread, container, false);

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

        forumThreadAdapter = new ScrapedThreadAdapter(mIWatchedThreadIdentifier);
        forumThreadAdapter.getOnThreadClickedObservable()
                .subscribe(scrapedThread -> listener.OnThreadClicked(scrapedThread.getID(), scrapedThread.getTitle()));

        mRecycleView.setAdapter(forumThreadAdapter);
        mRecycleView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getContext()).showLastDivider().build());

        mRecycleView.setDefaultOnRefreshListener(this::loadThreads);
    }

    private void loadThreads() {
        _controller.GetScrapedThreads(mForumId, mGroupId);
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
    public void DisplayThreads(List<ScrapedThread> threads) {
        forumThreadAdapter.SetThreads(threads);
    }

    @Override
    public void HideRefreshLoader() {
        mRecycleView.setRefreshing(false);
    }
}
