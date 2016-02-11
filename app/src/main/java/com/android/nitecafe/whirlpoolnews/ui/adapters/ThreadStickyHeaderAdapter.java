package com.android.nitecafe.whirlpoolnews.ui.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.nitecafe.whirlpoolnews.R;
import com.android.nitecafe.whirlpoolnews.constants.StringConstants;
import com.android.nitecafe.whirlpoolnews.models.IWhirlpoolThread;
import com.android.nitecafe.whirlpoolnews.utilities.WhirlpoolDateUtils;
import com.jakewharton.rxbinding.view.RxView;
import com.marshalchen.ultimaterecyclerview.UltimateViewAdapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.subjects.PublishSubject;

public class ThreadStickyHeaderAdapter<T extends IWhirlpoolThread> extends UltimateViewAdapter<ThreadStickyHeaderAdapter.ThreadViewHolder> {

    protected List<T> threadsList = new ArrayList<>();
    protected PublishSubject<Integer> OnThreadClickedObservable = PublishSubject.create();
    private Map<String, Integer> headerMap = new HashMap<>();
    private int headerId = 0;

    public Observable<T> getOnThreadClickedObservable() {
        return OnThreadClickedObservable.map(
                integer -> threadsList.get(integer)).asObservable();
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
        return new ThreadViewHolder(inflate, OnThreadClickedObservable);
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
        return Html.fromHtml(thread.getTITLE()).toString();
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

    public static class ThreadViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        public View itemView;
        @Bind(R.id.thread_title) TextView threadTitle;
        @Bind(R.id.thread_total_page) TextView threadTotalPage;
        @Bind(R.id.thread_last_post_info) TextView threadLastPostInfo;

        ThreadViewHolder(View itemView, PublishSubject<Integer> onThreadClickedObservable) {
            super(itemView);
            this.itemView = itemView;
            itemView.setOnCreateContextMenuListener(this);
            RxView.clicks(itemView).map(aVoid -> getAdapterPosition()).subscribe(onThreadClickedObservable);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            onCreateContextMenuCustom(menu);
        }

        protected void onCreateContextMenuCustom(ContextMenu menu) {
            menu.setHeaderTitle("Select an Action");
        }
    }
}
