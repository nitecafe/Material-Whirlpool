package com.android.nitecafe.whirlpoolnews.models;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class Whim {

    private String MESSAGE;
    private String DATE;
    private Integer REPLIED;
    private Integer VIEWED;
    private Poster FROM;
    private Integer ID;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * @return The MESSAGE
     */
    public String getMESSAGE() {
        return MESSAGE;
    }

    /**
     * @param MESSAGE The MESSAGE
     */
    public void setMESSAGE(String MESSAGE) {
        this.MESSAGE = MESSAGE;
    }

    /**
     * @return The DATE
     */
    public String getDATE() {
        return DATE;
    }

    /**
     * @param DATE The DATE
     */
    public void setDATE(String DATE) {
        this.DATE = DATE;
    }

    /**
     * @return The REPLIED
     */
    public Integer getREPLIED() {
        return REPLIED;
    }

    /**
     * @param REPLIED The REPLIED
     */
    public void setREPLIED(Integer REPLIED) {
        this.REPLIED = REPLIED;
    }

    /**
     * @return The VIEWED
     */
    public Integer getVIEWED() {
        return VIEWED;
    }

    /**
     * @param VIEWED The VIEWED
     */
    public void setVIEWED(Integer VIEWED) {
        this.VIEWED = VIEWED;
    }

    /**
     * @return The FROM
     */
    public Poster getFROM() {
        return FROM;
    }

    /**
     * @param FROM The FROM
     */
    public void setFROM(Poster FROM) {
        this.FROM = FROM;
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

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}