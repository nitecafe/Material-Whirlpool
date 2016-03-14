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
import com.android.nitecafe.whirlpoolnews.constants.StringConstants;
import com.android.nitecafe.whirlpoolnews.controllers.PostBookmarkController;
import com.android.nitecafe.whirlpoolnews.models.PostBookmark;
import com.android.nitecafe.whirlpoolnews.ui.adapters.PostBookmarksAdapter;
import com.android.nitecafe.whirlpoolnews.ui.interfaces.IOnThreadClicked;
import com.android.nitecafe.whirlpoolnews.ui.interfaces.IPostBookmarkFragment;
import com.android.nitecafe.whirlpoolnews.utilities.ThreadScraper;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

public class PostBookmarkFragment extends BaseFragment implements IPostBookmarkFragment {

    @Bind(R.id.post_bookmark_recycle_view) UltimateRecyclerView postBookmarkRecycleView;
    @Bind(R.id.post_bookmark_progress_loader) MaterialProgressBar mMaterialProgressBar;
    @Bind(R.id.emptyview) TextView emptyView;
    @Inject PostBookmarkController _controller;
    private PostBookmarksAdapter bookmarkAdapter;
    private IOnThreadClicked listener;

    @Override
    public void onResume() {
        super.onResume();
        WhirlpoolApp.getInstance().trackScreenView("Post Bookmarks Fragment");
    }

    @Override
    public void onDestroyView() {
        _controller.Attach(null);
        super.onDestroyView();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View inflate = inflater.inflate(R.layout.fragment_post_bookmark, container, false);

        ButterKnife.bind(this, inflate);
        ((WhirlpoolApp) getActivity().getApplication()).getDaggerComponent().inject(this);
        _controller.Attach(this);

        SetupRecycleView();
        _controller.GetPostBookmarks();

        return inflate;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setToolbarTitle(getString(R.string.title_post_bookmark));
    }

    private void SetupRecycleView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        postBookmarkRecycleView.setLayoutManager(layoutManager);

        bookmarkAdapter = new PostBookmarksAdapter();
        bookmarkAdapter.OnRemoveFromBookmarkClickedSubject.subscribe(integer -> _controller.RemovePostBookmark(integer));
        bookmarkAdapter.OnBookmarkClickedSubject.subscribe(postBookmark -> openBookmark(postBookmark));
        postBookmarkRecycleView.setAdapter(bookmarkAdapter);

        postBookmarkRecycleView.setDefaultOnRefreshListener(() -> _controller.GetPostBookmarks());
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

    private void openBookmark(PostBookmark postBookmark) {
        int i = calculatePostNoInThread(postBookmark);
        listener.OnThreadClicked(postBookmark.getThreadId(),
                postBookmark.getThreadTitle(), postBookmark.getPageLocated()
                , i, postBookmark.getTotalPage(), ThreadScraper.PUBLIC_FORUM_ID);
    }

    private int calculatePostNoInThread(PostBookmark postBookmark) {
        return postBookmark.getPositionOnPage()
                + (postBookmark.getPageLocated() - 1) * StringConstants.POST_PER_PAGE;
    }

    @Override
    public void DisplayPostBookmarks(List<PostBookmark> postBookmarks) {
        if (postBookmarks.size() == 0) {
            emptyView.setVisibility(View.VISIBLE);
        } else
            emptyView.setVisibility(View.GONE);
        bookmarkAdapter.setPostBookmarks(postBookmarks);
    }

    @Override
    public void HideCenterProgressBar() {
        mMaterialProgressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void DisplayErrorMessage() {
        Snackbar.make(postBookmarkRecycleView, R.string.message_check_connection, Snackbar.LENGTH_LONG)
                .setAction(R.string.action_message_retry, view -> _controller.GetPostBookmarks())
                .show();
    }

    @Override
    public void DisplayPostBookmarkRemovedMessage() {
        Snackbar.make(postBookmarkRecycleView, "Bookmark has been removed", Snackbar.LENGTH_LONG)
                .show();
    }
}


