package com.visitegypt.domain.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.google.gson.annotations.SerializedName;
import com.visitegypt.domain.model.converters.PlacesTypeConverter;

import java.util.List;
//@Entity(tableName = "recommendation_places")
public class RecommendationPlaces {
    @PrimaryKey(autoGenerate = true)
    private String id;
    @SerializedName("user_likes")
    @TypeConverters(PlacesTypeConverter.class)
    private List<Place> userLikePlaces;
    @SerializedName("people_likes")
    @TypeConverters(PlacesTypeConverter.class)
    private List<Place> peopleLikePlaces;

    public RecommendationPlaces(String id, List<Place> userLikePlaces, List<Place> peopleLikePlaces) {
        this.id = id;
        this.userLikePlaces = userLikePlaces;
        this.peopleLikePlaces = peopleLikePlaces;
    }

    public RecommendationPlaces(List<Place> userLikePlaces, List<Place> peopleLikePlaces) {
        this.userLikePlaces = userLikePlaces;
        this.peopleLikePlaces = peopleLikePlaces;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Place> getUserLikePlaces() {
        return userLikePlaces;
    }

    public void setUserLikePlaces(List<Place> userLikePlaces) {
        this.userLikePlaces = userLikePlaces;
    }

    public List<Place> getPeopleLikePlaces() {
        return peopleLikePlaces;
    }

    public void setPeopleLikePlaces(List<Place> peopleLikePlaces) {
        this.peopleLikePlaces = peopleLikePlaces;
    }
}
