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
import com.android.nitecafe.whirlpoolnews.ui.interfaces.IThreadFragment;
import com.android.nitecafe.whirlpoolnews.utilities.WhirlpoolUtils;
import com.android.nitecafe.whirlpoolnews.web.interfaces.IWatchedThreadService;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.marshalchen.ultimaterecyclerview.divideritemdecoration.HorizontalDividerItemDecoration;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.zhanghai.android.materialprogressbar.MaterialProgressBar;
import rx.subjects.PublishSubject;

public class ThreadFragment extends BaseFragment implements IThreadFragment {

    public static final String FORUM_ID = "ForumId";
    public static final String FORUM_NAME = "ForumTitle";
    public PublishSubject<Void> OnFragmentDestroySubject = PublishSubject.create();
    public PublishSubject<Void> OnFragmentCreateViewSubject = PublishSubject.create();
    @Inject ForumThreadController _controller;
    @Inject IWatchedThreadService mIWatchedThreadService;
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
        _controller.Attach(null);
        OnFragmentDestroySubject.onNext(null);
        super.onDestroyView();
    }

    @Override
    public void onResume() {
        super.onResume();
        WhirlpoolApp.getInstance().trackScreenView("Api Thread Fragment");
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
        OnFragmentDestroySubject.onCompleted();
        OnFragmentCreateViewSubject.onCompleted();
        super.onDetach();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View inflate = inflater.inflate(R.layout.fragment_thread, container, false);

        ButterKnife.bind(this, inflate);
        ((WhirlpoolApp) getActivity().getApplication()).getDaggerComponent().inject(this);
        _controller.Attach(this);

        OnFragmentCreateViewSubject.onNext(null);

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

        forumThreadAdapter = new ForumThreadAdapter<>(mIWatchedThreadService);

        forumThreadAdapter.getOnThreadClickedObservable().subscribe(
                thread -> listener.OnThreadClicked(thread.getID(), thread.getTITLE(), WhirlpoolUtils.getNumberOfPage(thread.getREPLIES())));
        forumThreadAdapter.getOnWatchClickedObservable().subscribe(thread
                -> _controller.WatchThread(thread.getID()));
        forumThreadAdapter.getOnUnwatchedObservable().subscribe(recent ->
                _controller.UnwatchThread(recent.getID()));
        forumThreadAdapter.getOnMarkAsClickedObservable().subscribe(recent ->
                _controller.MarkThreadAsRead(recent.getID()));

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
        Snackbar.make(mRecycleView, R.string.message_check_connection, Snackbar.LENGTH_LONG)
                .setAction(R.string.action_message_retry, view -> loadThreads())
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
}
