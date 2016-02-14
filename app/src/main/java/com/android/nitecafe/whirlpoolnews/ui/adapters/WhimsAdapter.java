package com.android.nitecafe.whirlpoolnews.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.nitecafe.whirlpoolnews.R;
import com.android.nitecafe.whirlpoolnews.models.Whim;
import com.android.nitecafe.whirlpoolnews.utilities.WhirlpoolDateUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

public class WhimsAdapter extends RecyclerView.Adapter<WhimsAdapter.WhimViewHolder> {

    private List<Whim> whims = new ArrayList<>();

    public void SetWhims(List<Whim> whims) {
        this.whims = whims;
        notifyDataSetChanged();
    }

    @Override public WhimViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_whims, parent, false);
        return new WhimViewHolder(inflate);
    }

    @Override public void onBindViewHolder(WhimViewHolder holder, int position) {
        Whim whim = whims.get(position);
        holder.whimFrom.setText(whim.getFROM().getNAME());
        final Date localDateFromString = WhirlpoolDateUtils.getLocalDateFromString(whim.getDATE());
        String sentDate = new SimpleDateFormat("yyyy-MM-dd hh:mm aa", Locale.getDefault()).format(localDateFromString);
        holder.whimSentTime.setText(sentDate);
        String message = whim.getMESSAGE();
        String blurb = message.substring(0, Math.min(message.length(), 50));
        holder.whimContent.setText(blurb + "...");

        if (whim.getVIEWED() == 0)
            holder.itemView.setBackgroundResource(R.color.primary_light);
        else
            holder.itemView.setBackgroundResource(R.color.white);
    }

    @Override public int getItemCount() {
        return whims.size();
    }


    public class WhimViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.whim_from) TextView whimFrom;
        @Bind(R.id.whim_sent_time) TextView whimSentTime;
        @Bind(R.id.whim_content) TextView whimContent;

        public WhimViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
