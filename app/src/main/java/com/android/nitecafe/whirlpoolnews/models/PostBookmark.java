package com.android.nitecafe.whirlpoolnews.models;

public class PostBookmark {

    private String bookMarkName;
    private int threadId;
    private int postId;
    private int pageLocated;
    private int positionOnPage;

    public PostBookmark(String bookMarkName, int threadId, int postId, int pageLocated, int positionOnPage) {
        this.bookMarkName = bookMarkName;
        this.threadId = threadId;
        this.postId = postId;
        this.pageLocated = pageLocated;
        this.positionOnPage = positionOnPage;
    }

    public PostBookmark(int postId, int positionOnPage) {
        this.postId = postId;
        this.positionOnPage = positionOnPage;
    }

    public String getBookMarkName() {
        return bookMarkName;
    }

    public void setBookMarkName(String bookMarkName) {
        this.bookMarkName = bookMarkName;
    }

    public int getThreadId() {
        return threadId;
    }

    public void setThreadId(int threadId) {
        this.threadId = threadId;
    }

    public int getPostId() {
        return postId;
    }

    public int getPageLocated() {
        return pageLocated;
    }

    public void setPageLocated(int pageLocated) {
        this.pageLocated = pageLocated;
    }

    public int getPositionOnPage() {
        return positionOnPage;
    }

    @Override
    public boolean equals(Object b) {
        if (!(b instanceof PostBookmark)) {
            return false;
        }
        PostBookmark bookmark = (PostBookmark) b;
        if (!getBookMarkName().equals(bookmark.getBookMarkName()))
            return false;
        if (getPageLocated() != bookmark.getPageLocated())
            return false;
        if (getPositionOnPage() != bookmark.getPageLocated())
            return false;
        if (getPostId() != bookmark.getPostId())
            return false;
        if (getThreadId() != bookmark.getThreadId())
            return false;
        return true;
    }
}
