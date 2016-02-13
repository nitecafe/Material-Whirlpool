package com.android.nitecafe.whirlpoolnews.ui.fragments;

import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

public class BaseFragment extends Fragment implements IThreadActionMessageFragment {

    protected void setToolbarTitle(String title) {
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        ActionBar supportActionBar = activity.getSupportActionBar();
        if (supportActionBar != null)
            supportActionBar.setTitle(title);
    }

    @Override
    public void ShowMarkAsReadSuccessMessage() {
        Snackbar.make(getView(), "Marked as read.", Snackbar.LENGTH_LONG)
                .show();
    }

    @Override
    public void ShowThreadWatchedSuccessMessage() {
        Snackbar.make(getView(), "Thread is now being watched.", Snackbar.LENGTH_LONG)
                .show();
    }

    @Override
    public void ShowThreadUnWatchedSuccessMessage() {
        Snackbar.make(getView(), "Thread is now unwatched.", Snackbar.LENGTH_LONG)
                .show();
    }

    @Override
    public void ShowActionFailedMessage() {
        Snackbar.make(getView(), "Something went wrong. Try again", Snackbar.LENGTH_LONG)
                .show();
    }
}
