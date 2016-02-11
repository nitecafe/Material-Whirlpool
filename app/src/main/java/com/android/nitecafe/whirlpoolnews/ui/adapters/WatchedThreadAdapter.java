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
        return OnWatchClickedObservable.map(integer -> threadsList.get(integer)).asObservable();
    }

    private PublishSubject<Integer> OnWatchClickedObservable = PublishSubject.create();

    public WatchedThreadAdapter(IRecycleViewItemClick itemClickHandler) {
        super(itemClickHandler);
    }

    protected String getThreadTitleText(Watched thread) {
        return thread.getTITLE() + " (" + thread.getUNREAD() + " unread)";
    }

    @NonNull
    @Override
    protected ThreadViewHolder getThreadViewHolderCustom(View inflate) {
        return new WatchedThreadViewHolder(inflate, this, OnWatchClickedObservable);
    }

    public static class WatchedThreadViewHolder extends ThreadViewHolder {

        private PublishSubject<Integer> mWatchedClickedSubject;

        WatchedThreadViewHolder(View itemView, RecyclerViewAdapterClickListener tThreadStickyHeaderAdapter, PublishSubject<Integer> watchedClickedSubject) {
            super(itemView, tThreadStickyHeaderAdapter);
            mWatchedClickedSubject = watchedClickedSubject;
        }

        @Override
        protected void onCreateContextMenuCustom(ContextMenu menu) {
            super.onCreateContextMenuCustom(menu);
            MenuItem add = menu.add("Unwatch Thread");
            RxMenuItem.clicks(add).map(aVoid -> getAdapterPosition()).subscribe(mWatchedClickedSubject);
        }
    }
}
