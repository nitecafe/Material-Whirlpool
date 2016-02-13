package com.android.nitecafe.whirlpoolnews.models;

import android.text.Html;

import java.util.Date;

public class ScrapedThread implements IThreadBase {

    private final int id;
    private final Date last_date;
    private final String last_poster;
    private final String title;
    private final String forum;
    private final int forum_id;
    private String lastDateText;
    private int pageCount;
    private boolean sticky;
    private boolean closed;
    private boolean deleted;
    private boolean moved;

    public ScrapedThread(int id, String title, Date last_date, String last_poster, String forum, int forum_id) {
        this.id = id;
        this.title = title;
        this.last_date = last_date;
        this.last_poster = last_poster;
        this.forum = forum;
        this.forum_id = forum_id;
    }

    public String getTitle() {
        return title;
    }

    public Date getLast_date() {
        return last_date;
    }

    public String getLast_poster() {
        return last_poster;
    }

    public String getForum() {
        return forum;
    }

    public int getForum_id() {
        return forum_id;
    }

    public String getLastDateText() {
        return lastDateText;
    }

    public void setLastDateText(String lastDateText) {
        this.lastDateText = lastDateText;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public boolean isSticky() {
        return sticky;
    }

    public void setSticky(boolean sticky) {
        this.sticky = sticky;
    }

    public boolean isClosed() {
        return closed;
    }

    public void setClosed(boolean closed) {
        this.closed = closed;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public boolean isMoved() {
        return moved;
    }

    public void setMoved(boolean moved) {
        this.moved = moved;
    }

    @Override
    public Integer getID() {
        return id;
    }
}
