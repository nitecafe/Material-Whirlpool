package com.android.nitecafe.whirlpoolnews.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class WhimsList {

    private List<Whim> WHIMS = new ArrayList<Whim>();
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * @return The WHIMS
     */
    public List<Whim> getWHIMS() {
        return WHIMS;
    }

    /**
     * @param WHIMS The WHIMS
     */
    public void setWHIMS(List<Whim> WHIMS) {
        this.WHIMS = WHIMS;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}