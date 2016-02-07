package com.android.nitecafe.whirlpoolnews.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.nitecafe.whirlpoolnews.R;
import com.android.nitecafe.whirlpoolnews.constants.StringConstants;
import com.android.nitecafe.whirlpoolnews.models.ForumThread;
import com.android.nitecafe.whirlpoolnews.ui.interfaces.IRecycleViewItemClick;
import com.android.nitecafe.whirlpoolnews.ui.interfaces.RecyclerViewAdapterClickListener;
import com.android.nitecafe.whirlpoolnews.utilities.WhirlpoolDateUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ForumThreadAdapter extends RecyclerView.Adapter<ForumThreadAdapter.ForumThreadViewHolder> implements RecyclerViewAdapterClickListener {

    private List<ForumThread> mThreads = new ArrayList<>();
    private IRecycleViewItemClick itemClickHandler;

    public ForumThreadAdapter(IRecycleViewItemClick itemClickHandler) {
        this.itemClickHandler = itemClickHandler;
    }

    public void SetThreads(List<ForumThread> threads) {
        mThreads = threads;
        notifyDataSetChanged();
    }

    @Override
    public ForumThreadViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_thread, parent, false);
        return new ForumThreadViewHolder(inflate, this);
    }

    @Override
    public void onBindViewHolder(ForumThreadViewHolder holder, int position) {
        final ForumThread forumThread = mThreads.get(position);
        holder.threadTitle.setText(forumThread.getTITLE());
        final int pages = getNumberOfPage(forumThread);
        holder.threadTotalPage.setText(String.valueOf(pages));

        final Date localDateFromString = WhirlpoolDateUtils.getLocalDateFromString(forumThread.getLASTDATE());
        holder.threadLastPostInfo.setText(String.format("%s ago by %s",
                WhirlpoolDateUtils.getTimeSince(localDateFromString), forumThread.getLAST().getNAME()));

    }

    private int getNumberOfPage(ForumThread thread) {
        return (int) Math.ceil((double) thread.getREPLIES() / StringConstants.POST_PER_PAGE);
    }

    @Override
    public int getItemCount() {
        return mThreads.size();
    }

    @Override
    public void recyclerViewListClicked(View v, int position) {
        final ForumThread forumThread = mThreads.get(position);
        itemClickHandler.OnItemClicked(forumThread.getID(), forumThread.getTITLE());
    }

    public static class ForumThreadViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @Bind(R.id.thread_title) TextView threadTitle;
        @Bind(R.id.thread_total_page) TextView threadTotalPage;
        @Bind(R.id.thread_last_post_info) TextView threadLastPostInfo;
        public View itemView;
        private RecyclerViewAdapterClickListener mListener;

        ForumThreadViewHolder(View itemView, RecyclerViewAdapterClickListener listener) {
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
