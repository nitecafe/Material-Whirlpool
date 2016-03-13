package com.android.nitecafe.whirlpoolnews.utilities.customTabs;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

public class DefaultBrowserFallback implements CustomTabsActivityHelper.CustomTabFallback {
    @Override
    public void openUri(Activity activity, Uri uri) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, uri);
        activity.startActivity(browserIntent);
    }
}
