package com.android.nitecafe.whirlpoolnews.models;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class Contact {

    private Integer BLOCKED;
    private String NAME;
    private Integer ID;
    private String LASTONLINE;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * @return The BLOCKED
     */
    public Integer getBLOCKED() {
        return BLOCKED;
    }

    /**
     * @param BLOCKED The BLOCKED
     */
    public void setBLOCKED(Integer BLOCKED) {
        this.BLOCKED = BLOCKED;
    }

    /**
     * @return The NAME
     */
    public String getNAME() {
        return NAME;
    }

    /**
     * @param NAME The NAME
     */
    public void setNAME(String NAME) {
        this.NAME = NAME;
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
     * @return The LASTONLINE
     */
    public String getLASTONLINE() {
        return LASTONLINE;
    }

    /**
     * @param LASTONLINE The LASTONLINE
     */
    public void setLASTONLINE(String LASTONLINE) {
        this.LASTONLINE = LASTONLINE;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}