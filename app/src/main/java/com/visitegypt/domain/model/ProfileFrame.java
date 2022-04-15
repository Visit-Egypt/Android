package com.visitegypt.domain.model;

import com.google.gson.annotations.SerializedName;

public class ProfileFrame {

    public static final int NEWBIE = 0, TRAVELLER = 1, EXPLORER = 2, ADVENTURER = 3, EXPERT = 4, PHAROS = 5;

    private int id;
    private int type;
    @SerializedName("image_url")
    private String imageUrl;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
