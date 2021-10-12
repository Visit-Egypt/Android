package com.visitegypt.domain.model;

import java.util.ArrayList;

public class Item {
    private String title;
    private String description;
    private ArrayList<String> imageUrls;

    public Item() {

    }

    public Item(String title, String description, ArrayList<String> imageUrls) {
        this.title = title;
        this.description = description;
        this.imageUrls = imageUrls;
    }

    public Item(String title, String description) {
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

    public ArrayList<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrl(ArrayList<String> imageUrls) {
        this.imageUrls = imageUrls;
    }
}
