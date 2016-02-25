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
import com.android.nitecafe.whirlpoolnews.models.Forum;
import com.android.nitecafe.whirlpoolnews.ui.interfaces.IRecycleViewItemClick;
import com.android.nitecafe.whirlpoolnews.ui.interfaces.RecyclerViewAdapterClickListener;
import com.android.nitecafe.whirlpoolnews.utilities.interfaces.IFavouriteThreadService;
import com.jakewharton.rxbinding.view.RxMenuItem;
import com.marshalchen.ultimaterecyclerview.UltimateViewAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.subjects.PublishSubject;

public class ForumStickyHeaderAdapter extends UltimateViewAdapter<ForumStickyHeaderAdapter.ForumViewHolder> implements RecyclerViewAdapterClickListener {

    private List<Forum> forums = new ArrayList<>();
    private Map<String, Integer> headerMap = new HashMap<>();
    private int headerId = 0;
    private IRecycleViewItemClick itemClickHandler;
    private IFavouriteThreadService favouriteThreadService;
    private PublishSubject<Forum> OnAddToFavClickedObservable = PublishSubject.create();
    private PublishSubject<Forum> OnRemoveFromFavClickedObservable = PublishSubject.create();

    public ForumStickyHeaderAdapter(IRecycleViewItemClick itemClickHandler, IFavouriteThreadService favouriteThreadService) {
        this.itemClickHandler = itemClickHandler;
        this.favouriteThreadService = favouriteThreadService;
    }

    public PublishSubject<Forum> getOnAddToFavClickedObservable() {
        return OnAddToFavClickedObservable;
    }

    public PublishSubject<Forum> getOnRemoveFromFavClickedObservable() {
        return OnRemoveFromFavClickedObservable;
    }

    public void setForum(List<Forum> forums) {
        this.forums = forums;
        headerMap.clear();
        headerId = 0;
        notifyDataSetChanged();
    }

    @Override
    public ForumViewHolder getViewHolder(View view) {
        return null;
    }

    @Override
    public ForumViewHolder onCreateViewHolder(ViewGroup parent) {
        final View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_forum, parent, false);
        return new ForumViewHolder(inflate, this);
    }

    @Override
    public int getAdapterItemCount() {
        return forums.size();
    }

    @Override
    public long generateHeaderId(int position) {
        String section = forums.get(position).getSECTION();
        if (headerMap.containsKey(section))
            return headerMap.get(section);
        else {
            headerMap.put(section, ++headerId);
            return headerId;
        }
    }

    @Override
    public void onBindViewHolder(ForumViewHolder holder, int position) {
        holder.forumTitle.setText(Html.fromHtml(forums.get(position).getTITLE()));
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
        textView.setText(forums.get(position).getSECTION());
    }

    @Override
    public void recyclerViewListClicked(View v, int position) {
        final Forum forum = forums.get(position);
        itemClickHandler.OnItemClicked(forum.getID(), forum.getTITLE());
    }


    public class ForumViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener {
        public View itemView;
        @Bind(R.id.forum_title) TextView forumTitle;
        private RecyclerViewAdapterClickListener mListener;

        ForumViewHolder(View itemView, RecyclerViewAdapterClickListener listener) {
            super(itemView);
            this.itemView = itemView;
            itemView.setOnCreateContextMenuListener(this);
            mListener = listener;
            itemView.setOnClickListener(this);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onClick(View v) {
            mListener.recyclerViewListClicked(v, getAdapterPosition());
        }

        @Override public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle(R.string.context_menu_title);
            final Forum forum = forums.get(getAdapterPosition());

            if (favouriteThreadService.isAFavouriteThread(forum.getID())) {
                MenuItem removeFav = menu.add(R.string.context_menu_thread_remove_favourite);
                RxMenuItem.clicks(removeFav).map(aVoid -> forum)
                        .doOnNext(forum1 -> WhirlpoolApp.getInstance().trackEvent("Forum Context Menu", "Add to Favourite", ""))
                        .subscribe(OnRemoveFromFavClickedObservable);
            } else {
                MenuItem addFav = menu.add(R.string.context_menu_thread_add_favourite);
                RxMenuItem.clicks(addFav).map(aVoid -> forum)
                        .doOnNext(forum1 -> WhirlpoolApp.getInstance().trackEvent("Forum Context Menu", "Remove from Favourite", ""))
                        .subscribe(OnAddToFavClickedObservable);
            }
        }
    }
}
