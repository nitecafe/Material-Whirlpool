package com.android.nitecafe.whirlpoolnews.models;

/**
 * Created by grahamgoh on 9/02/16.
 */
public class User {
    private String group;
    private String userId;
    private String userName;

    public User(String userId, String userName) {
        this.userId = userId;

        this.userName = userName;
    }

    public void setGroup(String group) {
        this.group = group;
    }
}
