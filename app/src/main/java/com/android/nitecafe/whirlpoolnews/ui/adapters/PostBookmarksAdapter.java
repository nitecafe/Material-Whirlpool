package com.android.nitecafe.whirlpoolnews.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.nitecafe.whirlpoolnews.R;
import com.android.nitecafe.whirlpoolnews.WhirlpoolApp;
import com.android.nitecafe.whirlpoolnews.models.PostBookmark;
import com.jakewharton.rxbinding.view.RxMenuItem;
import com.jakewharton.rxbinding.view.RxView;
import com.marshalchen.ultimaterecyclerview.UltimateViewAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.subjects.PublishSubject;

public class PostBookmarksAdapter extends UltimateViewAdapter<PostBookmarksAdapter.PostBookmarkViewHolder> {

    public PublishSubject<Integer> OnRemoveFromBookmarkClickedSubject = PublishSubject.create();
    public PublishSubject<PostBookmark> OnBookmarkClickedSubject = PublishSubject.create();
    private List<PostBookmark> postBookmarks = new ArrayList<>();

    @Override public PostBookmarkViewHolder getViewHolder(View view) {
        return null;
    }

    @Override public PostBookmarkViewHolder onCreateViewHolder(ViewGroup parent) {
        final View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post_bookmark, parent, false);
        return new PostBookmarkViewHolder(inflate);
    }

    @Override public int getAdapterItemCount() {
        return postBookmarks.size();
    }

    @Override public long generateHeaderId(int position) {
        return 0;
    }

    @Override public void onBindViewHolder(PostBookmarkViewHolder holder, int position) {
        PostBookmark postBookmark = postBookmarks.get(position);
        holder.bookMarkName.setText(postBookmark.getBookMarkName());
        holder.bookMarkThreadName.setText(Html.fromHtml(postBookmark.getThreadTitle()));
        holder.bookMarkPageNumber.setText(String.format("Page %s of %s",
                String.valueOf(postBookmark.getPageLocated()), String.valueOf(postBookmark.getTotalPage())));
    }

    @Override public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        return null;
    }

    @Override public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    public void setPostBookmarks(List<PostBookmark> postBookmarks) {
        this.postBookmarks = postBookmarks;
        notifyDataSetChanged();
    }


    public class PostBookmarkViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        @Bind(R.id.post_bookmark_name) TextView bookMarkName;
        @Bind(R.id.post_bookmark_thread_title) TextView bookMarkThreadName;
        @Bind(R.id.post_bookmark_page_number) TextView bookMarkPageNumber;

        PostBookmarkViewHolder(View itemView) {
            super(itemView);
            itemView.setOnCreateContextMenuListener(this);
            RxView.clicks(itemView).map(aVoid -> postBookmarks.get(getAdapterPosition()))
                    .onErrorResumeNext(Observable.empty())
                    .doOnNext(postBookmark -> WhirlpoolApp.getInstance().trackEvent("RecycleView Click", "Open Post bookmark", ""))
                    .subscribe(OnBookmarkClickedSubject);
            ButterKnife.bind(this, itemView);
        }

        @Override public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("Select an Action");
            MenuItem removeBookmark = menu.add("Remove from Bookmarks");
            RxMenuItem.clicks(removeBookmark).map(aVoid -> postBookmarks.get(getAdapterPosition()).getPostId())
                    .doOnNext(integer -> WhirlpoolApp.getInstance().trackEvent("Post Bookmarks Context Menu", "Remove from Bookmark", ""))
                    .subscribe(OnRemoveFromBookmarkClickedSubject);
        }
    }
}
