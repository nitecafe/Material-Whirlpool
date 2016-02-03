package com.android.nitecafe.whirlpoolnews.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class ForumList {

    private List<Forum> FORUM = new ArrayList<Forum>();
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * @return The FORUM
     */
    public List<Forum> getFORUM() {
        return FORUM;
    }

    /**
     * @param FORUM The FORUM
     */
    public void setFORUM(List<Forum> FORUM) {
        this.FORUM = FORUM;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}