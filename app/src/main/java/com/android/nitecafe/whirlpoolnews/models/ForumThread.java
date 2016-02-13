package com.android.nitecafe.whirlpoolnews.models;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class ForumThread implements IWhirlpoolThread {

    @SerializedName("LAST_DATE")
    private String LASTDATE;
    @SerializedName("FORUM_DATE")
    private String FORUMNAME;
    private Poster LAST;
    @SerializedName("FORUM_ID")
    private Integer FORUMID;
    private Integer ID;
    private String TITLE;
    private Integer REPLIES;
    private String GROUP;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

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
     * @return The GROUP
     */
    public String getGROUP() {
        return GROUP;
    }

    /**
     * @param GROUP The GROUP
     */
    public void setGROUP(String GROUP) {
        this.GROUP = GROUP;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}