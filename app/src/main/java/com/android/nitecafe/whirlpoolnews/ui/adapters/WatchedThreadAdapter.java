package com.android.nitecafe.whirlpoolnews.ui.adapters;

import com.android.nitecafe.whirlpoolnews.models.Watched;
import com.android.nitecafe.whirlpoolnews.utilities.IWatchedThreadIdentifier;

public class WatchedThreadAdapter extends ThreadStickyHeaderAdapter<Watched> {

    public WatchedThreadAdapter(IWatchedThreadIdentifier watchedThreadIdentifier) {
        super(watchedThreadIdentifier);
    }

    protected String getThreadTitleText(Watched thread) {
        return thread.getTITLE() + " (" + thread.getUNREAD() + " unread)";
    }
}
