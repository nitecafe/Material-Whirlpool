package com.android.nitecafe.whirlpoolnews.ui.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Html;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.nitecafe.whirlpoolnews.R;
import com.android.nitecafe.whirlpoolnews.WhirlpoolApp;
import com.android.nitecafe.whirlpoolnews.constants.StringConstants;
import com.android.nitecafe.whirlpoolnews.controllers.ScrapedPostChildController;
import com.android.nitecafe.whirlpoolnews.models.ScrapedPost;
import com.android.nitecafe.whirlpoolnews.ui.adapters.ScrapedPostAdapter;
import com.android.nitecafe.whirlpoolnews.ui.interfaces.IScrapedPostChildFragment;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.marshalchen.ultimaterecyclerview.divideritemdecoration.HorizontalDividerItemDecoration;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import icepick.Icepick;
import icepick.State;
import me.zhanghai.android.materialprogressbar.MaterialProgressBar;
import rx.subjects.PublishSubject;

public class ScrapedPostChildFragment extends BaseFragment implements IScrapedPostChildFragment {

    public static final String THREAD_ID = "ThreadId";
    public static final String THREAD_TITLE = "ThreadTitle";
    public static final String THREAD_PAGE = "ThreadPage";
    public static final String POST_LAST_READ = "PostLastRead";
    public PublishSubject<Integer> OnPageCountUpdateSubject = PublishSubject.create();
    @Inject ScrapedPostChildController _controller;
    @Bind(R.id.post_recycle_view) UltimateRecyclerView mRecycleView;
    @Bind(R.id.post_progress_loader) MaterialProgressBar mMaterialProgressBar;
    @State int mPageToLoad;
    private int mThreadId;
    private ScrapedPostAdapter scrapedPostAdapter;
    private String mThreadTitle;
    private int mPostLastReadId;

    public static ScrapedPostChildFragment newInstance(int threadId, String threadTitle, int page, int postLastRead) {
        ScrapedPostChildFragment fragment = new ScrapedPostChildFragment();
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
    public void onResume() {
        super.onResume();
        WhirlpoolApp.getInstance().trackScreenView("Scraped Post Child Fragment");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
    }

    @Override
    public void onDestroy() {
        OnPageCountUpdateSubject.onCompleted();
        super.onDestroy();
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
        View inflate = inflater.inflate(R.layout.fragment_scraped_post_child, container, false);

        ButterKnife.bind(this, inflate);
        ((WhirlpoolApp) getActivity().getApplication()).getDaggerComponent().inject(this);
        _controller.attach(this);

        Icepick.restoreInstanceState(this, savedInstanceState);

        SetupRecycleView();
        loadPosts();

        return inflate;
    }

    /**
     * Attempt to fix a strange but when a longer toolbar title is displayed after a shorter one.
     * In that case, the long title gets truncated. Apparently the fix is to set title in
     * oncreatecontextmenu.
     */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        setToolbarTitle(Html.fromHtml(mThreadTitle).toString());
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void SetupRecycleView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecycleView.setLayoutManager(layoutManager);

        scrapedPostAdapter = new ScrapedPostAdapter();
        scrapedPostAdapter.OnReplyPostClickedObservable.subscribe(scrapedPost -> LaunchReplyPostInBrowser(mThreadId, scrapedPost.getId()));

        mRecycleView.setAdapter(scrapedPostAdapter);
        mRecycleView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getContext()).showLastDivider().build());

        mRecycleView.setDefaultOnRefreshListener(this::loadPosts);
    }

    private void LaunchReplyPostInBrowser(int mThreadId, String replyId) {
        Uri url = Uri.parse(StringConstants.REPLY_URL + String.valueOf(mThreadId) + "&r=" + String.valueOf(replyId));
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, url);
        startActivity(browserIntent);
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

        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(StringConstants.THREAD_URL + String.valueOf(mThreadId)));
        startActivity(browserIntent);
    }

    @Override
    public void DisplayPosts(List<ScrapedPost> posts) {
        scrapedPostAdapter.SetPosts(posts);
        ScrollToFirstUnreadItem();
    }

    @Override
    public void SetTitle(String thread_title) {
        setToolbarTitle(thread_title);
    }

    @Override
    public void UpdatePageCount(int pageCount) {
        OnPageCountUpdateSubject.onNext(pageCount);
    }

    private void ScrollToFirstUnreadItem() {
        if (mPostLastReadId > 0) {
            int position = mPostLastReadId - ((mPageToLoad - 1) * StringConstants.POST_PER_PAGE);
            mRecycleView.scrollVerticallyToPosition(position - 1);
            mPostLastReadId = 0;
        } else
            mRecycleView.scrollVerticallyToPosition(0);
    }
}