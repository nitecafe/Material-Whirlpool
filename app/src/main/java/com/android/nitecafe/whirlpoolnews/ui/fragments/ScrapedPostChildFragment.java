package com.android.nitecafe.whirlpoolnews.ui.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.customtabs.CustomTabsService;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Html;
import android.text.InputType;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.android.nitecafe.whirlpoolnews.R;
import com.android.nitecafe.whirlpoolnews.WhirlpoolApp;
import com.android.nitecafe.whirlpoolnews.constants.StringConstants;
import com.android.nitecafe.whirlpoolnews.controllers.ScrapedPostChildController;
import com.android.nitecafe.whirlpoolnews.models.PostBookmark;
import com.android.nitecafe.whirlpoolnews.models.ScrapedPost;
import com.android.nitecafe.whirlpoolnews.ui.adapters.ScrapedPostAdapter;
import com.android.nitecafe.whirlpoolnews.ui.interfaces.IScrapedPostChildFragment;
import com.android.nitecafe.whirlpoolnews.utilities.WhirlpoolUtils;
import com.android.nitecafe.whirlpoolnews.utilities.interfaces.IPreferencesGetter;
import com.bartoszlipinski.recyclerviewheader2.RecyclerViewHeader;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.marshalchen.ultimaterecyclerview.ui.divideritemdecoration.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

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
    private final int maxNumberOfPostReplyPrefetch = 2;
    public PublishSubject<Integer> OnPageCountUpdateSubject = PublishSubject.create();
    @Inject ScrapedPostChildController _controller;
    @Inject IPreferencesGetter preferencesGetter;
    @Inject @Named("browser") PublishSubject<Uri> launchBrowserSubject;
    @Inject @Named("prefetchBundle") PublishSubject<List<Bundle>> prefetchBundleSubject;
    @Bind(R.id.post_recycle_view) UltimateRecyclerView mRecycleView;
    @Bind(R.id.post_progress_loader) MaterialProgressBar mMaterialProgressBar;
    @Bind(R.id.post_recycle_view_header) RecyclerViewHeader mRecyclerViewHeader;
    @Bind(R.id.post_note) TextView mNoteTextView;
    @State int mPageToLoad;
    @State Parcelable mScrollPosition;
    private int mThreadId;
    private ScrapedPostAdapter scrapedPostAdapter;
    private String mThreadTitle;
    private int mPostLastReadId;
    private int mTotalPageCount;
    private LinearLayoutManager layoutManager;

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
        mThreadTitle = Html.fromHtml(getArguments().getString(THREAD_TITLE, "")).toString();
    }

    @Override
    public void onResume() {
        super.onResume();
        WhirlpoolApp.getInstance().trackScreenView("Scraped Post Child Fragment");
    }

    @Override
    public void onPause() {
        super.onPause();
        saveScrollPosition();
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
        mNoteTextView.setMovementMethod(LinkMovementMethod.getInstance());
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
        setToolbarTitle(mThreadTitle);
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    private void SetupRecycleView() {
        layoutManager = new LinearLayoutManager(getContext());
        mRecycleView.setLayoutManager(layoutManager);

        scrapedPostAdapter = new ScrapedPostAdapter(_controller, preferencesGetter);
        mSubscriptions.add(scrapedPostAdapter.OnEditPostClickedObservable.subscribe(scrapedPost -> LaunchEditPostInBrowser(scrapedPost.getId())));
        mSubscriptions.add(scrapedPostAdapter.OnReplyPostClickedObservable.subscribe(scrapedPost -> LaunchReplyPostInBrowser(mThreadId, scrapedPost.getId())));
        mSubscriptions.add(scrapedPostAdapter.OnSharePostClickedObservable.subscribe(scrapedPost -> sharePostToOtherApp(scrapedPost)));
        mSubscriptions.add(scrapedPostAdapter.OnAddToBookmarkClickedObservable.subscribe(bookmark -> addBookMark(bookmark)));
        mSubscriptions.add(scrapedPostAdapter.OnRemoveFromBookmarkClickedObservable.subscribe(integer -> _controller.removeFromBookmark(integer)));
        mSubscriptions.add(scrapedPostAdapter.OnViewUserInfoClickedObservable.subscribe(integer -> launchUserInfoPage(integer)));
        mSubscriptions.add(scrapedPostAdapter.OnOpenCustomTabClickedObservable.subscribe(scrapedPost -> LaunchPostInBrowser(scrapedPost.getId())));

        mRecyclerViewHeader.attachTo(mRecycleView.mRecyclerView);
        mRecycleView.setAdapter(scrapedPostAdapter);
        mRecycleView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getContext()).showLastDivider().build());

        mRecycleView.setDefaultOnRefreshListener(this::loadPosts);
    }

    private void saveScrollPosition() {
        mScrollPosition = layoutManager.onSaveInstanceState();
    }

    private void LaunchEditPostInBrowser(String postId) {
        Uri url = Uri.parse(StringConstants.WHIRLPOOL_EDIT_URL + String.valueOf(postId));
        launchBrowserSubject.onNext(url);
    }

    private void sharePostToOtherApp(String shortCode) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, mThreadTitle + " " + shortCode);
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent, getContext().getString(R.string.sharing_post_intent_title)));

    }

    private void addBookMark(PostBookmark bookmark) {
        new MaterialDialog.Builder(getActivity())
                .title("Bookmark Name:")
                .inputType(InputType.TYPE_CLASS_TEXT)
                .input("Bookmark 1", "", false, (dialog, input) -> {
                    bookmark.setBookMarkName(input.toString());
                    bookmark.setThreadId(mThreadId);
                    bookmark.setPageLocated(mPageToLoad);
                    bookmark.setThreadTitle(mThreadTitle);
                    bookmark.setTotalPage(mTotalPageCount);
                    _controller.addToPostBookmark(bookmark);
                })
                .positiveText("Add")
                .negativeText("Cancel")
                .build().show();
    }

    private void LaunchReplyPostInBrowser(int mThreadId, String replyId) {
        Uri url = Uri.parse(StringConstants.REPLY_URL + String.valueOf(mThreadId) + "&r=" + String.valueOf(replyId));
        launchBrowserSubject.onNext(url);
    }

    private void launchUserInfoPage(int userId) {
        Uri url = Uri.parse(StringConstants.USER_INFO_URL + userId);
        launchBrowserSubject.onNext(url);
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
    }

    @Override
    public void DisplayPosts(List<ScrapedPost> posts, String note) {
        scrapedPostAdapter.SetPosts(posts);
        SetupNoteBar(note);
        ScrollToFirstUnreadItem();
        prefetchSomeReplyUri(mThreadId, posts);
    }

    private void SetupNoteBar(String note) {
        if (note != null && !note.isEmpty()) {
            String s = WhirlpoolUtils.replaceAllWhirlpoolLinksWithInternalAppLinks(note, _controller.isDarkTheme());
            mNoteTextView.setVisibility(View.VISIBLE);
            mNoteTextView.setText(Html.fromHtml(s));
            if(_controller.isDarkTheme())
                mRecyclerViewHeader.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.black));
            else
                mRecyclerViewHeader.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.primary_light));
        } else
            mNoteTextView.setVisibility(View.GONE);
    }

    @Override
    public void SetTitle(String thread_title) {
        setToolbarTitle(thread_title);
    }

    @Override
    public void UpdatePageCount(int pageCount) {
        mTotalPageCount = pageCount;
        OnPageCountUpdateSubject.onNext(pageCount);
    }

    @Override
    public void showAddedToBookmarkMessage() {
        Snackbar.make(mRecycleView, R.string.message_post_added_bookmark, Snackbar.LENGTH_SHORT)
                .show();
    }

    @Override
    public void showRemoveFromBookmarkMessage() {
        Snackbar.make(mRecycleView, R.string.message_post_removed_bookmark, Snackbar.LENGTH_SHORT)
                .show();
    }

    @Override
    public void LaunchThreadInBrowser() {
        LaunchPostInBrowser(String.valueOf(mPostLastReadId));
    }

    private void LaunchPostInBrowser(String postId) {
        final Uri parse = Uri.parse(StringConstants.THREAD_URL + String.valueOf(mThreadId) + "&p=" +
                String.valueOf(mPageToLoad) + "&#r" + postId);
        launchBrowserSubject.onNext(parse);
    }

    private void ScrollToFirstUnreadItem() {
        if (mScrollPosition != null) {
            mRecycleView.getLayoutManager().onRestoreInstanceState(mScrollPosition);
        } else if (mPostLastReadId > 0) {
            //calculate number of post before and take away the answer
            int position = mPostLastReadId - ((mPageToLoad - 1) * StringConstants.POST_PER_PAGE);
            mRecycleView.scrollVerticallyToPosition(position - 1);
            mPostLastReadId = 0;
        } else
            mRecycleView.scrollVerticallyToPosition(0);
    }

    private void prefetchSomeReplyUri(int threadId, List<ScrapedPost> scrapedPosts) {
        int length = scrapedPosts.size();
        int maxLinks;
        if (length > maxNumberOfPostReplyPrefetch)
            maxLinks = maxNumberOfPostReplyPrefetch;
        else
            maxLinks = length;

        List<Bundle> bundles = new ArrayList<>(maxLinks);
        //preload the last $maxLinks post
        for (int i = 0; i < maxLinks; i++) {
            Uri url = Uri.parse(StringConstants.REPLY_URL + String.valueOf(threadId) + "&r=" + String.valueOf(scrapedPosts.get(length - 1 - i)));
            Bundle bundle = new Bundle();
            bundle.putParcelable(CustomTabsService.KEY_URL, url);
            bundles.add(bundle);
        }
        prefetchBundleSubject.onNext(bundles);
    }

    public void attachRefreshSubject(PublishSubject<Void> onRefreshClickedSubject) {
        mSubscriptions.add(onRefreshClickedSubject.subscribe(aVoid -> loadPosts(),
                throwable -> Log.e("ScrappedPostChildFragment", "Failed to refresh")));
    }
}
