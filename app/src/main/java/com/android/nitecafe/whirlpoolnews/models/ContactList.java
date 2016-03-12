package com.android.nitecafe.whirlpoolnews.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class ContactList {

    private List<Contact> CONTACTS = new ArrayList<Contact>();
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * @return The CONTACTS
     */
    public List<Contact> getCONTACTS() {
        return CONTACTS;
    }

    /**
     * @param CONTACTS The CONTACTS
     */
    public void setCONTACTS(List<Contact> CONTACTS) {
        this.CONTACTS = CONTACTS;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
