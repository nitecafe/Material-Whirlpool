package com.android.nitecafe.whirlpoolnews.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;

import com.android.nitecafe.whirlpoolnews.controllers.NewsController;
import com.android.nitecafe.whirlpoolnews.R;
import com.android.nitecafe.whirlpoolnews.WhirlpoolApp;
import com.android.nitecafe.whirlpoolnews.constants.StringConstants;
import com.android.nitecafe.whirlpoolnews.interfaces.INewsActivity;
import com.android.nitecafe.whirlpoolnews.models.News;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.marshalchen.ultimaterecyclerview.divideritemdecoration.HorizontalDividerItemDecoration;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class NewsActivity extends AppCompatActivity implements INewsActivity {

    @Bind(R.id.news_recycle_view) UltimateRecyclerView newsRecyclcView;
    @Inject NewsController _controller;
    @Inject Bus eventBus;
    private NewsAdapter newsAdapter;

    @Override
    protected void onPause() {
        eventBus.unregister(this);
        super.onPause();
    }

    @Override
    protected void onResume() {
        eventBus.register(this);
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        _controller.Attach(null);
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        ButterKnife.bind(this);
        ((WhirlpoolApp) getApplication()).getDaggerComponent().inject(this);
        _controller.Attach(this);

        SetupRecycleView();
        LoadNews();
    }

    private void SetupRecycleView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        newsRecyclcView.setLayoutManager(layoutManager);
        newsRecyclcView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this).build());

        newsAdapter = new NewsAdapter(eventBus);
        newsRecyclcView.setAdapter(newsAdapter);

        newsRecyclcView.setDefaultOnRefreshListener(() ->
        {
            LoadNews();
            newsRecyclcView.setRefreshing(false);
        });
    }

    private void LoadNews() {
        _controller.GetNews();
    }

    @Override
    public void DisplayNews(List<News> news) {
        newsAdapter.SetNews(news);
    }

    @Override
    public void DisplayErrorMessage() {
        Snackbar.make(newsRecyclcView, "Can't load. Please check connection.", Snackbar.LENGTH_LONG)
                .setAction("Retry", view -> LoadNews())
                .show();
    }

    @Subscribe
    public void OnItemClicked(String newsId) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(StringConstants.NEWS_REDIRECT_URL + newsId));
        startActivity(browserIntent);
    }
}


