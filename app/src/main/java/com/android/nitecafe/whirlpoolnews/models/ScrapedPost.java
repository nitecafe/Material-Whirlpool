package com.android.nitecafe.whirlpoolnews.models;

public class ScrapedPost {

    private String id;
    private User user;
    private String posted_time;
    private String content;
    private boolean edited;
    private boolean op;
    private boolean deleted;

    public ScrapedPost(String id, User user, String posted_time, String content, boolean edited, boolean op) {
        this.id = id;
        this.user = user;
        this.posted_time = posted_time;
        this.content = content;
        this.edited = edited;
        this.op = op;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public boolean isOp() {
        return op;
    }

    public boolean isEdited() {
        return edited;
    }

    public String getContent() {
        return content;
    }

    public String getPosted_time() {
        return posted_time;
    }

    public User getUser() {
        return user;
    }

    public String getId() {
        return id;
    }

    public int getIdInteger() {
        return Integer.parseInt(id);
    }
}
