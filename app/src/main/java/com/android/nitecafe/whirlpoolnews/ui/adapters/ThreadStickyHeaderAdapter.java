package com.android.nitecafe.whirlpoolnews.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.nitecafe.whirlpoolnews.R;
import com.android.nitecafe.whirlpoolnews.constants.StringConstants;
import com.android.nitecafe.whirlpoolnews.models.IWhirlpoolThread;
import com.android.nitecafe.whirlpoolnews.utilities.IStickyHeaderUtil;
import com.android.nitecafe.whirlpoolnews.utilities.IWatchedThreadIdentifier;
import com.android.nitecafe.whirlpoolnews.utilities.WhirlpoolDateUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ThreadStickyHeaderAdapter<T extends IWhirlpoolThread> extends ForumThreadAdapter<T> {

    private Map<String, Integer> headerMap = new HashMap<>();
    private int headerId = 0;
    private IStickyHeaderUtil mStickyHeaderUtil;

    public ThreadStickyHeaderAdapter(IWatchedThreadIdentifier watchedThreadIdentifier, IStickyHeaderUtil stickyHeaderUtil) {
        super(watchedThreadIdentifier);
        mStickyHeaderUtil = stickyHeaderUtil;
    }

    @Override
    protected void resetHeader() {
        mStickyHeaderUtil.resetHeader();
    }

    @Override
    public long generateHeaderId(int position) {
        String section = threadsList.get(position).getFORUMNAME();
        return mStickyHeaderUtil.generateHeaderId(section);
    }

    @Override
    public void onBindViewHolder(ThreadStickyHeaderAdapter.ThreadViewHolder holder, int position) {

        T thread = threadsList.get(position);
        holder.threadTitle.setText(Html.fromHtml(getThreadTitleText(thread)));
        final int pages = getNumberOfPage(thread);
        holder.threadTotalPage.setText(String.valueOf(pages));

        final Date localDateFromString = WhirlpoolDateUtils.getLocalDateFromString(thread.getLASTDATE());
        holder.threadLastPostInfo.setText(String.format("%s ago by %s",
                WhirlpoolDateUtils.getTimeSince(localDateFromString), thread.getLAST().getNAME()));
    }

    /**
     * A seam to allow modification of the title shows on the UI
     */
    protected String getThreadTitleText(T thread) {
        return thread.getTITLE();
    }

    private int getNumberOfPage(T thread) {
        return (int) Math.ceil((double) thread.getREPLIES() / StringConstants.POST_PER_PAGE);
    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_header_item_forum, parent, false);
        return new RecyclerView.ViewHolder(view) {
        };
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {
        TextView textView = (TextView) holder.itemView;
        textView.setText(threadsList.get(position).getFORUMNAME());
    }
}
