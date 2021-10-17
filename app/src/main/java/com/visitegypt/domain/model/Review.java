package com.visitegypt.domain.model;

public class Review {
    private float rating;
    private String review;
    private User user;

    public Review() {
    }

    public Review(float rating, String review, User user) {
        this.rating = rating;
        this.review = review;
        this.user = user;
    }

    public Review(float rating, User user) {
        this.rating = rating;
        this.user = user;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
