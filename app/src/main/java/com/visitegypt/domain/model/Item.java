package com.visitegypt.domain.model;

import androidx.room.TypeConverters;

import com.visitegypt.domain.model.Converters.ImageUrlsConverter;

import java.util.List;

public class Item {
    private String title;
    private String description;
    @TypeConverters(ImageUrlsConverter.class)
    private List<String> imageUrls;

    public Item() {

    }

    public Item(String title, String description, List<String> imageUrls) {
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

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrl(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }
}
