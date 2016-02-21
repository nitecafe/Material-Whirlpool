package com.android.nitecafe.whirlpoolnews.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.nitecafe.whirlpoolnews.R;
import com.android.nitecafe.whirlpoolnews.models.News;
import com.android.nitecafe.whirlpoolnews.ui.interfaces.IRecycleViewItemClick;
import com.android.nitecafe.whirlpoolnews.ui.interfaces.RecyclerViewAdapterClickListener;
import com.marshalchen.ultimaterecyclerview.UltimateViewAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class NewsAdapter extends UltimateViewAdapter<NewsAdapter.NewsViewHolder> implements RecyclerViewAdapterClickListener {

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
    public NewsViewHolder getViewHolder(View view) {
        return null;
    }

    @Override
    public NewsViewHolder onCreateViewHolder(ViewGroup parent) {
        final View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_news, parent, false);
        return new NewsViewHolder(inflate, this);
    }

    @Override
    public void onBindViewHolder(NewsViewHolder holder, int position) {
        holder.newsBlurb.setText(mNews.get(position).getBLURB());
        holder.newsTitle.setText(mNews.get(position).getTITLE());
        holder.mItemView.setTag(mNews.get(position).getID());
    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        return null;
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getAdapterItemCount() {
        return mNews.size();
    }

    @Override
    public long generateHeaderId(int position) {
        return 0;
    }

    @Override
    public void recyclerViewListClicked(View v, int position) {
        final News news = mNews.get(position);
        itemClickHandler.OnItemClicked(news.getID(), news.getTITLE());
    }

    public static class NewsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public View mItemView;
        @Bind(R.id.news_title) TextView newsTitle;
        @Bind(R.id.news_blurb) TextView newsBlurb;
        private RecyclerViewAdapterClickListener mListener;

        NewsViewHolder(View itemView, RecyclerViewAdapterClickListener listener) {
            super(itemView);
            mItemView = itemView;
            mListener = listener;
            itemView.setOnClickListener(this);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onClick(View v) {
            mListener.recyclerViewListClicked(v, getAdapterPosition());
        }
    }
}
