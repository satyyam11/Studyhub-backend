package com.studyhub.model;

import java.util.Date;

public class User {
    private String uid;
    private String displayName;
    private String email;
    private String avatarUrl;
    private Date createdAt;
    private Date updatedAt;

    public User() {}

    public User(String uid, String displayName, String email, String avatarUrl) {
        this.uid = uid;
        this.displayName = displayName;
        this.email = email;
        this.avatarUrl = avatarUrl;
        this.createdAt = new Date();
        this.updatedAt = new Date();
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}
