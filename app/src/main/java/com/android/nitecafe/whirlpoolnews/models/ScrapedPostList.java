package com.android.nitecafe.whirlpoolnews.models;

import java.util.ArrayList;

public class ScrapedPostList {

    private int thread_id;
    private String thread_title;
    private ArrayList<ScrapedPost> scrapedPosts = new ArrayList<>();
    private String notebar;
    private int pageCount;

    public ScrapedPostList(int thread_id, String thread_title) {

        this.thread_id = thread_id;
        this.thread_title = thread_title;
    }

    public int getThread_id() {
        return thread_id;
    }

    public String getThread_title() {
        return thread_title;
    }

    public ArrayList<ScrapedPost> getScrapedPosts() {
        return scrapedPosts;
    }

    public void setScrapedPosts(ArrayList<ScrapedPost> scrapedPosts) {
        this.scrapedPosts = scrapedPosts;
    }

    public String getNotebar() {
        return notebar;
    }

    public void setNotebar(String notebar) {
        this.notebar = notebar;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }
}
