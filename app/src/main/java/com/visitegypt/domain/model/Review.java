package com.visitegypt.domain.model;

import com.google.gson.annotations.SerializedName;

public class Review {
    private float rating;
    private String review;
    @SerializedName("user_name")
    private String fullName;
    @SerializedName("user_id")
    private String userId;

    public Review() {
    }

    public Review(float rating, String review, String firstName, String userId) {
        this.rating = rating;
        this.review = review;
        this.fullName = firstName;
        this.userId = userId;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
