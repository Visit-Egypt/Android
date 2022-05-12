package com.visitegypt.domain.model;

import com.google.gson.annotations.SerializedName;

public class TripMateRequest {
    private String id;
    private String title;
    private String description;
    @SerializedName("initator_id")
    private String userID;
    @SerializedName("is_approved")
    private boolean isApproved;

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public boolean isApproved() {
        return isApproved;
    }

    public void setApproved(boolean approved) {
        isApproved = approved;
    }

    public TripMateRequest(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
