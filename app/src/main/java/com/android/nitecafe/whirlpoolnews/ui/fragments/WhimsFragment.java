package com.android.nitecafe.whirlpoolnews.ui.fragments;

import android.content.Context;
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
import com.android.nitecafe.whirlpoolnews.ui.interfaces.IOnWhimClicked;
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
    private IOnWhimClicked listener;

    @Override
    public void onDestroyView() {
        whimsController.Attach(null);
        super.onDestroyView();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof IOnWhimClicked)
            listener = (IOnWhimClicked) context;
        else
            throw new ClassCastException("Activity must implement IOnWhimClicked");
    }

    @Override
    public void onDetach() {
        listener = null;
        super.onDetach();
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
        whimAdapter.getOnWhimClickedSubject().subscribe(whim ->
                OpenWhim(whim.getID(), whim.getMESSAGE(), whim.getFROM().getNAME()));

        recyclerView.setAdapter(whimAdapter);
        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getContext()).build());
        recyclerView.setDefaultOnRefreshListener(() -> loadWhims());
    }

    private void OpenWhim(Integer id, String message, String sender) {
        whimsController.MarkWhimAsRead(id);
        listener.OnWhimClicked(id, message, sender);
    }

    private void loadWhims() {
        whimsController.GetWhims();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setToolbarTitle(getActivity().getString(R.string.title_private_messages));
    }

    @Override public void ShowErrorMessage() {
        Snackbar.make(getView(), R.string.message_generic_error, Snackbar.LENGTH_LONG)
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
