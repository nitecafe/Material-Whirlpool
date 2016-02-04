package com.android.nitecafe.whirlpoolnews.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.nitecafe.whirlpoolnews.R;
import com.android.nitecafe.whirlpoolnews.models.News;
import com.android.nitecafe.whirlpoolnews.ui.interfaces.IRecycleViewItemClick;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> implements View.OnClickListener {

    private List<News> mNews = new ArrayList<>();
    private IRecycleViewItemClick itemClickHandler;

    public NewsAdapter(IRecycleViewItemClick itemClickHandler) {
        this.itemClickHandler = itemClickHandler;
    }

    public void SetNews(List<News> news) {
        mNews = news;
        notifyDataSetChanged();
    }

    @Override
    public NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_news, parent, false);
        inflate.setOnClickListener(this);
        return new NewsViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(NewsViewHolder holder, int position) {
        holder.newsBlurb.setText(mNews.get(position).getBLURB());
        holder.newsTitle.setText(mNews.get(position).getTITLE());
        holder.mItemView.setTag(mNews.get(position).getID());
    }

    @Override
    public int getItemCount() {
        return mNews.size();
    }

    @Override
    public void onClick(View view) {
        itemClickHandler.OnItemClicked(view.getTag().toString());
    }

    public static class NewsViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.news_title) TextView newsTitle;
        @Bind(R.id.news_blurb) TextView newsBlurb;
        public View mItemView;

        NewsViewHolder(View itemView) {
            super(itemView);
            mItemView = itemView;
            ButterKnife.bind(this, itemView);
        }
    }

}
