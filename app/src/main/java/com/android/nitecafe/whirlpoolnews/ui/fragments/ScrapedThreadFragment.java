package com.android.nitecafe.whirlpoolnews.ui.fragments;

import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
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
import com.android.nitecafe.whirlpoolnews.web.interfaces.IWatchedThreadService;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.marshalchen.ultimaterecyclerview.divideritemdecoration.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import icepick.Icepick;
import icepick.State;
import me.zhanghai.android.materialprogressbar.MaterialProgressBar;
import rx.subjects.PublishSubject;

public class ScrapedThreadFragment extends BaseFragment implements IScrapedThreadFragment {

    public static final String FORUM_ID = "ForumId";
    public static final String FORUM_NAME = "ForumTitle";
    public static final String FORUM_GROUP_ID = "ForumGroupId";
    public PublishSubject<Void> OnFragmentDestroySubject = PublishSubject.create();
    public PublishSubject<Void> OnFragmentCreateViewSubject = PublishSubject.create();
    @Inject ScrapedThreadController _controller;
    @Inject IWatchedThreadService mIWatchedThreadService;
    @Bind(R.id.thread_recycle_view) UltimateRecyclerView mRecycleView;
    @Bind(R.id.thread_progress_loader) MaterialProgressBar mMaterialProgressBar;
    @Bind(R.id.toolbar_scraped_thread) Toolbar threadToolbar;
    @Bind(R.id.spinner_scraped_groups) Spinner mSpinner;
    @Bind(R.id.btn_thread_pages) Button mButtonSelectPage;
    @State int mGroupId;
    @State int spinnerSelectedPosition;
    @State int pageToLoad = 1;
    Map<String, Integer> mThreadGroups;
    private IOnThreadClicked listener;
    private int mForumId;
    private String mForumTitle;
    private ScrapedThreadAdapter forumThreadAdapter;

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
        OnFragmentDestroySubject.onNext(null);
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
        OnFragmentCreateViewSubject.onCompleted();
        OnFragmentDestroySubject.onCompleted();
        super.onDetach();
    }

    @Override public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View inflate = inflater.inflate(R.layout.fragment_scraped_thread, container, false);

        ButterKnife.bind(this, inflate);
        ((WhirlpoolApp) getActivity().getApplication()).getDaggerComponent().inject(this);
        _controller.Attach(this);
        Icepick.restoreInstanceState(this, savedInstanceState);
        OnFragmentCreateViewSubject.onNext(null);

        SetSpinnerArrowToWhite();
        SetupRecycleView();
        SetupToolbar();
        loadThreads();

        return inflate;
    }

    private void SetupToolbar() {
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
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setToolbarTitle(mForumTitle);
    }

    private void SetupRecycleView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecycleView.setLayoutManager(layoutManager);

        forumThreadAdapter = new ScrapedThreadAdapter(mIWatchedThreadService);
        forumThreadAdapter.getOnThreadClickedObservable()
                .subscribe(scrapedThread -> listener.OnThreadClicked(scrapedThread.getID(), scrapedThread.getTitle(), scrapedThread.getPageCount()));
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
        _controller.GetScrapedThreads(mForumId, pageToLoad, mGroupId);
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
    public void DisplayThreads(List<ScrapedThread> threads) {
        forumThreadAdapter.SetThreads(threads);
        ResetScrollPositionToTop();
    }

    private void ResetScrollPositionToTop() {
        mRecycleView.scrollVerticallyToPosition(0);
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
        pageToLoad = page;
        mButtonSelectPage.setText(String.format("%d / %d", page, pageCount));
        updateNavigationButtonVisibility();
    }

    private void SetSpinnerArrowToWhite() {
        mSpinner.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);

    }

    private int getCurrentGroupId(String item) {
        int currentGroupId;
        if (mThreadGroups.containsKey(item))
            currentGroupId = mThreadGroups.get(item);
        else
            currentGroupId = 0;
        return currentGroupId;
    }

    @Override
    public void SetupGroupSpinnerDropDown(Map<String, Integer> groups, int groupId) {
        mGroupId = groupId;
        if (mSpinner.getAdapter() == null) {
            List<String> groupsList = new ArrayList<>();
            groupsList.add("All");
            for (String g : groups.keySet()) {
                groupsList.add(g);
            }
            ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<>(getContext(), R.layout.spinner_row, groupsList);
            mSpinner.setAdapter(stringArrayAdapter);
            mSpinner.setSelection(spinnerSelectedPosition);
            setSpinnerListener();
            mThreadGroups = groups;
        }

    }

    private void setSpinnerListener() {
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinnerSelectedPosition = position;
                final String item = (String) mSpinner.getAdapter().getItem(position);
                int selectedGroupId = getCurrentGroupId(item);

                if (mGroupId != selectedGroupId) {
                    mRecycleView.setRefreshing(true);
                    _controller.GetScrapedThreads(mForumId, mThreadGroups.get(item));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
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
                .inputType(InputType.TYPE_CLASS_NUMBER)
                .input("Enter a page number", "", false, (dialog, input) -> {
                    try {
                        int pageNumber = Integer.parseInt(input.toString());
                        if (pageNumber <= _controller.getTotalPage())
                            _controller.GetScrapedThreads(mForumId, pageNumber, mGroupId);

                    } catch (NumberFormatException e) {
                        Log.e("Enter a page number", "Failed to parse input text into a number");
                    }
                })
                .positiveText("Go")
                .negativeText("Cancel")
                .build().show();
    }
}
