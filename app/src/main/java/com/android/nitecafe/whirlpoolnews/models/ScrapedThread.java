package com.android.nitecafe.whirlpoolnews.models;

import java.util.Date;

public class ScrapedThread {

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
        this.title = removeCommonHtmlChars(title);
        this.last_date = last_date;
        this.last_poster = last_poster;
        this.forum = forum;
        this.forum_id = forum_id;
    }

    /**
     * Replaces (some common) HTML characters with the actual character
     *
     * @param text String to search for HTML characters
     * @return String with HTML characters replaced with actual characters
     */
    public static String removeCommonHtmlChars(String text) {
        String[] chars = {
                "&quot;", "\"",
                "&amp;", "&",
                "&frasl;", "/",
                "&lt;", "<",
                "&gt;", ">",
                "&ndash;", "-",
                "&nbsp;", " "
        };

        for (int i = 0; i < chars.length; i = i + 2) {
            text = text.replace(chars[i], chars[i + 1]);
        }

        return text;
    }

    public String getTitle() {
        return title;
    }

    public int getId() {
        return id;
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
}
