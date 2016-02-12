package com.android.nitecafe.whirlpoolnews.models;

import android.text.Html;

import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class Watched implements IWhirlpoolThread {

    @SerializedName("LAST_DATE")
    private String LASTDATE;
    private Poster FIRST;
    @SerializedName("FORUM_NAME")
    private String FORUMNAME;
    private Poster LAST;
    private Integer LASTREAD;
    @SerializedName("FORUM_ID")
    private Integer FORUMID;
    private Integer UNREAD;
    private Integer ID;
    private Integer LASTPAGE;
    private String TITLE;
    private Integer REPLIES;
    @SerializedName("FIRST_DATE")
    private String FIRSTDATE;

    /**
     * @return The LASTDATE
     */
    public String getLASTDATE() {
        return LASTDATE;
    }

    /**
     * @param LASTDATE The LAST_DATE
     */
    public void setLASTDATE(String LASTDATE) {
        this.LASTDATE = LASTDATE;
    }

    /**
     * @return The FIRST
     */
    public Poster getFIRST() {
        return FIRST;
    }

    /**
     * @param FIRST The FIRST
     */
    public void setFIRST(Poster FIRST) {
        this.FIRST = FIRST;
    }

    /**
     * @return The FORUMNAME
     */
    public String getFORUMNAME() {
        return FORUMNAME;
    }

    /**
     * @param FORUMNAME The FORUM_NAME
     */
    public void setFORUMNAME(String FORUMNAME) {
        this.FORUMNAME = FORUMNAME;
    }

    /**
     * @return The LAST
     */
    public Poster getLAST() {
        return LAST;
    }

    /**
     * @param LAST The LAST
     */
    public void setLAST(Poster LAST) {
        this.LAST = LAST;
    }

    /**
     * @return The LASTREAD
     */
    public Integer getLASTREAD() {
        return LASTREAD;
    }

    /**
     * @param LASTREAD The LASTREAD
     */
    public void setLASTREAD(Integer LASTREAD) {
        this.LASTREAD = LASTREAD;
    }

    /**
     * @return The FORUMID
     */
    public Integer getFORUMID() {
        return FORUMID;
    }

    /**
     * @param FORUMID The FORUM_ID
     */
    public void setFORUMID(Integer FORUMID) {
        this.FORUMID = FORUMID;
    }

    /**
     * @return The UNREAD
     */
    public Integer getUNREAD() {
        return UNREAD;
    }

    /**
     * @param UNREAD The UNREAD
     */
    public void setUNREAD(Integer UNREAD) {
        this.UNREAD = UNREAD;
    }

    /**
     * @return The ID
     */
    public Integer getID() {
        return ID;
    }

    /**
     * @param ID The ID
     */
    public void setID(Integer ID) {
        this.ID = ID;
    }

    /**
     * @return The LASTPAGE
     */
    public Integer getLASTPAGE() {
        return LASTPAGE;
    }

    /**
     * @param LASTPAGE The LASTPAGE
     */
    public void setLASTPAGE(Integer LASTPAGE) {
        this.LASTPAGE = LASTPAGE;
    }

    /**
     * @return The TITLE
     */
    public String getTITLE() {
        return Html.fromHtml(TITLE).toString();
    }

    /**
     * @param TITLE The TITLE
     */
    public void setTITLE(String TITLE) {
        this.TITLE = TITLE;
    }

    /**
     * @return The REPLIES
     */
    public Integer getREPLIES() {
        return REPLIES;
    }

    /**
     * @param REPLIES The REPLIES
     */
    public void setREPLIES(Integer REPLIES) {
        this.REPLIES = REPLIES;
    }

    /**
     * @return The FIRSTDATE
     */
    public String getFIRSTDATE() {
        return FIRSTDATE;
    }

    /**
     * @param FIRSTDATE The FIRST_DATE
     */
    public void setFIRSTDATE(String FIRSTDATE) {
        this.FIRSTDATE = FIRSTDATE;
    }

}