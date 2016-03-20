package com.android.nitecafe.whirlpoolnews.ui.fragments;

import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import com.android.nitecafe.whirlpoolnews.R;
import com.android.nitecafe.whirlpoolnews.ui.interfaces.IThreadActionMessageFragment;

import rx.subscriptions.CompositeSubscription;

public class BaseFragment extends Fragment implements IThreadActionMessageFragment {

    protected CompositeSubscription mSubscriptions = new CompositeSubscription();

    protected void setToolbarTitle(String title) {
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        ActionBar supportActionBar = activity.getSupportActionBar();
        if (supportActionBar != null)
            supportActionBar.setTitle(title);
    }

    @Override public void onDestroy() {
        mSubscriptions.unsubscribe();
        super.onDestroy();
    }

    @Override
    public void ShowMarkAsReadSuccessMessage() {
        Snackbar.make(getView(), R.string.message_marked_read, Snackbar.LENGTH_LONG)
                .show();
    }

    @Override
    public void ShowThreadWatchedSuccessMessage() {
        Snackbar.make(getView(), R.string.message_thread_watched, Snackbar.LENGTH_LONG)
                .show();
    }

    @Override
    public void ShowThreadUnWatchedSuccessMessage() {
        Snackbar.make(getView(), R.string.message_thread_unwatched, Snackbar.LENGTH_LONG)
                .show();
    }

    @Override
    public void ShowActionFailedMessage() {
        Snackbar.make(getView(), R.string.message_generic_error, Snackbar.LENGTH_LONG)
                .show();
    }
}
