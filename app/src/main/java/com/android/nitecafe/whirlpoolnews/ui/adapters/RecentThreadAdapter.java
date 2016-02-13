package com.android.nitecafe.whirlpoolnews.ui.adapters;

import com.android.nitecafe.whirlpoolnews.models.Recent;
import com.android.nitecafe.whirlpoolnews.utilities.IWatchedThreadIdentifier;

public class RecentThreadAdapter extends ThreadStickyHeaderAdapter<Recent> {

    public RecentThreadAdapter(IWatchedThreadIdentifier watchedThreadIdentifier) {
        super(watchedThreadIdentifier);
    }
}
