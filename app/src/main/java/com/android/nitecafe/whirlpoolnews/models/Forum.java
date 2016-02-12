package com.android.nitecafe.whirlpoolnews.models;

import android.text.Html;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class Forum {

    private Integer SORT;
    private Integer ID;
    private String TITLE;
    private String SECTION;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * @return The SORT
     */
    public Integer getSORT() {
        return SORT;
    }

    /**
     * @param SORT The SORT
     */
    public void setSORT(Integer SORT) {
        this.SORT = SORT;
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
     * @return The SECTION
     */
    public String getSECTION() {
        return SECTION;
    }

    /**
     * @param SECTION The SECTION
     */
    public void setSECTION(String SECTION) {
        this.SECTION = SECTION;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}