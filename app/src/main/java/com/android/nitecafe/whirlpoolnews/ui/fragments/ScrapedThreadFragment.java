package com.android.nitecafe.whirlpoolnews.ui.fragments;

import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.afollestad.materialdialogs.MaterialDialog;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

public class ScrapedThreadFragment extends BaseFragment implements IScrapedThreadFragment {

    public static final String FORUM_ID = "ForumId";
    public static final String FORUM_NAME = "ForumTitle";
    public static final String FORUM_GROUP_ID = "ForumGroupId";
    @Inject ScrapedThreadController _controller;
    @Inject IWatchedThreadIdentifier mIWatchedThreadIdentifier;
    @Bind(R.id.thread_recycle_view) UltimateRecyclerView mRecycleView;
    @Bind(R.id.thread_progress_loader) MaterialProgressBar mMaterialProgressBar;
    @Bind(R.id.toolbar_scraped_thread) Toolbar threadToolbar;
    @Bind(R.id.spinner_scraped_groups) Spinner mSpinner;
    @Bind(R.id.btn_thread_pages) Button mButtonSelectPage;
    private IOnThreadClicked listener;
    private int mForumId;
    private String mForumTitle;
    private ScrapedThreadAdapter forumThreadAdapter;
    private int mGroupId;
    private Map<String, Integer> mThreadGroups;

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
        _controller.Attach(this);

        SetSpinnerArrowToWhite();
        SetupRecycleView();
        threadToolbar.inflateMenu(R.menu.menu_thread_toolbar);
        threadToolbar.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.menuitem_back_thread:
                    _controller.loadPreviousPage(mForumId, mGroupId);
                    break;
                case R.id.menuitem_next_thread:
                    _controller.loadNextPage(mForumId, mGroupId);
                    break;
            }
            return true;
        });
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

        forumThreadAdapter.getOnThreadClickedObservable().subscribe(
                recent -> listener.OnThreadClicked(recent.getID(), recent.getTitle()));
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

    @Override
    public void ShowRefreshLoader() {
        mRecycleView.setRefreshing(true);
    }

    @Override
    public void SetupPageSpinnerDropDown(int pageCount, int page) {
        mButtonSelectPage.setText(String.format("%d / %d", page, pageCount));
        updateNavigationButtonVisibility();
    }

    private void SetSpinnerArrowToWhite() {
        mSpinner.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    mRecycleView.setRefreshing(true);
                    _controller.GetScrapedThreads(mForumId, 0);
                    mGroupId = 0;
                } else {
                    final String item = (String) mSpinner.getAdapter().getItem(position);
                    if ((mThreadGroups.containsKey(item) && mGroupId != mThreadGroups.get(item))) {
                        mRecycleView.setRefreshing(true);
                        _controller.GetScrapedThreads(mForumId, mThreadGroups.get(item));
                        mGroupId = mThreadGroups.get(item);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void SetupGroupSpinnerDropDown(Map<String, Integer> groups, int groupId) {

        if (mThreadGroups == null) {
            List<String> groupsList = new ArrayList<>();
            groupsList.add("All");
            for (String g : groups.keySet()) {
                groupsList.add(g);
            }
            ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<>(getContext(), R.layout.spinner_row, groupsList);
            mSpinner.setAdapter(stringArrayAdapter);
            mThreadGroups = groups;
        }
    }

    private void updateNavigationButtonVisibility() {
        final MenuItem backItem = threadToolbar.getMenu().findItem(R.id.menuitem_back_thread);
        final MenuItem nextItem = threadToolbar.getMenu().findItem(R.id.menuitem_next_thread);
        if (_controller.IsAtFirstPage())
            backItem.setEnabled(false);
        else
            backItem.setEnabled(true);

        if (_controller.IsAtLastPage())
            nextItem.setEnabled(false);
        else
            nextItem.setEnabled(true);
    }

    @OnClick(R.id.btn_thread_pages)
    public void ShowPageGoToPopup() {
        new MaterialDialog.Builder(getActivity())
                .title("Go To Page:")
                .inputRange(1, 3)
                .inputType(InputType.TYPE_CLASS_NUMBER)
                .input("Enter a page number", "", false, (dialog, input) -> {

                })
                .positiveText("Go")
                .negativeText("Cancel")
                .build().show();
    }
}
