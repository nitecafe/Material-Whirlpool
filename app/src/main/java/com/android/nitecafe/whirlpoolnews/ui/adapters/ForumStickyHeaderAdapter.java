package com.android.nitecafe.whirlpoolnews.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.nitecafe.whirlpoolnews.R;
import com.android.nitecafe.whirlpoolnews.models.Forum;
import com.android.nitecafe.whirlpoolnews.ui.interfaces.IRecycleViewItemClick;
import com.android.nitecafe.whirlpoolnews.ui.interfaces.RecyclerViewAdapterClickListener;
import com.marshalchen.ultimaterecyclerview.UltimateViewAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ForumStickyHeaderAdapter extends UltimateViewAdapter<ForumStickyHeaderAdapter.ForumViewHolder> implements RecyclerViewAdapterClickListener {

    private List<Forum> forums = new ArrayList<>();
    private Map<String, Integer> headerMap = new HashMap<>();
    private int headerId = 0;
    private IRecycleViewItemClick itemClickHandler;

    public ForumStickyHeaderAdapter(IRecycleViewItemClick itemClickHandler) {
        this.itemClickHandler = itemClickHandler;
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

    public static class ForumViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @Bind(R.id.forum_title) TextView forumTitle;
        public View itemView;
        private RecyclerViewAdapterClickListener mListener;

        ForumViewHolder(View itemView, RecyclerViewAdapterClickListener listener) {
            super(itemView);
            this.itemView = itemView;
            mListener = listener;
            itemView.setOnClickListener(this);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onClick(View v) {
            mListener.recyclerViewListClicked(v, getAdapterPosition());
        }
    }
}
