package com.android.nitecafe.whirlpoolnews.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.nitecafe.whirlpoolnews.R;
import com.android.nitecafe.whirlpoolnews.constants.StringConstants;
import com.android.nitecafe.whirlpoolnews.models.Recent;
import com.android.nitecafe.whirlpoolnews.ui.interfaces.IRecycleViewItemClick;
import com.marshalchen.ultimaterecyclerview.UltimateViewAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

public class RecentStickyHeaderAdapter extends UltimateViewAdapter<RecentStickyHeaderAdapter.RecentViewHolder> implements View.OnClickListener {

    private List<Recent> recentList = new ArrayList<>();
    private Map<String, Integer> headerMap = new HashMap<>();
    private int headerId = 0;
    private IRecycleViewItemClick itemClickHandler;

    public RecentStickyHeaderAdapter(IRecycleViewItemClick itemClickHandler) {
        this.itemClickHandler = itemClickHandler;
    }

    public void setRecent(List<Recent> recent) {
        this.recentList = recent;
        headerMap.clear();
        headerId = 0;
        notifyDataSetChanged();

    }

    @Override public RecentViewHolder getViewHolder(View view) {
        return null;
    }

    @Override public void onClick(View v) {
//        itemClickHandler.OnItemClicked(v.getTag().toString());
    }

    @Override public RecentViewHolder onCreateViewHolder(ViewGroup parent) {
        final View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_thread, parent, false);
        inflate.setOnClickListener(this);
        return new RecentViewHolder(inflate);
    }

    @Override public int getAdapterItemCount() {
        return recentList.size();
    }

    @Override public long generateHeaderId(int position) {
        String section = recentList.get(position).getFORUMNAME();
        if (headerMap.containsKey(section))
            return headerMap.get(section);
        else {
            headerMap.put(section, ++headerId);
            return headerId;
        }
    }

    @Override public void onBindViewHolder(RecentViewHolder holder, int position) {
        Recent recentItem = recentList.get(position);

        holder.threadTitle.setText(recentItem.getTITLE());
        holder.threadTotalPage.setText(recentItem.getREPLIES().toString());

//        long datePosted = Long.valueOf(recentItem.getLASTDATE());
//        CharSequence relativeTimeSpanString = DateUtils.getRelativeTimeSpanString(datePosted);
//     Ad   holder.threadLastPostInfo.setText(String.format("%s by %s", relativeTimeSpanString, recentItem.getLAST().getNAME()));
    }

    @Override public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_header_item_forum, parent, false);
        return new RecyclerView.ViewHolder(view) {
        };
    }

    @Override public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {
        TextView textView = (TextView) holder.itemView;
        textView.setText(recentList.get(position).getFORUMNAME());
        holder.itemView.setTag(recentList.get(position).getID());
    }

    public static class RecentViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.thread_title) TextView threadTitle;
        @Bind(R.id.thread_total_page) TextView threadTotalPage;
        @Bind(R.id.thread_last_post_info) TextView threadLastPostInfo;
        public View itemView;

        RecentViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            ButterKnife.bind(this, itemView);
        }
    }
}
