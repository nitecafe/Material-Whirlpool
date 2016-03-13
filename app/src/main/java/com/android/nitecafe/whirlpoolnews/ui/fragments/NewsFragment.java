package com.android.nitecafe.whirlpoolnews.ui.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.nitecafe.whirlpoolnews.R;
import com.android.nitecafe.whirlpoolnews.WhirlpoolApp;
import com.android.nitecafe.whirlpoolnews.constants.StringConstants;
import com.android.nitecafe.whirlpoolnews.controllers.NewsController;
import com.android.nitecafe.whirlpoolnews.models.News;
import com.android.nitecafe.whirlpoolnews.ui.adapters.NewsAdapter;
import com.android.nitecafe.whirlpoolnews.ui.interfaces.INewsFragment;
import com.android.nitecafe.whirlpoolnews.ui.interfaces.IRecycleViewItemClick;
import com.android.nitecafe.whirlpoolnews.utilities.customTabs.CustomTabsActivityHelper;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.marshalchen.ultimaterecyclerview.divideritemdecoration.HorizontalDividerItemDecoration;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

public class NewsFragment extends BaseFragment implements INewsFragment, IRecycleViewItemClick {

    @Bind(R.id.news_recycle_view) UltimateRecyclerView newsRecycleView;
    @Bind(R.id.news_progress_loader) MaterialProgressBar mMaterialProgressBar;
    @Inject CustomTabsActivityHelper customActivityTabsHelper;
    @Inject NewsController _controller;
    private NewsAdapter newsAdapter;

    @Override
    public void onResume() {
        super.onResume();
        WhirlpoolApp.getInstance().trackScreenView("News Fragment");
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
        View inflate = inflater.inflate(R.layout.fragment_news, container, false);

        ButterKnife.bind(this, inflate);
        ((WhirlpoolApp) getActivity().getApplication()).getDaggerComponent().inject(this);
        _controller.Attach(this);

        SetupRecycleView();

        LoadNews();

        return inflate;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setToolbarTitle(getActivity().getString(R.string.title_industry_news));
    }

    private void SetupRecycleView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        newsRecycleView.setLayoutManager(layoutManager);
        newsRecycleView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getContext()).build());

        newsAdapter = new NewsAdapter(this);
        newsRecycleView.setAdapter(newsAdapter);

        newsRecycleView.setDefaultOnRefreshListener(this::LoadNews);
    }

    private void LoadNews() {
        _controller.GetNews();
    }

    @Override
    public void DisplayNews(List<News> news) {
        newsAdapter.SetNews(news);
    }

    @Override
    public void HideCenterProgressBar() {
        mMaterialProgressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void DisplayErrorMessage() {
        Snackbar.make(newsRecycleView, R.string.message_check_connection, Snackbar.LENGTH_LONG)
                .setAction(R.string.action_message_retry, view -> LoadNews())
                .show();
    }

    @Override
    public void HideRefreshLoader() {
        newsRecycleView.setRefreshing(false);
    }

    @Override
    public void OnItemClicked(int itemClicked, String title) {
        WhirlpoolApp.getInstance().trackEvent("RecycleView Click", "View News", "");
        final Uri parse = Uri.parse(StringConstants.NEWS_REDIRECT_URL + String.valueOf(itemClicked));
        customActivityTabsHelper.openCustomTabStandard(getActivity(), parse);
    }
}


