package com.android.nitecafe.whirlpoolnews.ui.interfaces;

public interface IOnThreadClicked {
    void OnThreadClicked(int threadId, String threadTitle);

    void OnWatchedThreadClicked(int threadId, String threadTitle, int lastPageRead);
}
