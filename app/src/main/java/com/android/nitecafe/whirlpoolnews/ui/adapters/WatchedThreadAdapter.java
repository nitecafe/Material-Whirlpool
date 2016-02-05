package com.android.nitecafe.whirlpoolnews.ui.adapters;

import com.android.nitecafe.whirlpoolnews.models.Watched;
import com.android.nitecafe.whirlpoolnews.ui.interfaces.IRecycleViewItemClick;

public class WatchedThreadAdapter extends ThreadStickyHeaderAdapter<Watched> {
    public WatchedThreadAdapter(IRecycleViewItemClick itemClickHandler) {
        super(itemClickHandler);
    }

    @Override protected String getThreadTitleText(Watched thread) {
        return thread.getTITLE() + " (" + thread.getUNREAD() + " unread)";
    }
}
