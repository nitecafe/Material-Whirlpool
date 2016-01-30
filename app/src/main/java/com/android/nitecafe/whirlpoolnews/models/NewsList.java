package com.android.nitecafe.whirlpoolnews.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewsList {

    private List<News> NEWS = new ArrayList<News>();
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     *
     * @return
     * The NEWS
     */
    public List<News> getNEWS() {
        return NEWS;
    }

    /**
     *
     * @param NEWS
     * The NEWS
     */
    public void setNEWS(List<News> NEWS) {
        this.NEWS = NEWS;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}