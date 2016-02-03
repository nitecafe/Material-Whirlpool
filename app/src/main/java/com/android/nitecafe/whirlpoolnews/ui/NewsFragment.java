package com.android.nitecafe.whirlpoolnews.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.nitecafe.whirlpoolnews.R;
import com.android.nitecafe.whirlpoolnews.WhirlpoolApp;
import com.android.nitecafe.whirlpoolnews.constants.StringConstants;
import com.android.nitecafe.whirlpoolnews.controllers.NewsController;
import com.android.nitecafe.whirlpoolnews.interfaces.INewsFragment;
import com.android.nitecafe.whirlpoolnews.models.News;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.marshalchen.ultimaterecyclerview.divideritemdecoration.HorizontalDividerItemDecoration;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

public class NewsFragment extends BaseFragment implements INewsFragment {

    @Bind(R.id.news_recycle_view) UltimateRecyclerView newsRecycleView;
    @Bind(R.id.news_progress_loader) MaterialProgressBar mMaterialProgressBar;
    @Inject NewsController _controller;
    @Inject Bus eventBus;
    private NewsAdapter newsAdapter;

    @Override
    public void onPause() {
        eventBus.unregister(this);
        super.onPause();
    }

    @Override
    public void onResume() {
        eventBus.register(this);
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        _controller.Attach(null);
        super.onDestroyView();
    }

    @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View inflate = inflater.inflate(R.layout.fragment_news, container, false);

        ButterKnife.bind(this, inflate);
        ((WhirlpoolApp) getActivity().getApplication()).getDaggerComponent().inject(this);
        _controller.Attach(this);

        SetupRecycleView();

        LoadNews();

        return inflate;
    }

    @Override public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setToolbarTitle("Industry News");
    }

    private void SetupRecycleView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        newsRecycleView.setLayoutManager(layoutManager);
        newsRecycleView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getContext()).build());

        newsAdapter = new NewsAdapter(eventBus);
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
        Snackbar.make(newsRecycleView, "Can't load. Please check connection.", Snackbar.LENGTH_SHORT)
                .setAction("Retry", view -> LoadNews())
                .show();
    }

    @Override
    public void HideRefreshLoader() {
        newsRecycleView.setRefreshing(false);
    }

    @Subscribe
    public void OnItemClicked(String newsId) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(StringConstants.NEWS_REDIRECT_URL + newsId));
        startActivity(browserIntent);
    }
}


