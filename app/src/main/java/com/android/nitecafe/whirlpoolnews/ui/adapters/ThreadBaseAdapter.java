package com.android.nitecafe.whirlpoolnews.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.nitecafe.whirlpoolnews.R;
import com.android.nitecafe.whirlpoolnews.WhirlpoolApp;
import com.android.nitecafe.whirlpoolnews.constants.StringConstants;
import com.android.nitecafe.whirlpoolnews.models.IThreadBase;
import com.android.nitecafe.whirlpoolnews.web.interfaces.IWatchedThreadService;
import com.jakewharton.rxbinding.view.RxMenuItem;
import com.jakewharton.rxbinding.view.RxView;
import com.marshalchen.ultimaterecyclerview.UltimateViewAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.subjects.PublishSubject;

public abstract class ThreadBaseAdapter<T extends IThreadBase> extends UltimateViewAdapter<ThreadBaseAdapter.ThreadViewHolder> {

    public PublishSubject<T> OnThreadClickedObservable = PublishSubject.create();
    public PublishSubject<T> OnWatchClickedObservable = PublishSubject.create();
    public PublishSubject<T> OnMarkAsReadClickedObservable = PublishSubject.create();
    public PublishSubject<T> OnUnwatchClickedObservable = PublishSubject.create();
    public PublishSubject<T> OnGoToLastPageClickedObservable = PublishSubject.create();
    protected List<T> threadsList = new ArrayList<>();
    private IWatchedThreadService mWatchedThreadIdentifier;

    public ThreadBaseAdapter(IWatchedThreadService watchedThreadIdentifier) {
        this.mWatchedThreadIdentifier = watchedThreadIdentifier;
    }

    public void SetThreads(List<T> threads) {
        threadsList = threads;
        notifyDataSetChanged();
        resetHeader();
    }

    protected void resetHeader() {
    }

    @Override
    public ThreadViewHolder onCreateViewHolder(ViewGroup parent) {
        final View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_thread, parent, false);
        return new ThreadViewHolder(inflate);
    }

    @Override
    public int getAdapterItemCount() {
        return threadsList.size();
    }

    public class ThreadViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        @Bind(R.id.thread_title) TextView threadTitle;
        @Bind(R.id.thread_total_page) TextView threadTotalPage;
        @Bind(R.id.thread_last_post_info) TextView threadLastPostInfo;

        ThreadViewHolder(View itemView) {
            super(itemView);
            itemView.setOnCreateContextMenuListener(this);
            RxView.clicks(itemView).map(aVoid -> threadsList.get(getAdapterPosition()))
                    .onErrorResumeNext(Observable.<T>empty()) //ignore when getAdapterPosition is -1 http://stackoverflow.com/questions/29684154/recyclerview-viewholder-getlayoutposition-vs-getadapterposition
                    .subscribe(OnThreadClickedObservable);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

            menu.setHeaderTitle(R.string.context_menu_title);
            final T t = threadsList.get(getAdapterPosition());
            if (mWatchedThreadIdentifier.isThreadWatched(t.getID())) {
                MenuItem unwatch = menu.add(R.string.context_menu_unwatch_thread);
                MenuItem markAsRead = menu.add(R.string.context_menu_mark_read);
                RxMenuItem.clicks(unwatch).map(aVoid -> t)
                        .doOnNext(integer -> WhirlpoolApp.getInstance().trackEvent(StringConstants.ANALYTIC_THREAD_CONTEXT_MENU, "Unwatch Thread", ""))
                        .subscribe(OnUnwatchClickedObservable);
                RxMenuItem.clicks(markAsRead).map(aVoid -> t)
                        .doOnNext(integer -> WhirlpoolApp.getInstance().trackEvent(StringConstants.ANALYTIC_THREAD_CONTEXT_MENU, "Mark as Read", ""))
                        .subscribe(OnMarkAsReadClickedObservable);
            } else {
                MenuItem watch = menu.add(R.string.context_menu_watch_thread);
                RxMenuItem.clicks(watch).map(aVoid -> t)
                        .doOnNext(integer -> WhirlpoolApp.getInstance().trackEvent(StringConstants.ANALYTIC_THREAD_CONTEXT_MENU, "Watch Thread", ""))
                        .subscribe(OnWatchClickedObservable);
            }

            MenuItem lastPage = menu.add(R.string.context_menu_go_last_page);
            RxMenuItem.clicks(lastPage).map(aVoid -> t)
                    .doOnNext(integer -> WhirlpoolApp.getInstance().trackEvent(StringConstants.ANALYTIC_THREAD_CONTEXT_MENU, "Go To Last Page", ""))
                    .subscribe(OnGoToLastPageClickedObservable);
        }
    }
}
