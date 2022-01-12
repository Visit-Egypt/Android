package com.visitegypt.domain.model;

import com.google.gson.annotations.SerializedName;

public class UserUpdateRequest {
    @SerializedName("first_name")
    private String firstName;
    @SerializedName("last_name")
    private String lastName;
    @SerializedName("email")
    private String email;
    @SerializedName("phone_number")
    private String phoneNumber;
    @SerializedName("password")
    private String password;
    @SerializedName("photo_link")
    private String photoLink;

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPhotoLink(String photoLink) {
        this.photoLink = photoLink;
    }
}
