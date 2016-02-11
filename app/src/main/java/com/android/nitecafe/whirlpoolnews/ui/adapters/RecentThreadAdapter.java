package com.android.nitecafe.whirlpoolnews.ui.adapters;

import android.support.annotation.NonNull;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;

import com.android.nitecafe.whirlpoolnews.models.Recent;
import com.jakewharton.rxbinding.view.RxMenuItem;

import rx.Observable;
import rx.subjects.PublishSubject;

public class RecentThreadAdapter extends ThreadStickyHeaderAdapter<Recent> {

    private PublishSubject<Integer> OnUnwatchClickedObservable = PublishSubject.create();

    public Observable<Recent> getOnUnwatchedObservable() {
        return OnUnwatchClickedObservable.map(integer -> threadsList.get(integer)).asObservable();
    }

    @NonNull
    @Override
    protected ThreadViewHolder getThreadViewHolderCustom(View inflate) {
        return new RecentThreadViewHolder(inflate, OnThreadClickedObservable, OnUnwatchClickedObservable);
    }

    public static class RecentThreadViewHolder extends ThreadViewHolder {

        private PublishSubject<Integer> mOnUnwatchClickedObservable;

        RecentThreadViewHolder(View itemView, PublishSubject<Integer> onThreadClickedObservable, PublishSubject<Integer> onUnwatchClickedObservable) {
            super(itemView, onThreadClickedObservable);
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
