
package com.android.nitecafe.whirlpoolnews.models;

import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class Recent {

    @SerializedName("LAST_DATE")
    private String LASTDATE;
    private Poster FIRST;
    @SerializedName("FORUM_NAME")
    private String FORUMNAME;
    private Poster LAST;
    @SerializedName("FORUM_ID")
    private Integer FORUMID;
    @SerializedName("REPLIES_BY_USER")
    private Integer REPLIESBYUSER;
    private Integer ID;
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
     * @return The Poster
     */
    public Poster getFIRST() {
        return FIRST;
    }

    /**
     * @param FIRST The Poster
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
     * @return The LastPoster
     */
    public Poster getLAST() {
        return LAST;
    }

    /**
     * @param LAST The LastPoster
     */
    public void setLAST(Poster LAST) {
        this.LAST = LAST;
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
     * @return The REPLIESBYUSER
     */
    public Integer getREPLIESBYUSER() {
        return REPLIESBYUSER;
    }

    /**
     * @param REPLIESBYUSER The REPLIES_BY_USER
     */
    public void setREPLIESBYUSER(Integer REPLIESBYUSER) {
        this.REPLIESBYUSER = REPLIESBYUSER;
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
     * @return The TITLE
     */
    public String getTITLE() {
        return TITLE;
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
