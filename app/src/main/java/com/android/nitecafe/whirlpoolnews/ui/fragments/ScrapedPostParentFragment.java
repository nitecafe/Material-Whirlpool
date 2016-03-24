package com.android.nitecafe.whirlpoolnews.ui.fragments;

import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.android.nitecafe.whirlpoolnews.R;
import com.android.nitecafe.whirlpoolnews.WhirlpoolApp;
import com.android.nitecafe.whirlpoolnews.constants.StringConstants;
import com.android.nitecafe.whirlpoolnews.controllers.ScrapedPostParentController;
import com.android.nitecafe.whirlpoolnews.ui.interfaces.IScrapedPostParentFragment;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.Bind;
import butterknife.ButterKnife;
import icepick.Icepick;
import icepick.State;
import rx.subjects.PublishSubject;

public class ScrapedPostParentFragment extends BaseFragment implements IScrapedPostParentFragment {

    public static final String THREAD_ID = "ThreadId";
    public static final String THREAD_TOTAL_PAGE = "ThreadTotalPage";
    public static final String THREAD_TITLE = "ThreadTitle";
    public static final String THREAD_PAGE = "ThreadPage";
    public static final String POST_LAST_READ = "PostLastRead";
    public PublishSubject<Void> OnFragmentDestroySubject = PublishSubject.create();
    public PublishSubject<Void> OnFragmentCreateViewSubject = PublishSubject.create();
    public PublishSubject<Void> OnRefreshClickedSubject = PublishSubject.create();
    @State int mPageToLoad;
    @Bind(R.id.spinner_post_page) Spinner pageNumberSpinner;
    @Bind(R.id.toolbar_post) Toolbar postToolbar;
    @Bind(R.id.viewpager_post) ViewPager postViewPager;
    @Inject ScrapedPostParentController mController;
    @Inject @Named("browser") PublishSubject<Uri> launchBrowserSubject;
    @Inject @Named("prefetch") PublishSubject<Uri> prefetchSubject;
    private int mThreadId;
    private int mPostLastReadId;
    private String mThreadTitle;
    private int mThreadTotalPage;

