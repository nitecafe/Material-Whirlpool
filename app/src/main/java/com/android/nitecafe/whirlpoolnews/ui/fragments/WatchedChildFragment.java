package com.android.nitecafe.whirlpoolnews.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.nitecafe.whirlpoolnews.R;
import com.android.nitecafe.whirlpoolnews.WhirlpoolApp;
import com.android.nitecafe.whirlpoolnews.controllers.WatchedController;
import com.android.nitecafe.whirlpoolnews.models.Watched;
import com.android.nitecafe.whirlpoolnews.ui.adapters.WatchedThreadAdapter;
import com.android.nitecafe.whirlpoolnews.ui.interfaces.IOnThreadClicked;
import com.android.nitecafe.whirlpoolnews.ui.interfaces.IWatchedFragment;
import com.android.nitecafe.whirlpoolnews.utilities.StickyHeaderUtil;
import com.android.nitecafe.whirlpoolnews.utilities.WhirlpoolUtils;
import com.android.nitecafe.whirlpoolnews.web.interfaces.IWatchedThreadService;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.marshalchen.ultimaterecyclerview.divideritemdecoration.HorizontalDividerItemDecoration;
import com.marshalchen.ultimaterecyclerview.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;
import com.marshalchen.ultimaterecyclerview.stickyheadersrecyclerview.StickyRecyclerHeadersTouchListener;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

/**
 * Inherited by AllWatchedChildFragment
 */
public class WatchedChildFragment extends BaseFragment implements IWatchedFragment {

    @Inject WatchedController _controller;
    @Inject IWatchedThreadService mIWatchedThreadService;
    @Bind(R.id.watched_recycle_view) UltimateRecyclerView watchedRecycleView;
    @Bind(R.id.watched_progress_loader) MaterialProgressBar mMaterialProgressBar;
    private WatchedThreadAdapter stickyHeaderAdapter;
    private IOnThreadClicked listener;
    private StickyRecyclerHeadersDecoration stickyRecyclerHeadersDecoration;
    private ForumFragment.IOnForumClicked forumClickListener;

    @Override
    public void onResume() {
        super.onResume();
        startGoogleAnalytic();
        loadWatched();
    }

    protected void startGoogleAnalytic() {
        WhirlpoolApp.getInstance().trackScreenView("Unread Watched Fragment");
    }

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

        if (context instanceof ForumFragment.IOnForumClicked)
            forumClickListener = (ForumFragment.IOnForumClicked) context;
        else
            throw new ClassCastException("Activity must implement IOnForumClicked");
    }

    @Override
    public void onDetach() {
        listener = null;
        forumClickListener = null;
        super.onDetach();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View inflate = inflater.inflate(R.layout.fragment_child_watched, container, false);

        ButterKnife.bind(this, inflate);
        ((WhirlpoolApp) getActivity().getApplication()).getDaggerComponent().inject(this);
        _controller.Attach(this);

        SetupRecycleView();

        return inflate;
    }

    private void SetupRecycleView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        watchedRecycleView.setLayoutManager(layoutManager);

        stickyHeaderAdapter = new WatchedThreadAdapter(mIWatchedThreadService, new StickyHeaderUtil());

        mSubscriptions.add(stickyHeaderAdapter.OnThreadClickedObservable.subscribe(watched1 -> {
            listener.OnThreadClicked(watched1.getID(), watched1.getTITLE(), watched1.getLASTPAGE(), watched1.getLASTREAD(), WhirlpoolUtils.getNumberOfPage(watched1.getREPLIES()), watched1.getFORUMID());
        }));
        mSubscriptions.add(stickyHeaderAdapter.OnUnwatchClickedObservable.subscribe(thread
                -> _controller.UnwatchThread(thread.getID())));
        mSubscriptions.add(stickyHeaderAdapter.OnMarkAsReadClickedObservable.subscribe(
                watched -> _controller.MarkThreadAsRead(watched.getID())));
        mSubscriptions.add(stickyHeaderAdapter.OnGoToLastPageClickedObservable.subscribe(watched -> {
            int lastPage = WhirlpoolUtils.getNumberOfPage(watched.getREPLIES());
            listener.OnThreadClicked(watched.getID(), watched.getTITLE(), lastPage, 0, lastPage, watched.getFORUMID());
        }));

        watchedRecycleView.setAdapter(stickyHeaderAdapter);
        stickyRecyclerHeadersDecoration = new StickyRecyclerHeadersDecoration(stickyHeaderAdapter);
        watchedRecycleView.addItemDecoration(stickyRecyclerHeadersDecoration);
        watchedRecycleView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getContext()).showLastDivider().build());

        StickyRecyclerHeadersTouchListener stickyRecyclerHeadersTouchListener = new StickyRecyclerHeadersTouchListener(watchedRecycleView.mRecyclerView, stickyRecyclerHeadersDecoration);
        stickyRecyclerHeadersTouchListener.setOnHeaderClickListener((header, position, headerId) -> {
            TextView textView = (TextView) header;
            int forumId = Integer.parseInt(header.getTag().toString());
            openForum(forumId, textView.getText());
        });
        watchedRecycleView.addOnItemTouchListener(stickyRecyclerHeadersTouchListener);
        watchedRecycleView.setDefaultOnRefreshListener(this::loadWatched);
    }

    private void openForum(int forumId, CharSequence forumTitle) {
        forumClickListener.onForumClicked(forumId, forumTitle.toString());
    }

    @Override
    public void loadWatched() {
        _controller.GetUnreadWatched();
    }

    @Override
    public void DisplayWatched(List<Watched> watcheds) {
        stickyHeaderAdapter.SetThreads(watcheds);
        updateHeader();
    }

    @Override
    public void HideCenterProgressBar() {
        mMaterialProgressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void DisplayErrorMessage() {
        Snackbar.make(watchedRecycleView, R.string.message_check_connection, Snackbar.LENGTH_LONG)
                .setAction(R.string.action_message_retry, view -> loadWatched())
                .show();
    }

    private void updateHeader() {
        stickyRecyclerHeadersDecoration.invalidateHeaders();
    }

    @Override
    public void HideRefreshLoader() {
        watchedRecycleView.setRefreshing(false);
    }
}
