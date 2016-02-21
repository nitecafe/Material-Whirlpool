package com.android.nitecafe.whirlpoolnews.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.nitecafe.whirlpoolnews.R;
import com.android.nitecafe.whirlpoolnews.WhirlpoolApp;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.subjects.PublishSubject;

public class IndividualWhimFragment extends BaseFragment {

    private static final String WHIM_CONTENT = "WHIM_CONTENT";
    private static final String WHIM_SENDER = "WHIM_SENDER";
    public PublishSubject<Void> OnFragmentDestroySubject = PublishSubject.create();
    public PublishSubject<Void> OnFragmentCreateViewSubject = PublishSubject.create();
    @Bind(R.id.whim_individual_content) TextView whimContentTextView;
    private String whimContent;
    private String whimSender;

    public static IndividualWhimFragment newInstance(String whimContent, String whimSender) {
        IndividualWhimFragment fragment = new IndividualWhimFragment();
        Bundle args = new Bundle();
        args.putString(WHIM_SENDER, whimSender);
        args.putString(WHIM_CONTENT, whimContent);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onDestroyView() {
        OnFragmentDestroySubject.onNext(null);
        super.onDestroyView();
    }

    @Override
    public void onResume() {
        super.onResume();
        WhirlpoolApp.getInstance().trackScreenView("Individual Whim Fragment");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        whimContent = getArguments().getString(WHIM_CONTENT, "");
        whimSender = getArguments().getString(WHIM_SENDER, "");
    }

    @Override
    public void onDetach() {
        OnFragmentCreateViewSubject.onCompleted();
        OnFragmentDestroySubject.onCompleted();
        super.onDetach();
    }

    @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View inflate = inflater.inflate(R.layout.fragment_individual_whim, container, false);
        ButterKnife.bind(this, inflate);
        setToolbarTitle("Whim from " + whimSender);
        OnFragmentCreateViewSubject.onNext(null);
        whimContentTextView.setMovementMethod(LinkMovementMethod.getInstance());
        ShowContentOnTextView();
        return inflate;
    }

    private void ShowContentOnTextView() {
        whimContentTextView.setText(whimContent);
    }
}