    public static ScrapedPostParentFragment newInstance(int threadId, String threadTitle, int page, int postLastRead, int totalPage) {
        ScrapedPostParentFragment fragment = new ScrapedPostParentFragment();
        Bundle args = new Bundle();
        args.putInt(THREAD_ID, threadId);
        args.putInt(THREAD_PAGE, page);
        args.putInt(POST_LAST_READ, postLastRead);
        args.putString(THREAD_TITLE, threadTitle);
        args.putInt(THREAD_TOTAL_PAGE, totalPage);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onDestroyView() {
        mController.Attach(null);
        OnFragmentDestroySubject.onNext(null);
        super.onDestroyView();
    }

    @Override
    public void onResume() {
        super.onResume();
        WhirlpoolApp.getInstance().trackScreenView("Scraped post Parent Fragment");
    }

    @Override
    public void onDetach() {
        OnFragmentCreateViewSubject.onCompleted();
        OnFragmentDestroySubject.onCompleted();
        OnRefreshClickedSubject.onCompleted();
        super.onDetach();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mThreadId = getArguments().getInt(THREAD_ID, 0);
        mThreadTotalPage = getArguments().getInt(THREAD_TOTAL_PAGE, 0);
        mPageToLoad = getArguments().getInt(THREAD_PAGE, 1);
        mPostLastReadId = getArguments().getInt(POST_LAST_READ, 0);
        mThreadTitle = getArguments().getString(THREAD_TITLE, "");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View inflate = inflater.inflate(R.layout.fragment_scraped_post_parent, container, false);

        ButterKnife.bind(this, inflate);
        ((WhirlpoolApp) getActivity().getApplication()).getDaggerComponent().inject(this);
        OnFragmentCreateViewSubject.onNext(null);
        mController.Attach(this);
        Icepick.restoreInstanceState(this, savedInstanceState);
        setupViewPager();
        SetupToolbar();
        SetupPageSpinnerDropDown(mThreadTotalPage, 1);
        SetupSpinnerItemEvents();
        setUpToolbarActionButtons();
        prefetchReplyPage();

        if (mPageToLoad > 0)
            postViewPager.setCurrentItem(mPageToLoad - 1);
        return inflate;
    }

    private void updatePageCount(int pageCount) {
        mThreadTotalPage = pageCount;
        SetupPageSpinnerDropDown(pageCount, mPageToLoad);

        if (mPageToLoad == -1) {
            mPageToLoad = mThreadTotalPage;
            postViewPager.setCurrentItem(mPageToLoad - 1);
        }

        updatePageToLoad();
    }

    @Override
    public void setUpToolbarActionButtons() {
        if (mController.IsThreadWatched(mThreadId)) {
            changeToWatched();
        } else {
            changeToUnwatched();
        }
    }

    private void changeToUnwatched() {
        MenuItem markRead = postToolbar.getMenu().findItem(R.id.menuitem_mark_read);
        MenuItem watchPost = postToolbar.getMenu().findItem(R.id.menuitem_watch_post);
        MenuItem unwatchPost = postToolbar.getMenu().findItem(R.id.menuitem_unwatch_post);
        markRead.setVisible(false);
        unwatchPost.setVisible(false);
        watchPost.setVisible(true);
    }

    private void changeToWatched() {
        MenuItem markRead = postToolbar.getMenu().findItem(R.id.menuitem_mark_read);
        MenuItem watchPost = postToolbar.getMenu().findItem(R.id.menuitem_watch_post);
        MenuItem unwatchPost = postToolbar.getMenu().findItem(R.id.menuitem_unwatch_post);
        markRead.setVisible(true);
        unwatchPost.setVisible(true);
        watchPost.setVisible(false);
    }

    private void setupViewPager() {
        PostPagerAdapter viewPagerAdapter = new PostPagerAdapter(getChildFragmentManager(), mThreadTotalPage);
        postViewPager.setAdapter(viewPagerAdapter);
        postViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                pageNumberSpinner.setSelection(position);
                updatePageToLoad();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void SetupSpinnerItemEvents() {
        pageNumberSpinner.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        pageNumberSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (postViewPager.getCurrentItem() != position) {
                    postViewPager.setCurrentItem(position);
                }

                if (mController.shouldMarkThreadAsRead(mThreadId) && position + 1 == mThreadTotalPage)
                    mController.MarkThreadAsReadNoNotification(mThreadId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void SetupToolbar() {
        postToolbar.inflateMenu(R.menu.menu_post_toolbar);
        postToolbar.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.menuitem_mark_read:
                    WhirlpoolApp.getInstance().trackEvent(StringConstants.ANALYTIC_POST_TOOLBAR, "Mark Read Post", "");
                    mController.MarkThreadAsRead(mThreadId);
                    break;
                case R.id.menuitem_unwatch_post:
                    WhirlpoolApp.getInstance().trackEvent(StringConstants.ANALYTIC_POST_TOOLBAR, "Unwatch Post", "");
                    mController.UnwatchThread(mThreadId);
                    break;
                case R.id.menuitem_watch_post:
                    WhirlpoolApp.getInstance().trackEvent(StringConstants.ANALYTIC_POST_TOOLBAR, "Watch Post", "");
                    mController.WatchThread(mThreadId);
                    break;
                case R.id.menuitem_go_to_last_page:
                    WhirlpoolApp.getInstance().trackEvent(StringConstants.ANALYTIC_POST_TOOLBAR, "Go To Last Post", "");
                    postViewPager.setCurrentItem(mThreadTotalPage - 1);
                    break;
                case R.id.menuitem_go_to_first_page:
                    WhirlpoolApp.getInstance().trackEvent(StringConstants.ANALYTIC_POST_TOOLBAR, "Go To First Post", "");
                    postViewPager.setCurrentItem(0);
                    break;
                case R.id.menuitem_reply_post:
                    WhirlpoolApp.getInstance().trackEvent(StringConstants.ANALYTIC_POST_TOOLBAR, "Reply Post", "");
                    launchReplyPageInBrowser();
                    break;
                case R.id.menuitem_refresh:
                    WhirlpoolApp.getInstance().trackEvent(StringConstants.ANALYTIC_POST_TOOLBAR, "Refresh Post", "");
                    OnRefreshClickedSubject.onNext(null);
                    break;
                case R.id.menuitem_open_web_version:
                    WhirlpoolApp.getInstance().trackEvent(StringConstants.ANALYTIC_POST_TOOLBAR, "Open Web Version", "");
                    launchThreadInBrowser();
                    break;
            }
            return true;
        });
    }

    private void launchReplyPageInBrowser() {
        Uri parse = Uri.parse(StringConstants.REPLY_URL + String.valueOf(mThreadId));
        launchBrowserSubject.onNext(parse);
    }

    private void launchThreadInBrowser() {
        Uri parse = Uri.parse(StringConstants.THREAD_URL + String.valueOf(mThreadId) + "&p=" +
                String.valueOf(mPageToLoad));
        launchBrowserSubject.onNext(parse);
    }

    private void prefetchReplyPage() {
        Uri parse = Uri.parse(StringConstants.REPLY_URL + String.valueOf(mThreadId));
        prefetchSubject.onNext(parse);
    }

    public void SetupPageSpinnerDropDown(int pageCount, int page) {
        List<String> numberPages = new ArrayList<>();
        for (int i = 1; i <= pageCount; i++) {
            numberPages.add(i + " / " + pageCount);
        }
        ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<>(getContext(), R.layout.spinner_row, numberPages);
        pageNumberSpinner.setAdapter(stringArrayAdapter);
        pageNumberSpinner.setSelection(page - 1);
    }

    private void updatePageToLoad() {
        mPageToLoad = postViewPager.getCurrentItem() + 1;
    }

    public class PostPagerAdapter extends FragmentStatePagerAdapter {
        private int mTotalPage;

        public PostPagerAdapter(FragmentManager fragmentManager, int totalPage) {
            super(fragmentManager);
            mTotalPage = totalPage;
        }

        @Override
        public int getCount() {
            return mTotalPage;
        }

        @Override
        public Fragment getItem(int position) {
            final ScrapedPostChildFragment scrapedPostChildFragment = ScrapedPostChildFragment.newInstance(mThreadId, mThreadTitle, position + 1, mPostLastReadId);
            mSubscriptions.add(scrapedPostChildFragment.OnPageCountUpdateSubject.subscribe(integer -> {
                if (integer != mTotalPage) {
                    this.mTotalPage = integer;
                    notifyDataSetChanged();
                    updatePageCount(mTotalPage);
                }
            }));
            scrapedPostChildFragment.attachRefreshSubject(OnRefreshClickedSubject);
            return scrapedPostChildFragment;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "Page " + position;
        }

    }

}
