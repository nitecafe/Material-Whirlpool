package com.android.nitecafe.whirlpoolnews.ui.adapters;

import android.support.annotation.NonNull;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;

import com.android.nitecafe.whirlpoolnews.models.Watched;
import com.android.nitecafe.whirlpoolnews.ui.interfaces.IRecycleViewItemClick;
import com.android.nitecafe.whirlpoolnews.ui.interfaces.RecyclerViewAdapterClickListener;
import com.jakewharton.rxbinding.view.RxMenuItem;

import rx.Observable;
import rx.subjects.PublishSubject;

public class WatchedThreadAdapter extends ThreadStickyHeaderAdapter<Watched> {

    public Observable<Watched> getOnWatchClickedObservable() {
        return OnWatchClickedObservable.map(threadsList::get).asObservable();
    }

    public Observable<Watched> getOnMarkAsClickedObservable() {
        return OnMarkAsReadClickedObservable.map(threadsList::get).asObservable();
    }

    private PublishSubject<Integer> OnWatchClickedObservable = PublishSubject.create();
    private PublishSubject<Integer> OnMarkAsReadClickedObservable = PublishSubject.create();

    public WatchedThreadAdapter(IRecycleViewItemClick itemClickHandler) {
        super(itemClickHandler);
    }

    protected String getThreadTitleText(Watched thread) {
        return thread.getTITLE() + " (" + thread.getUNREAD() + " unread)";
    }

    @NonNull
    @Override
    protected ThreadViewHolder getThreadViewHolderCustom(View inflate) {
        return new WatchedThreadViewHolder(inflate, this, OnWatchClickedObservable,OnMarkAsReadClickedObservable);
    }

    public static class WatchedThreadViewHolder extends ThreadViewHolder {

        private PublishSubject<Integer> mWatchedClickedSubject;
        private PublishSubject<Integer> mOnMarkAsReadClickedObservable;

        WatchedThreadViewHolder(View itemView, RecyclerViewAdapterClickListener tThreadStickyHeaderAdapter,
                                PublishSubject<Integer> watchedClickedSubject, PublishSubject<Integer> onMarkAsReadClickedObservable) {
            super(itemView, tThreadStickyHeaderAdapter);
            mWatchedClickedSubject = watchedClickedSubject;
            mOnMarkAsReadClickedObservable = onMarkAsReadClickedObservable;
        }

        @Override
        protected void onCreateContextMenuCustom(ContextMenu menu) {
            super.onCreateContextMenuCustom(menu);
            MenuItem add = menu.add("Unwatch Thread");
            MenuItem markAsRead = menu.add("Mark as Read");
            RxMenuItem.clicks(add).map(aVoid -> getAdapterPosition()).subscribe(mWatchedClickedSubject);
            RxMenuItem.clicks(markAsRead).map(aVoid -> getAdapterPosition()).subscribe(mOnMarkAsReadClickedObservable);
        }
    }
}
