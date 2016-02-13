package com.android.nitecafe.whirlpoolnews.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;

import com.android.nitecafe.whirlpoolnews.constants.StringConstants;
import com.android.nitecafe.whirlpoolnews.models.IWhirlpoolThread;
import com.android.nitecafe.whirlpoolnews.utilities.IWatchedThreadIdentifier;
import com.android.nitecafe.whirlpoolnews.utilities.WhirlpoolDateUtils;

import java.util.Date;

public class ForumThreadAdapter<T extends IWhirlpoolThread> extends ThreadBaseAdapter<T> {

    public ForumThreadAdapter(IWatchedThreadIdentifier watchedThreadIdentifier) {
        super(watchedThreadIdentifier);
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
    public void onBindViewHolder(ForumThreadAdapter.ThreadViewHolder holder, int position) {
        final T forumThread = threadsList.get(position);
        holder.threadTitle.setText(Html.fromHtml(forumThread.getTITLE()));
        final int pages = getNumberOfPage(forumThread);
        holder.threadTotalPage.setText(String.valueOf(pages));

        final Date localDateFromString = WhirlpoolDateUtils.getLocalDateFromString(forumThread.getLASTDATE());
        holder.threadLastPostInfo.setText(String.format("%s ago by %s",
                WhirlpoolDateUtils.getTimeSince(localDateFromString), forumThread.getLAST().getNAME()));
    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        return null;
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    private int getNumberOfPage(T thread) {
        return (int) Math.ceil((double) thread.getREPLIES() / StringConstants.POST_PER_PAGE);
    }
}
