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

    public String getBookMarkName() {
        return bookMarkName;
    }

    public int getThreadId() {
        return threadId;
    }

    public int getPostId() {
        return postId;
    }

    public int getPageLocated() {
        return pageLocated;
    }

    public int getPositionOnPage() {
        return positionOnPage;
    }
}
