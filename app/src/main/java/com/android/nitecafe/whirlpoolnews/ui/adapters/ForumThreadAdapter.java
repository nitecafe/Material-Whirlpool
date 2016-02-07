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
import com.android.nitecafe.whirlpoolnews.utilities.WhirlpoolDateUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ForumThreadAdapter extends RecyclerView.Adapter<ForumThreadAdapter.ForumThreadViewHolder> implements View.OnClickListener {

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
        inflate.setOnClickListener(this);
        return new ForumThreadViewHolder(inflate);
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

        holder.itemView.setTag(forumThread.getID());
        holder.itemView.setTag(StringConstants.TAG_TITLE_KEY, forumThread.getTITLE());
    }

    private int getNumberOfPage(ForumThread thread) {
        return (int) Math.ceil((double) thread.getREPLIES() / StringConstants.POST_PER_PAGE);
    }

    @Override
    public int getItemCount() {
        return mThreads.size();
    }

    @Override
    public void onClick(View view) {
        itemClickHandler.OnItemClicked(view.getTag().toString(), view.getTag(1).toString());
    }

    public static class ForumThreadViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.thread_title) TextView threadTitle;
        @Bind(R.id.thread_total_page) TextView threadTotalPage;
        @Bind(R.id.thread_last_post_info) TextView threadLastPostInfo;
        public View itemView;

        ForumThreadViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            ButterKnife.bind(this, itemView);
        }
    }

}
