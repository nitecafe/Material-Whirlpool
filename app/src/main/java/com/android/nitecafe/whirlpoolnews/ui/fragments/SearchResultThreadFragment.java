package com.android.nitecafe.whirlpoolnews.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.nitecafe.whirlpoolnews.R;
import com.android.nitecafe.whirlpoolnews.WhirlpoolApp;
import com.android.nitecafe.whirlpoolnews.controllers.SearchResultController;
import com.android.nitecafe.whirlpoolnews.models.ScrapedThread;
import com.android.nitecafe.whirlpoolnews.ui.adapters.PopularScrapedStickyThreadAdapter;
import com.android.nitecafe.whirlpoolnews.ui.adapters.ScrapedThreadAdapter;
import com.android.nitecafe.whirlpoolnews.ui.interfaces.IOnThreadClicked;
import com.android.nitecafe.whirlpoolnews.ui.interfaces.ISearchResultFragment;
import com.android.nitecafe.whirlpoolnews.utilities.StickyHeaderUtil;
import com.android.nitecafe.whirlpoolnews.utilities.interfaces.IPreferencesGetter;
import com.android.nitecafe.whirlpoolnews.web.interfaces.IWatchedThreadService;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.marshalchen.ultimaterecyclerview.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;
import com.marshalchen.ultimaterecyclerview.stickyheadersrecyclerview.StickyRecyclerHeadersTouchListener;
import com.marshalchen.ultimaterecyclerview.ui.divideritemdecoration.HorizontalDividerItemDecoration;

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
    @Inject IPreferencesGetter preferencesGetter;
    @Inject IWatchedThreadService watchedThreadIdentifier;
    @Bind(R.id.popular_thread_recycle_view) UltimateRecyclerView recyclerView;
    @Bind(R.id.popular_thread_progress_loader) MaterialProgressBar progressBar;
    @Bind(R.id.emptyview) TextView emptyTextView;
    private ScrapedThreadAdapter popularThreadAdapter;
    private IOnThreadClicked listener;
    private int forumId;
    private int groupId;
    private String queryString;
    private ForumFragment.IOnForumClicked forumClickListener;

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
    public void onResume() {
        super.onResume();
        WhirlpoolApp.getInstance().trackScreenView("Search Results Thread Fragment");
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

        controller.Attach(this);
        SetupRecycleView();
        StartSearch();
        return inflate;
    }

    private void StartSearch() {
        controller.Search(queryString, forumId, groupId);
    }

    public void DisplaySearchResults(List<ScrapedThread> scrapedThreads) {
        if (scrapedThreads.size() == 0)
            emptyTextView.setVisibility(View.VISIBLE);
        else
            emptyTextView.setVisibility(View.GONE);
        popularThreadAdapter.SetThreads(scrapedThreads);
    }

    @Override
    public void HideSearchProgressBar() {
        progressBar.setVisibility(View.GONE);
    }

    private void SetupRecycleView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        popularThreadAdapter = new PopularScrapedStickyThreadAdapter(watchedThreadIdentifier, new StickyHeaderUtil(), preferencesGetter);
        SubscribeToObservables();

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
    }

    private void openForum(int forumId, CharSequence forumTitle) {
        forumClickListener.onForumClicked(forumId, forumTitle.toString());
    }

    private void SubscribeToObservables() {
        mSubscriptions.add(popularThreadAdapter.OnThreadClickedObservable
                .subscribe(scrapedThread -> listener.OnThreadClicked(scrapedThread.getID(), scrapedThread.getTitle(), scrapedThread.getPageCount(), scrapedThread.getFORUMID())));
        mSubscriptions.add(popularThreadAdapter.OnOpenWebVersionClickObservable
                .subscribe(scrapedThread -> listener.OnOpenWebVersionClicked(scrapedThread.getID(), scrapedThread.getPageCount())));
        mSubscriptions.add(popularThreadAdapter.OnWatchClickedObservable.subscribe(thread
                -> controller.WatchThread(thread.getID())));
        mSubscriptions.add(popularThreadAdapter.OnUnwatchClickedObservable.subscribe(recent ->
                controller.UnwatchThread(recent.getID())));
        mSubscriptions.add(popularThreadAdapter.OnMarkAsReadClickedObservable.subscribe(recent ->
                controller.MarkThreadAsRead(recent.getID())));
        mSubscriptions.add(popularThreadAdapter.OnGoToLastPageClickedObservable.subscribe(scrapedThread -> {
            listener.OnThreadClicked(scrapedThread.getID(), scrapedThread.getTitle(), scrapedThread.getPageCount(), 0, scrapedThread.getPageCount(), scrapedThread.getFORUMID());
        }));
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
