package com.studyhub.model;

public class Friend {

    private String id; // ID of the user who is logged in
    private String userid; // ID of the friend to add

    // Default constructor
    public Friend() {
    }

    public Friend(String id, String userid) {
        this.id = id;
        this.userid = userid;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }
}
