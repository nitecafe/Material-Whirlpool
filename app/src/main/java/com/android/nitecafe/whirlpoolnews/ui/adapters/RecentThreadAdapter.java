package com.android.nitecafe.whirlpoolnews.ui.adapters;

import android.support.annotation.NonNull;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;

import com.android.nitecafe.whirlpoolnews.models.Recent;
import com.android.nitecafe.whirlpoolnews.ui.interfaces.IRecycleViewItemClick;
import com.android.nitecafe.whirlpoolnews.ui.interfaces.RecyclerViewAdapterClickListener;
import com.jakewharton.rxbinding.view.RxMenuItem;

import rx.Observable;
import rx.subjects.PublishSubject;

public class RecentThreadAdapter extends ThreadStickyHeaderAdapter<Recent> {
    public RecentThreadAdapter(IRecycleViewItemClick itemClickHandler) {
        super(itemClickHandler);
    }

    public Observable<Recent> getOnUnwatchedObservable() {
        return OnUnwatchClickedObservable.map(integer -> threadsList.get(integer)).asObservable();
    }

    private PublishSubject<Integer> OnUnwatchClickedObservable = PublishSubject.create();

    @NonNull
    @Override
    protected ThreadViewHolder getThreadViewHolderCustom(View inflate) {
        return new RecentThreadViewHolder(inflate, this, OnUnwatchClickedObservable);
    }

    public static class RecentThreadViewHolder extends ThreadViewHolder {

        private PublishSubject<Integer> mOnUnwatchClickedObservable;

        RecentThreadViewHolder(View itemView, RecyclerViewAdapterClickListener tThreadStickyHeaderAdapter, PublishSubject<Integer> onUnwatchClickedObservable) {
            super(itemView, tThreadStickyHeaderAdapter);
            mOnUnwatchClickedObservable = onUnwatchClickedObservable;
        }

        @Override
        protected void onCreateContextMenuCustom(ContextMenu menu) {
            super.onCreateContextMenuCustom(menu);
            MenuItem add = menu.add("Watch Thread");
            RxMenuItem.clicks(add).map(aVoid -> getAdapterPosition()).subscribe(mOnUnwatchClickedObservable);
        }
    }
}
