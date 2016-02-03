package com.android.nitecafe.whirlpoolnews.ui;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.nitecafe.whirlpoolnews.R;
import com.android.nitecafe.whirlpoolnews.models.Forum;
import com.marshalchen.ultimaterecyclerview.UltimateViewAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

public class StickyHeaderAdapter extends UltimateViewAdapter<StickyHeaderAdapter.ForumViewHolder> {

    private List<Forum> forums = new ArrayList<>();
    private Map<String, Integer> headerMap = new HashMap<>();
    private int headerId = 0;

    public void setForum(List<Forum> forums) {
        this.forums = forums;
        headerMap.clear();
        headerId = 0;
        notifyDataSetChanged();

    }

    @Override public ForumViewHolder getViewHolder(View view) {
        return null;
    }

    @Override public ForumViewHolder onCreateViewHolder(ViewGroup parent) {
        final View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_forum, parent, false);
//        inflate.setOnClickListener(this);
        return new ForumViewHolder(inflate);
    }

    @Override public int getAdapterItemCount() {
        return forums.size();
    }

    @Override public long generateHeaderId(int position) {
        String section = forums.get(position).getSECTION();
        if (headerMap.containsKey(section))
            return headerMap.get(section);
        else {
            headerMap.put(section, ++headerId);
            return headerId;
        }
    }

    @Override public void onBindViewHolder(ForumViewHolder holder, int position) {
        holder.forumTitle.setText(forums.get(position).getTITLE());
    }

    @Override public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_header_item_forum, parent, false);
        return new RecyclerView.ViewHolder(view) {
        };
    }

    @Override public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {
        TextView textView = (TextView) holder.itemView;
        textView.setText(forums.get(position).getSECTION());
    }

    public static class ForumViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.forum_title) TextView forumTitle;

        ForumViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
