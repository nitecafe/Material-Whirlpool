package com.android.nitecafe.whirlpoolnews;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.nitecafe.whirlpoolnews.models.News;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {

    private List<News> mNews = new ArrayList<>();

    public void SetNews(List<News> news)
    {
        mNews = news;
        notifyDataSetChanged();
    }


    @Override
    public NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_news, parent, false);
        return new NewsViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(NewsViewHolder holder, int position) {
        holder.newsBlurb.setText(mNews.get(position).getBLURB());
        holder.newsTitle.setText(mNews.get(position).getTITLE());
    }

    @Override
    public int getItemCount() {
        return mNews.size();
    }

    public static class NewsViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.news_title) TextView newsTitle;
        @Bind(R.id.news_blurb) TextView newsBlurb;

        NewsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
