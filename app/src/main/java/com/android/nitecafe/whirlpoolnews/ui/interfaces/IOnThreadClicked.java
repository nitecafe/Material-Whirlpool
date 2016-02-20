package com.android.nitecafe.whirlpoolnews.ui.interfaces;

public interface IOnThreadClicked {
    void OnThreadClicked(int threadId, String threadTitle, int totalPage);

    void OnWatchedThreadClicked(int threadId, String threadTitle, int lastPageRead, int lastReadId, int totalPage);

    void OnThreadClickedViewPager(int threadId, String threadTitle, int totalPage);
}
