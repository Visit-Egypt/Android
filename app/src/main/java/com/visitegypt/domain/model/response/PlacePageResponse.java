package com.visitegypt.domain.model.response;



import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.google.gson.annotations.SerializedName;
import com.visitegypt.domain.model.Place;
import com.visitegypt.domain.model.converters.PlacesTypeConverter;

import java.util.List;
@Entity(tableName = "place_page_response")
public class PlacePageResponse {
    @PrimaryKey
    @SerializedName("current_page")
    private int currentPage;
    @SerializedName("has_next")
    private boolean hasNext;
    @TypeConverters(PlacesTypeConverter.class)
    private List<Place> places;

    public PlacePageResponse() {

    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public boolean isHasNext() {
        return hasNext;
    }

    public void setHasNext(boolean hasNext) {
        this.hasNext = hasNext;
    }

    public List<Place> getPlaces() {
        return places;
    }

    public void setPlaces(List<Place> places) {
        this.places = places;
    }
}
