package com.android.nitecafe.whirlpoolnews.models;

import java.util.HashMap;
import java.util.Map;

public class News {

    private String DATE;
    private String SOURCE;
    private String BLURB;
    private Integer ID;
    private String TITLE;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     *
     * @return
     * The DATE
     */
    public String getDATE() {
        return DATE;
    }

    /**
     *
     * @param DATE
     * The DATE
     */
    public void setDATE(String DATE) {
        this.DATE = DATE;
    }

    /**
     *
     * @return
     * The SOURCE
     */
    public String getSOURCE() {
        return SOURCE;
    }

    /**
     *
     * @param SOURCE
     * The SOURCE
     */
    public void setSOURCE(String SOURCE) {
        this.SOURCE = SOURCE;
    }

    /**
     *
     * @return
     * The BLURB
     */
    public String getBLURB() {
        return BLURB;
    }

    /**
     *
     * @param BLURB
     * The BLURB
     */
    public void setBLURB(String BLURB) {
        this.BLURB = BLURB;
    }

    /**
     *
     * @return
     * The ID
     */
    public Integer getID() {
        return ID;
    }

    /**
     *
     * @param ID
     * The ID
     */
    public void setID(Integer ID) {
        this.ID = ID;
    }

    /**
     *
     * @return
     * The TITLE
     */
    public String getTITLE() {
        return TITLE;
    }

    /**
     *
     * @param TITLE
     * The TITLE
     */
    public void setTITLE(String TITLE) {
        this.TITLE = TITLE;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}