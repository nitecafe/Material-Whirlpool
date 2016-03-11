package com.android.nitecafe.whirlpoolnews.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.nitecafe.whirlpoolnews.R;
import com.android.nitecafe.whirlpoolnews.models.Whim;
import com.android.nitecafe.whirlpoolnews.utilities.WhirlpoolDateUtils;
import com.android.nitecafe.whirlpoolnews.utilities.interfaces.IPreferencesGetter;
import com.jakewharton.rxbinding.view.RxView;
import com.marshalchen.ultimaterecyclerview.UltimateViewAdapter;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.subjects.PublishSubject;

public class WhimsAdapter extends UltimateViewAdapter<WhimsAdapter.WhimViewHolder> {

    private List<Whim> whims = new ArrayList<>();
    private PublishSubject<Integer> OnWhimClickedSubject = PublishSubject.create();
    private IPreferencesGetter preferencesGetter;

    public WhimsAdapter(IPreferencesGetter preferencesGetter) {
        this.preferencesGetter = preferencesGetter;
    }

    public void SetWhims(List<Whim> whims) {
        this.whims = whims;
        notifyDataSetChanged();
    }

    @Override
    public WhimViewHolder getViewHolder(View view) {
        return null;
    }

    @Override
    public WhimViewHolder onCreateViewHolder(ViewGroup parent) {
        final View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_whims, parent, false);
        return new WhimViewHolder(inflate);
    }

    @Override public void onBindViewHolder(WhimViewHolder holder, int position) {
        Whim whim = whims.get(position);
        holder.whimFrom.setText(whim.getFROM().getNAME());
        final DateTime localDateFromString = WhirlpoolDateUtils.getLocalDateFromString(whim.getDATE());
        holder.whimSentTime.setText(localDateFromString.toString("yyyy-MM-dd hh:mm aa"));
        String message = whim.getMESSAGE();
        holder.whimContent.setText(message);

        if (whim.getVIEWED() == 0) {
            if (preferencesGetter.isDarkThemeOn())
                holder.itemView.setBackgroundResource(R.color.primary_dark);
            else
                holder.itemView.setBackgroundResource(R.color.primary_light);
        } else
            holder.itemView.setBackgroundResource(0);
    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        return null;
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getAdapterItemCount() {
        return whims.size();
    }

    @Override
    public long generateHeaderId(int position) {
        return 0;
    }

    public Observable<Whim> getOnWhimClickedSubject() {
        return OnWhimClickedSubject.map(integer -> whims.get(integer)).asObservable();
    }

    public class WhimViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.whim_from) TextView whimFrom;
        @Bind(R.id.whim_sent_time) TextView whimSentTime;
        @Bind(R.id.whim_content) TextView whimContent;

        public WhimViewHolder(View itemView) {
            super(itemView);
            RxView.clicks(itemView).map(aVoid -> getAdapterPosition())
                    .onErrorResumeNext(Observable.empty())
                    .subscribe(OnWhimClickedSubject);
            ButterKnife.bind(this, itemView);
        }
    }
}
