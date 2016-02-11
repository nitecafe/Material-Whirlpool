package com.android.nitecafe.whirlpoolnews.ui.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.nitecafe.whirlpoolnews.R;
import com.android.nitecafe.whirlpoolnews.constants.StringConstants;
import com.android.nitecafe.whirlpoolnews.models.IWhirlpoolThread;
import com.android.nitecafe.whirlpoolnews.ui.interfaces.IRecycleViewItemClick;
import com.android.nitecafe.whirlpoolnews.ui.interfaces.RecyclerViewAdapterClickListener;
import com.android.nitecafe.whirlpoolnews.utilities.WhirlpoolDateUtils;
import com.marshalchen.ultimaterecyclerview.UltimateViewAdapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

    public class ThreadStickyHeaderAdapter<T extends IWhirlpoolThread> extends UltimateViewAdapter<ThreadStickyHeaderAdapter.ThreadViewHolder> implements RecyclerViewAdapterClickListener {

    protected List<T> threadsList = new ArrayList<>();
    private Map<String, Integer> headerMap = new HashMap<>();
    private int headerId = 0;
    private IRecycleViewItemClick itemClickHandler;

    public ThreadStickyHeaderAdapter(IRecycleViewItemClick itemClickHandler) {
        this.itemClickHandler = itemClickHandler;
    }

    public void setThreads(List<T> items) {
        this.threadsList = items;
        headerMap.clear();
        headerId = 0;
        notifyDataSetChanged();
    }

    @Override
    public ThreadViewHolder getViewHolder(View view) {
        return null;
    }

    @Override
    public ThreadViewHolder onCreateViewHolder(ViewGroup parent) {
        final View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_thread, parent, false);
        return getThreadViewHolderCustom(inflate);
    }

    @NonNull
    protected ThreadViewHolder getThreadViewHolderCustom(View inflate) {
        return new ThreadViewHolder(inflate, this);
    }

    @Override
    public int getAdapterItemCount() {
        return threadsList.size();
    }

    @Override
    public long generateHeaderId(int position) {
        String section = threadsList.get(position).getFORUMNAME();
        if (headerMap.containsKey(section))
            return headerMap.get(section);
        else {
            headerMap.put(section, ++headerId);
            return headerId;
        }
    }

    @Override
    public void onBindViewHolder(ThreadViewHolder holder, int position) {
        T thread = threadsList.get(position);

        holder.threadTitle.setText(getThreadTitleText(thread));
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

    @Override
    public void recyclerViewListClicked(View v, int position) {
        final T t = threadsList.get(position);
        itemClickHandler.OnItemClicked(t.getID(), t.getTITLE());
    }

    public static class ThreadViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener {
        @Bind(R.id.thread_title) TextView threadTitle;
        @Bind(R.id.thread_total_page) TextView threadTotalPage;
        @Bind(R.id.thread_last_post_info) TextView threadLastPostInfo;
        public View itemView;
        private RecyclerViewAdapterClickListener listener;

        ThreadViewHolder(View itemView, RecyclerViewAdapterClickListener tThreadStickyHeaderAdapter) {
            super(itemView);
            this.itemView = itemView;
            listener = tThreadStickyHeaderAdapter;
            itemView.setOnCreateContextMenuListener(this);
            itemView.setOnClickListener(this);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onClick(View v) {
            listener.recyclerViewListClicked(v, getAdapterPosition());
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            onCreateContextMenuCustom(menu);
        }

        protected void onCreateContextMenuCustom(ContextMenu menu){
            menu.setHeaderTitle("Select an Action");
        }
    }
}
