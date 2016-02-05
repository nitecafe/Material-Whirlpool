package com.android.nitecafe.whirlpoolnews.models;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class WatchedList {

    private List<Watched> WATCHED = new ArrayList<Watched>();

    /**
     * @return The WATCHED
     */
    public List<Watched> getWATCHED() {
        return WATCHED;
    }

    /**
     * @param WATCHED The WATCHED
     */
    public void setWATCHED(List<Watched> WATCHED) {
        this.WATCHED = WATCHED;
    }

}