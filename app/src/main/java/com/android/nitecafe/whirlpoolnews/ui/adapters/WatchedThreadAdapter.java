package com.android.nitecafe.whirlpoolnews.ui.adapters;

import com.android.nitecafe.whirlpoolnews.models.Watched;
import com.android.nitecafe.whirlpoolnews.utilities.interfaces.IStickyHeaderUtil;
import com.android.nitecafe.whirlpoolnews.web.interfaces.IWatchedThreadService;

public class WatchedThreadAdapter extends ThreadStickyHeaderAdapter<Watched> {

    public WatchedThreadAdapter(IWatchedThreadService watchedThreadIdentifier, IStickyHeaderUtil stickyHeaderUtil) {
        super(watchedThreadIdentifier, stickyHeaderUtil);
    }

    protected String getThreadTitleText(Watched thread) {
        if (thread.getUNREAD() == 0)
            return thread.getTITLE();
        return thread.getTITLE() + " (" + thread.getUNREAD() + " unread)";
    }
}
