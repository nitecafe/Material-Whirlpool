package com.android.nitecafe.whirlpoolnews.models;

public interface IWhirlpoolThread extends IThreadBase {

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
     * @return The LASTDATE
     */
    String getLASTDATE();

    /**
     * @return The LastPoster
     */
    Poster getLAST();

}
