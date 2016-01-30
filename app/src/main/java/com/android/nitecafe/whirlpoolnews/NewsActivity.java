package com.android.nitecafe.whirlpoolnews;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;

import com.android.nitecafe.whirlpoolnews.models.NewsList;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.marshalchen.ultimaterecyclerview.divideritemdecoration.HorizontalDividerItemDecoration;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class NewsActivity extends AppCompatActivity {

    @Bind(R.id.news_recycle_view) UltimateRecyclerView newsRecyclcView;
    @Inject NewsController _controller;
    private NewsAdapter newsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        ButterKnife.bind(this);
        ((WhirlpoolApp) getApplication()).getDaggerComponent().inject(this);

        SetupRecycleView();
        LoadNews();
    }

    private void SetupRecycleView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        newsRecyclcView.setLayoutManager(layoutManager);
        newsRecyclcView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this).build());

        newsAdapter = new NewsAdapter();
        newsRecyclcView.setAdapter(newsAdapter);

        newsRecyclcView.setDefaultOnRefreshListener(() ->
        {
            LoadNews();
            newsRecyclcView.setRefreshing(false);
        });
    }

    private void LoadNews() {
        GetNews().subscribe(newses -> {
            newsAdapter.SetNews(newses.getNEWS());
        }, throwable -> ShowUnableToLoadSnackBar());
    }

    private Observable<NewsList> GetNews() {
        return _controller.GetNews()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

    private void ShowUnableToLoadSnackBar() {
        Snackbar.make(newsRecyclcView, "Can't load. Please check connection.", Snackbar.LENGTH_LONG)
                .setAction("Retry", view -> LoadNews())
                .show();
    }
}


