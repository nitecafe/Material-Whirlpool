package com.android.nitecafe.whirlpoolnews.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.nitecafe.whirlpoolnews.R;
import com.android.nitecafe.whirlpoolnews.WhirlpoolApp;
import com.android.nitecafe.whirlpoolnews.controllers.WhimsController;
import com.android.nitecafe.whirlpoolnews.models.Whim;
import com.android.nitecafe.whirlpoolnews.ui.adapters.WhimsAdapter;
import com.android.nitecafe.whirlpoolnews.ui.interfaces.IWhimsFragment;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.marshalchen.ultimaterecyclerview.divideritemdecoration.HorizontalDividerItemDecoration;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.zhanghai.android.materialprogressbar.MaterialProgressBar;


public class WhimsFragment extends BaseFragment implements IWhimsFragment {

    @Inject WhimsController whimsController;
    @Bind(R.id.whim_progress_loader) MaterialProgressBar progressBar;
    @Bind(R.id.whim_recycle_view) UltimateRecyclerView recyclerView;
    private WhimsAdapter whimAdapter;

    @Override
    public void onDestroyView() {
        whimsController.Attach(null);
        super.onDestroyView();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View inflate = inflater.inflate(R.layout.fragment_whims, container, false);

        ButterKnife.bind(this, inflate);
        ((WhirlpoolApp) getActivity().getApplication()).getDaggerComponent().inject(this);
        whimsController.Attach(this);

        SetupRecycleView();
        loadWhims();

        return inflate;
    }

    private void SetupRecycleView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        whimAdapter = new WhimsAdapter();

        recyclerView.setAdapter(whimAdapter);
        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getContext()).build());
        recyclerView.setDefaultOnRefreshListener(() -> loadWhims());
    }

    private void loadWhims() {
        whimsController.GetWhims();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setToolbarTitle("Private Messages");
    }

    @Override public void ShowErrorMessage() {
        Snackbar.make(getView(), "Something went wrong. Try again", Snackbar.LENGTH_LONG)
                .show();
    }

    @Override public void DisplayWhims(List<Whim> whims) {
        whimAdapter.SetWhims(whims);
    }

    @Override public void HideAllProgressLoader() {
        progressBar.setVisibility(View.GONE);
        recyclerView.setRefreshing(false);
    }
}
