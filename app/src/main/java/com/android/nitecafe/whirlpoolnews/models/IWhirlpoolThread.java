package com.android.nitecafe.whirlpoolnews.models;

public interface IWhirlpoolThread {

    /**
     * @return The FORUMNAME
     */
    String getFORUMNAME();

    /**
     * @return The TITLE
     */
    String getTITLE();

    /**
     * @return The REPLIES
     */
    Integer getREPLIES();

    /**
     * @return The ID
     */
    Integer getID();

    /**
     * @return The LASTDATE
     */
    String getLASTDATE();

    /**
     * @return The LastPoster
     */
    Poster getLAST();

}
