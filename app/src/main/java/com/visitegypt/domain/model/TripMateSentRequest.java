package com.visitegypt.domain.model;

import java.util.List;

public class TripMateSentRequest extends  TripMateRequest{
    private String userName;
    private String photoUrl;
    private int followersNumber;
    public TripMateSentRequest(String title, String description) {
        super(title, description);
    }

    public TripMateSentRequest(String id, String title, String description, String userID, boolean isApproved) {
        super(id, title, description, userID, isApproved);
    }

    public TripMateSentRequest(String title, String description, String userName, String photoUrl, int followersNumber) {
        super(title, description);
        this.userName = userName;
        this.photoUrl = photoUrl;
        this.followersNumber = followersNumber;
    }

    public TripMateSentRequest(String id, String title, String description, String userID, boolean isApproved, String userName, String photoUrl, int followersNumber) {
        super(id, title, description, userID, isApproved);
        this.userName = userName;
        this.photoUrl = photoUrl;
        this.followersNumber = followersNumber;
    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public int getFollowersNumber() {
        return followersNumber;
    }

    public void setFollowersNumber(int followersNumber) {
        this.followersNumber = followersNumber;
    }
}
