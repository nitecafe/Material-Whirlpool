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
import com.android.nitecafe.whirlpoolnews.controllers.PopularThreadsController;
import com.android.nitecafe.whirlpoolnews.models.ScrapedThread;
import com.android.nitecafe.whirlpoolnews.ui.adapters.PopularScrapedStickyThreadAdapter;
import com.android.nitecafe.whirlpoolnews.ui.adapters.ScrapedThreadAdapter;
import com.android.nitecafe.whirlpoolnews.ui.interfaces.IOnThreadClicked;
import com.android.nitecafe.whirlpoolnews.ui.interfaces.IPopularFragment;
import com.android.nitecafe.whirlpoolnews.utilities.StickyHeaderUtil;
import com.android.nitecafe.whirlpoolnews.utilities.interfaces.IPreferencesGetter;
import com.android.nitecafe.whirlpoolnews.web.interfaces.IWatchedThreadService;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.marshalchen.ultimaterecyclerview.divideritemdecoration.HorizontalDividerItemDecoration;
import com.marshalchen.ultimaterecyclerview.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;
import com.marshalchen.ultimaterecyclerview.stickyheadersrecyclerview.StickyRecyclerHeadersTouchListener;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

public class PopularThreadFragment extends BaseFragment implements IPopularFragment {

    @Inject PopularThreadsController popularThreadsController;
    @Inject IWatchedThreadService watchedThreadIdentifier;
    @Inject IPreferencesGetter preferencesGetter;
    @Bind(R.id.popular_thread_progress_loader) MaterialProgressBar progressBar;
    @Bind(R.id.popular_thread_recycle_view) UltimateRecyclerView recyclerView;
    private ScrapedThreadAdapter popularThreadAdapter;
    private IOnThreadClicked listener;
    private ForumFragment.IOnForumClicked forumClickListener;

    @Override
    public void onDestroyView() {
        popularThreadsController.Attach(null);
        super.onDestroyView();
    }

    @Override
    public void onResume() {
        super.onResume();
        WhirlpoolApp.getInstance().trackScreenView("Popular Threads Fragment");
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
        View inflate = inflater.inflate(R.layout.fragment_scraped_popular_thread, container, false);

        ButterKnife.bind(this, inflate);
        ((WhirlpoolApp) getActivity().getApplication()).getDaggerComponent().inject(this);
        popularThreadsController.Attach(this);

        SetupRecycleView();
        loadPopularThreads();

        return inflate;
    }

    private void SetupRecycleView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        popularThreadAdapter = new PopularScrapedStickyThreadAdapter(watchedThreadIdentifier, new StickyHeaderUtil(), preferencesGetter);
        mSubscriptions.add(popularThreadAdapter.OnThreadClickedObservable
                .subscribe(scrapedThread -> listener.OnThreadClicked(scrapedThread.getID(), scrapedThread.getTitle(), scrapedThread.getPageCount(), scrapedThread.getFORUMID())));
        mSubscriptions.add(popularThreadAdapter.OnWatchClickedObservable.subscribe(thread
                -> popularThreadsController.WatchThread(thread.getID())));
        mSubscriptions.add(popularThreadAdapter.OnUnwatchClickedObservable.subscribe(recent ->
                popularThreadsController.UnwatchThread(recent.getID())));
        mSubscriptions.add(popularThreadAdapter.OnMarkAsReadClickedObservable.subscribe(recent ->
                popularThreadsController.MarkThreadAsRead(recent.getID())));
        mSubscriptions.add(popularThreadAdapter.OnGoToLastPageClickedObservable.subscribe(scrapedThread -> {
            listener.OnThreadClicked(scrapedThread.getID(), scrapedThread.getTitle(), scrapedThread.getPageCount(), 0, scrapedThread.getPageCount(), scrapedThread.getFORUMID());
        }));

        recyclerView.setAdapter(popularThreadAdapter);
        StickyRecyclerHeadersDecoration stickyRecyclerHeadersDecoration = new StickyRecyclerHeadersDecoration(popularThreadAdapter);
        recyclerView.addItemDecoration(stickyRecyclerHeadersDecoration);
        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getContext()).showLastDivider().build());

        StickyRecyclerHeadersTouchListener stickyRecyclerHeadersTouchListener = new StickyRecyclerHeadersTouchListener(recyclerView.mRecyclerView, stickyRecyclerHeadersDecoration);
        stickyRecyclerHeadersTouchListener.setOnHeaderClickListener((header, position, headerId) -> {
            TextView textView = (TextView) header;
            int forumId = Integer.parseInt(header.getTag().toString());
            openForum(forumId, textView.getText());
        });
        recyclerView.addOnItemTouchListener(stickyRecyclerHeadersTouchListener);

        recyclerView.setDefaultOnRefreshListener(this::loadPopularThreads);
    }

    private void openForum(int forumId, CharSequence forumTitle) {
        forumClickListener.onForumClicked(forumId, forumTitle.toString());
    }

    private void loadPopularThreads() {
        popularThreadsController.GetPopularThreads();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setToolbarTitle(getActivity().getString(R.string.title_popular_threads));
    }

    @Override
    public void DisplayPopularThreads(ArrayList<ScrapedThread> threadList) {
        popularThreadAdapter.SetThreads(threadList);
    }

    @Override
    public void DisplayErrorMessage() {
        Snackbar.make(getView(), R.string.message_generic_error, Snackbar.LENGTH_LONG)
                .show();
    }

    @Override
    public void HideCenterProgressBar() {
        progressBar.setVisibility(View.GONE);
    }
}
