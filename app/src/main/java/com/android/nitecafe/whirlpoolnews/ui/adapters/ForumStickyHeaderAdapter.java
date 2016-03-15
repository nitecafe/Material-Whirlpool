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
import com.android.nitecafe.whirlpoolnews.constants.StringConstants;
import com.android.nitecafe.whirlpoolnews.models.Forum;
import com.android.nitecafe.whirlpoolnews.utilities.StickyHeaderUtil;
import com.android.nitecafe.whirlpoolnews.utilities.interfaces.IFavouriteThreadService;
import com.android.nitecafe.whirlpoolnews.utilities.interfaces.IStickyHeaderUtil;
import com.jakewharton.rxbinding.view.RxMenuItem;
import com.jakewharton.rxbinding.view.RxView;
import com.marshalchen.ultimaterecyclerview.UltimateViewAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.subjects.PublishSubject;

public class ForumStickyHeaderAdapter extends UltimateViewAdapter<ForumStickyHeaderAdapter.ForumViewHolder> {

    private List<Forum> forums = new ArrayList<>();
    private IFavouriteThreadService favouriteThreadService;
    private PublishSubject<Forum> OnAddToFavClickedObservable = PublishSubject.create();
    private PublishSubject<Forum> OnRemoveFromFavClickedObservable = PublishSubject.create();
    private PublishSubject<Forum> OnForumClickedObservable = PublishSubject.create();
    private IStickyHeaderUtil headerUtil = new StickyHeaderUtil();

    public ForumStickyHeaderAdapter(IFavouriteThreadService favouriteThreadService) {
        this.favouriteThreadService = favouriteThreadService;
    }

    public PublishSubject<Forum> getOnAddToFavClickedObservable() {
        return OnAddToFavClickedObservable;
    }

    public PublishSubject<Forum> getOnRemoveFromFavClickedObservable() {
        return OnRemoveFromFavClickedObservable;
    }

    public PublishSubject<Forum> getOnForumClickedObservable() {
        return OnForumClickedObservable;
    }

    public void setForum(List<Forum> forums) {
        this.forums = forums;
        headerUtil.resetHeader();
        notifyDataSetChanged();
    }

    @Override
    public ForumViewHolder getViewHolder(View view) {
        return null;
    }

    @Override
    public ForumViewHolder onCreateViewHolder(ViewGroup parent) {
        final View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_forum, parent, false);
        return new ForumViewHolder(inflate);
    }

    @Override
    public int getAdapterItemCount() {
        return forums.size();
    }

    @Override
    public long generateHeaderId(int position) {
        String section = forums.get(position).getSECTION();
        return headerUtil.generateHeaderId(section);
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

    public class ForumViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        @Bind(R.id.forum_title) TextView forumTitle;

        ForumViewHolder(View itemView) {
            super(itemView);
            itemView.setOnCreateContextMenuListener(this);
            ButterKnife.bind(this, itemView);
            RxView.clicks(itemView).map(aVoid1 -> forums.get(getAdapterPosition())).subscribe(OnForumClickedObservable);
        }

        @Override public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle(R.string.context_menu_title);
            final Forum forum = forums.get(getAdapterPosition());

            if (favouriteThreadService.isAFavouriteThread(forum.getID())) {
                MenuItem removeFav = menu.add(R.string.context_menu_thread_remove_favourite);
                RxMenuItem.clicks(removeFav).map(aVoid -> forum)
                        .doOnNext(forum1 -> WhirlpoolApp.getInstance().trackEvent(StringConstants.ANALYTIC_FORUM_CONTEXT_MENU, "Add to Favourite", ""))
                        .subscribe(OnRemoveFromFavClickedObservable);
            } else {
                MenuItem addFav = menu.add(R.string.context_menu_thread_add_favourite);
                RxMenuItem.clicks(addFav).map(aVoid -> forum)
                        .doOnNext(forum1 -> WhirlpoolApp.getInstance().trackEvent(StringConstants.ANALYTIC_FORUM_CONTEXT_MENU, "Remove from Favourite", ""))
                        .subscribe(OnAddToFavClickedObservable);
            }
        }
    }
}
