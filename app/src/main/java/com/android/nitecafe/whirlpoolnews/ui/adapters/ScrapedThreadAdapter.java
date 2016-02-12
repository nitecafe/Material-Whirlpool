package com.android.nitecafe.whirlpoolnews.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.nitecafe.whirlpoolnews.R;
import com.android.nitecafe.whirlpoolnews.models.ScrapedThread;
import com.android.nitecafe.whirlpoolnews.ui.interfaces.IRecycleViewItemClick;
import com.android.nitecafe.whirlpoolnews.ui.interfaces.RecyclerViewAdapterClickListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ScrapedThreadAdapter extends RecyclerView.Adapter<ScrapedThreadAdapter.ScrapedThreadViewHolder> implements RecyclerViewAdapterClickListener {

    private List<ScrapedThread> mThreads = new ArrayList<>();
    private IRecycleViewItemClick itemClickHandler;

    public ScrapedThreadAdapter(IRecycleViewItemClick itemClickHandler) {
        this.itemClickHandler = itemClickHandler;
    }

    public void SetThreads(List<ScrapedThread> threads) {
        mThreads = threads;
        notifyDataSetChanged();
    }

    @Override
    public ScrapedThreadViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_thread, parent, false);
        return new ScrapedThreadViewHolder(inflate, this);
    }

    @Override
    public void onBindViewHolder(ScrapedThreadViewHolder holder, int position) {
        final ScrapedThread forumThread = mThreads.get(position);
        holder.threadTitle.setText(Html.fromHtml(forumThread.getTitle()));
        holder.threadTotalPage.setText(String.valueOf(forumThread.getPageCount()));

        if (forumThread.isMoved())
            holder.threadLastPostInfo.setText("This thread has been moved");
        else if (forumThread.isDeleted())
            holder.threadLastPostInfo.setText("This thread has been deleted");
        else if (forumThread.isClosed())
            holder.threadLastPostInfo.setText("This thread has been closed");
        else
            holder.threadLastPostInfo.setText(forumThread.getLast_poster());

        if (forumThread.isSticky())
            holder.itemView.setBackgroundResource(R.color.primary_light);
        else
            holder.itemView.setBackgroundResource(R.color.white);
    }

    @Override
    public int getItemCount() {
        return mThreads.size();
    }

    @Override
    public void recyclerViewListClicked(View v, int position) {
        final ScrapedThread forumThread = mThreads.get(position);
        itemClickHandler.OnItemClicked(forumThread.getId(), forumThread.getTitle());
    }

    public static class ScrapedThreadViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public View itemView;
        @Bind(R.id.thread_title) TextView threadTitle;
        @Bind(R.id.thread_total_page) TextView threadTotalPage;
        @Bind(R.id.thread_last_post_info) TextView threadLastPostInfo;
        private RecyclerViewAdapterClickListener mListener;

        ScrapedThreadViewHolder(View itemView, RecyclerViewAdapterClickListener listener) {
            super(itemView);
            this.itemView = itemView;
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
