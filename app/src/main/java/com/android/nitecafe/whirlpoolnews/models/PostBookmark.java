package com.android.nitecafe.whirlpoolnews.models;

public class PostBookmark {

    private String bookMarkName;
    private int threadId;
    private int postId;
    private int pageLocated;
    private int positionOnPage;
    private String threadTitle;
    private int totalPage;

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

    public String getThreadTitle() {
        return threadTitle;
    }

    public void setThreadTitle(String threadTitle) {
        this.threadTitle = threadTitle;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }
}
