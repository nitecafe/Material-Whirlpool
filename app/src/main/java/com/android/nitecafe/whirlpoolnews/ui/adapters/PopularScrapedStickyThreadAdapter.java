package com.android.nitecafe.whirlpoolnews.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.nitecafe.whirlpoolnews.R;
import com.android.nitecafe.whirlpoolnews.utilities.IStickyHeaderUtil;
import com.android.nitecafe.whirlpoolnews.utilities.IWatchedThreadIdentifier;

public class PopularScrapedStickyThreadAdapter extends ScrapedThreadAdapter {

    private IStickyHeaderUtil mStickyHeaderUtil;

    public PopularScrapedStickyThreadAdapter(IWatchedThreadIdentifier watchedThreadIdentifier, IStickyHeaderUtil stickyHeaderUtil) {
        super(watchedThreadIdentifier);
        mStickyHeaderUtil = stickyHeaderUtil;
    }

    @Override
    protected void resetHeader() {
        mStickyHeaderUtil.resetHeader();
    }

    @Override
    public long generateHeaderId(int position) {
        String section = threadsList.get(position).getForum();
        return mStickyHeaderUtil.generateHeaderId(section);
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
        textView.setText(threadsList.get(position).getForum());
    }
}
