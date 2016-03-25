package com.android.nitecafe.whirlpoolnews.ui.interfaces;

public interface IOnThreadClicked {
    void OnThreadClicked(int threadId, String threadTitle, int totalPage, int forumId);

    void OnThreadClicked(int threadId, String threadTitle, int lastPageRead, int lastReadId, int totalPage, int forumId);

    void OnOpenWebVersionClicked(int threadId, int totalPage);

    void OnOpenWebVersionClicked(int threadId, int lastPageRead, int lastReadId);
}
