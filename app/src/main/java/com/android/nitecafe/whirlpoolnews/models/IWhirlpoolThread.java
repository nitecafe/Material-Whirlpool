package com.android.nitecafe.whirlpoolnews.models;

import com.google.gson.annotations.SerializedName;

public interface IWhirlpoolThread {

    /**
     * @return The FORUMNAME
     */
    public String getFORUMNAME();

    /**
     * @return The TITLE
     */
    public String getTITLE();

    /**
     * @return The REPLIES
     */
    public Integer getREPLIES();

    /**
     * @return The ID
     */
    public Integer getID();

    /**
     * @return The LASTDATE
     */
    public String getLASTDATE();

    /**
     * @return The LastPoster
     */
    public Poster getLAST();

}
