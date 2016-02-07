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

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> implements RecyclerViewAdapterClickListener {

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
        return new NewsViewHolder(inflate, this);
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
    public void recyclerViewListClicked(View v, int position) {
        final News news = mNews.get(position);
        itemClickHandler.OnItemClicked(news.getID(), news.getTITLE());
    }

    public static class NewsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @Bind(R.id.news_title) TextView newsTitle;
        @Bind(R.id.news_blurb) TextView newsBlurb;
        public View mItemView;
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
