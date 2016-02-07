package com.android.nitecafe.whirlpoolnews.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class ForumThreadList {

    private List<ForumThread> THREADS = new ArrayList<ForumThread>();
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * @return The THREADS
     */
    public List<ForumThread> getTHREADS() {
        return THREADS;
    }

    /**
     * @param THREADS The THREADS
     */
    public void setTHREADS(List<ForumThread> THREADS) {
        this.THREADS = THREADS;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}