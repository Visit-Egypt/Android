package com.visitegypt.domain.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PlacePageResponse {
    @SerializedName("current_page")
    private int currentPage;
    @SerializedName("has_next")
    private boolean hasNext;
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
