package com.visitegypt.domain.model;

import com.google.gson.annotations.SerializedName;

public class Badge {

    public static final int PLACE = 0, CITY = 1, REGION = 2, GENERAL = 3;

    private int id;
    @SerializedName("image_url")
    private String imageUrl;
    int progress;
    private boolean owned;
    private int type;
    private int xp;
    @SerializedName("badge_frame_image_url")
    private String badgeImageFrame;


    public Badge(int id, String imageUrl, boolean owned, int type, int xp) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.owned = owned;
        this.type = type;
        this.xp = xp;
    }

    public Badge(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Badge() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isOwned() {
        return owned;
    }

    public void setOwned(boolean owned) {
        this.owned = owned;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getXp() {
        return xp;
    }

    public void setXp(int xp) {
        this.xp = xp;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }
}
