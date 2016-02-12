package com.android.nitecafe.whirlpoolnews.ui.fragments;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.android.nitecafe.whirlpoolnews.R;
import com.android.nitecafe.whirlpoolnews.WhirlpoolApp;
import com.android.nitecafe.whirlpoolnews.constants.StringConstants;
import com.android.nitecafe.whirlpoolnews.controllers.ScrapedPostController;
import com.android.nitecafe.whirlpoolnews.models.ScrapedPost;
import com.android.nitecafe.whirlpoolnews.ui.adapters.ScrapedPostAdapter;
import com.android.nitecafe.whirlpoolnews.ui.interfaces.IScrapedPostFragment;
import com.android.nitecafe.whirlpoolnews.utilities.ThreadScraper;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.marshalchen.ultimaterecyclerview.divideritemdecoration.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

public class ScrapedPostFragment extends BaseFragment implements IScrapedPostFragment {

    public static final String THREAD_ID = "ThreadId";
    public static final String THREAD_TITLE = "ThreadTitle";
    public static final String THREAD_PAGE = "ThreadPage";
    public static final String POST_LAST_READ = "PostLastRead";
    @Inject ScrapedPostController _controller;
    @Bind(R.id.post_recycle_view) UltimateRecyclerView mRecycleView;
    @Bind(R.id.post_progress_loader) MaterialProgressBar mMaterialProgressBar;
    @Bind(R.id.spinner_post_page) Spinner pageNumberSpinner;
    @Bind(R.id.btn_next) ImageButton buttonNext;
    @Bind(R.id.btn_back) ImageButton buttonPrevious;
    private int mThreadId;
    private ScrapedPostAdapter scrapedPostAdapter;
    private String mThreadTitle;
    private int mPageToLoad;
    private int mPostLastReadId;

    public static ScrapedPostFragment newInstance(int threadId, String threadTitle, int page, int postLastRead) {
        ScrapedPostFragment fragment = new ScrapedPostFragment();
        Bundle args = new Bundle();
        args.putInt(THREAD_ID, threadId);
        args.putInt(THREAD_PAGE, page);
        args.putInt(POST_LAST_READ, postLastRead);
        args.putString(THREAD_TITLE, threadTitle);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mThreadId = getArguments().getInt(THREAD_ID, 0);
        mPageToLoad = getArguments().getInt(THREAD_PAGE, 0);
        mPostLastReadId = getArguments().getInt(POST_LAST_READ, 0);
        mThreadTitle = getArguments().getString(THREAD_TITLE, "");
    }

    @Override
    public void onDestroyView() {
        _controller.attach(null);
        super.onDestroyView();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View inflate = inflater.inflate(R.layout.fragment_scraped_post, container, false);

        ButterKnife.bind(this, inflate);
        ((WhirlpoolApp) getActivity().getApplication()).getDaggerComponent().inject(this);
        _controller.attach(this);

        SetupRecycleView();

        loadPosts();

        return inflate;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setToolbarTitle(mThreadTitle);
    }

    private void SetupRecycleView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecycleView.setLayoutManager(layoutManager);

        scrapedPostAdapter = new ScrapedPostAdapter();

        mRecycleView.setAdapter(scrapedPostAdapter);
        mRecycleView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getContext()).showLastDivider().build());

        mRecycleView.setDefaultOnRefreshListener(this::loadPosts);
    }

    private void loadPosts() {
        _controller.GetScrapedPosts(mThreadId, mPageToLoad);
    }

    @Override
    public void HideCenterProgressBar() {
        mMaterialProgressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void DisplayErrorMessage() {
        Snackbar.make(mRecycleView, "Can't load. Please check connection.", Snackbar.LENGTH_LONG)
                .setAction("Retry", view -> loadPosts())
                .show();

        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(ThreadScraper.THREAD_URL + String.valueOf(mThreadId)));
        startActivity(browserIntent);
    }

    @Override
    public void DisplayPosts(List<ScrapedPost> posts) {
        scrapedPostAdapter.SetPosts(posts);
        ScrollToFirstUnreadItem();
    }

    private void ScrollToFirstUnreadItem() {
        if (mPostLastReadId > 0) {
            int position = mPostLastReadId - ((mPageToLoad - 1) * StringConstants.POST_PER_PAGE);
            mRecycleView.scrollVerticallyToPosition(position - 1);
            mPostLastReadId = 0;
        } else
            mRecycleView.scrollVerticallyToPosition(0);
    }

    @Override
    public void HideRefreshLoader() {
        mRecycleView.setRefreshing(false);
    }

    @Override
    public void SetupPageSpinner(int pageCount, int page) {
        pageNumberSpinner.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        List<String> numberPages = new ArrayList<>();
        for (int i = 1; i <= pageCount; i++) {
            numberPages.add("Page " + i + " / " + pageCount);
        }
        ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<>(getContext(), R.layout.spinner_row, numberPages);
        pageNumberSpinner.setAdapter(stringArrayAdapter);
        pageNumberSpinner.setSelection(page - 1);

        updateNavigationButtonVisibility();
    }

    @Override
    public void ShowRefreshLoader() {
        mRecycleView.setRefreshing(true);
    }

    private void updateNavigationButtonVisibility() {
        if (_controller.IsAtFirstPage())
            buttonPrevious.setVisibility(View.INVISIBLE);
        else
            buttonPrevious.setVisibility(View.VISIBLE);

        if (_controller.IsAtLastPage())
            buttonNext.setVisibility(View.INVISIBLE);
        else
            buttonNext.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.btn_back)
    public void GoToPreviousPage() {
        _controller.loadPreviousPage(mThreadId);
    }

    @OnClick(R.id.btn_next)
    public void GoToNextPage() {
        _controller.loadNextPage(mThreadId);
    }

}
