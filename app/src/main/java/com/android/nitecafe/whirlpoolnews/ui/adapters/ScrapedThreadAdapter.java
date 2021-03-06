package com.android.nitecafe.whirlpoolnews.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;

import com.android.nitecafe.whirlpoolnews.R;
import com.android.nitecafe.whirlpoolnews.models.ScrapedThread;
import com.android.nitecafe.whirlpoolnews.utilities.interfaces.IPreferencesGetter;
import com.android.nitecafe.whirlpoolnews.web.interfaces.IWatchedThreadService;

public class ScrapedThreadAdapter extends ThreadBaseAdapter<ScrapedThread> {

    private IPreferencesGetter preferencesGetter;

    public ScrapedThreadAdapter(IWatchedThreadService watchedThreadIdentifier, IPreferencesGetter preferencesGetter) {
        super(watchedThreadIdentifier);
        this.preferencesGetter = preferencesGetter;
    }

    @Override
    public void onBindViewHolder(ForumThreadAdapter.ThreadViewHolder holder, int position) {
        final ScrapedThread forumThread = threadsList.get(position);
        holder.threadTitle.setText(Html.fromHtml(forumThread.getTitle()));
        holder.threadTotalPage.setText(String.valueOf(forumThread.getPageCount()));

        if (forumThread.isMoved())
            holder.threadLastPostInfo.setText(R.string.message_thread_moved);
        else if (forumThread.isDeleted())
            holder.threadLastPostInfo.setText(R.string.message_thread_deleted);
        else if (forumThread.isClosed())
            holder.threadLastPostInfo.setText(R.string.message_thread_closed);
        else
            holder.threadLastPostInfo.setText(Html.fromHtml(forumThread.getLast_poster()));

        if (forumThread.isSticky())
            if (preferencesGetter.isDarkThemeOn())
                holder.itemView.setBackgroundResource(R.color.primary_dark);
            else
                holder.itemView.setBackgroundResource(R.color.primary_light);
        else
            holder.itemView.setBackgroundResource(0);
    }

    @Override
    public ThreadViewHolder getViewHolder(View view) {
        return null;
    }

    @Override
    public long generateHeaderId(int position) {
        return 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        return null;
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {

    }
}
