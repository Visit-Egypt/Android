package com.visitegypt.domain.model;

import androidx.room.TypeConverters;

import com.google.gson.annotations.SerializedName;
import com.visitegypt.domain.model.converters.ImageUrlsConverter;

import java.util.ArrayList;

public class Item {
    private String title;

    @SerializedName("long_description")
    private String description;
    @TypeConverters(ImageUrlsConverter.class)
    @SerializedName("list_of_images")
    private ArrayList<String> imageUrls;

    @SerializedName("place_id")
    private String placeId;

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

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }
}
