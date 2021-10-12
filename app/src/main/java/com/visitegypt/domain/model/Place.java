package com.visitegypt.domain.model;

import androidx.room.Entity;

import com.visitegypt.utils.Constants;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
public class Place {
    private String title;
    private String description;
    private List<String> imageUrls;
    private Map<Constants.customerType, Integer> ticketPrices = new HashMap<>();
    private Map<Constants.weekDays, String> openingHours = new HashMap<>();
    private List<Review> reviews;

    public Place() {
    }

    public Place(String title, String description, Map<Constants.customerType, Integer> ticketPrices) {
        this.title = title;
        this.description = description;
        this.ticketPrices = ticketPrices;
    }

    public Place(String title, String description, List<String> mainImageUrl) {
        this.title = title;
        this.description = description;
        this.imageUrls = mainImageUrl;
    }

    public Place(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public Place(String title, String description, List<String> mainImageUrl, Map<Constants.customerType, Integer> ticketPrices) {
        this.title = title;
        this.description = description;
        this.imageUrls = mainImageUrl;
        this.ticketPrices = ticketPrices;
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

    public List<String> getMainImageUrl() {
        return imageUrls;
    }

    public void setMainImageUrl(List<String> mainImageUrls) {
        this.imageUrls = mainImageUrls;
    }

    public Map<Constants.customerType, Integer> getTicketPrices() {
        return ticketPrices;
    }

    public void setTicketPrices(Map<Constants.customerType, Integer> ticketPrices) {
        this.ticketPrices = ticketPrices;
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    public Map<Constants.weekDays, String> getOpeningHours() {
        return openingHours;
    }

    public void setOpeningHours(Map<Constants.weekDays, String> openingHours) {
        this.openingHours = openingHours;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }
}
