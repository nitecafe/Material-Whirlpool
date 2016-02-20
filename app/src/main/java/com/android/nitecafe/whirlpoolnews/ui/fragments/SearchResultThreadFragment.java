package com.android.nitecafe.whirlpoolnews.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.nitecafe.whirlpoolnews.R;
import com.android.nitecafe.whirlpoolnews.WhirlpoolApp;
import com.android.nitecafe.whirlpoolnews.controllers.SearchResultController;
import com.android.nitecafe.whirlpoolnews.models.ScrapedThread;
import com.android.nitecafe.whirlpoolnews.ui.adapters.PopularScrapedStickyThreadAdapter;
import com.android.nitecafe.whirlpoolnews.ui.adapters.ScrapedThreadAdapter;
import com.android.nitecafe.whirlpoolnews.ui.interfaces.IOnThreadClicked;
import com.android.nitecafe.whirlpoolnews.ui.interfaces.ISearchResultFragment;
import com.android.nitecafe.whirlpoolnews.utilities.StickyHeaderUtil;
import com.android.nitecafe.whirlpoolnews.web.interfaces.IWatchedThreadService;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.marshalchen.ultimaterecyclerview.divideritemdecoration.HorizontalDividerItemDecoration;
import com.marshalchen.ultimaterecyclerview.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

public class SearchResultThreadFragment extends BaseFragment implements ISearchResultFragment {

    private static final String QUERY_STRING = "QueryString";
    private static final String FORUM_ID = "ForumId";
    private static final String GROUP_ID = "GroupId";
    @Inject SearchResultController controller;
    @Inject IWatchedThreadService watchedThreadIdentifier;
    @Bind(R.id.popular_thread_recycle_view) UltimateRecyclerView recyclerView;
    @Bind(R.id.popular_thread_progress_loader) MaterialProgressBar progressBar;
    private ScrapedThreadAdapter popularThreadAdapter;
    private IOnThreadClicked listener;
    private int forumId;
    private int groupId;
    private String queryString;

    public static SearchResultThreadFragment newInstance(String query, int forumId, int groupId) {
        SearchResultThreadFragment fragment = new SearchResultThreadFragment();
        Bundle args = new Bundle();
        args.putString(QUERY_STRING, query);
        args.putInt(FORUM_ID, forumId);
        args.putInt(GROUP_ID, groupId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onDestroyView() {
        controller.Attach(null);
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
        View inflate = inflater.inflate(R.layout.fragment_scraped_popular_thread, container, false);

        ButterKnife.bind(this, inflate);
        ((WhirlpoolApp) getActivity().getApplication()).getDaggerComponent().inject(this);

        controller.Attach(this);
        SetupRecycleView();
        StartSearch();
        return inflate;
    }

    private void StartSearch() {
        controller.Search(queryString, forumId, groupId);
    }

    public void DisplaySearchResults(List<ScrapedThread> scrapedThreads) {
        popularThreadAdapter.SetThreads(scrapedThreads);
    }

    @Override
    public void HideSearchProgressBar() {
        progressBar.setVisibility(View.GONE);
    }

    private void SetupRecycleView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        popularThreadAdapter = new PopularScrapedStickyThreadAdapter(watchedThreadIdentifier, new StickyHeaderUtil());
        SubscribeToObservables();

        recyclerView.setAdapter(popularThreadAdapter);
        recyclerView.addItemDecoration(new StickyRecyclerHeadersDecoration(popularThreadAdapter));
        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getContext()).showLastDivider().build());
    }

    private void SubscribeToObservables() {
        popularThreadAdapter.getOnThreadClickedObservable()
                .subscribe(scrapedThread -> listener.OnThreadClicked(scrapedThread.getID(), scrapedThread.getTitle(), scrapedThread.getPageCount()));
        popularThreadAdapter.getOnWatchClickedObservable().subscribe(thread
                -> controller.WatchThread(thread.getID()));
        popularThreadAdapter.getOnUnwatchedObservable().subscribe(recent ->
                controller.UnwatchThread(recent.getID()));
        popularThreadAdapter.getOnMarkAsClickedObservable().subscribe(recent ->
                controller.MarkThreadAsRead(recent.getID()));
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setToolbarTitle("Search: " + queryString);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        forumId = getArguments().getInt(FORUM_ID, 0);
        groupId = getArguments().getInt(GROUP_ID, 0);
        queryString = getArguments().getString(QUERY_STRING, "");
    }
}
