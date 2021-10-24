package com.visitegypt.domain.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.visitegypt.domain.model.Converters.CategoriesConverter;
import com.visitegypt.domain.model.Converters.ImageUrlsConverter;
import com.visitegypt.domain.model.Converters.ItemsTypeConverter;
import com.visitegypt.domain.model.Converters.OpeningHoursConverter;
import com.visitegypt.domain.model.Converters.ReviewsConverter;
import com.visitegypt.domain.model.Converters.TicketPricesConverter;
import com.visitegypt.utils.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
public class Place {
    @PrimaryKey
    @NonNull
    private String id;
    private String title;
    private String description;

    private String locationDescription;
    private double longitude;
    private double altitude;
    private String city;

    private String default_image;

    @TypeConverters(CategoriesConverter.class)
    private List<String> categories;
    @TypeConverters(ItemsTypeConverter.class)
    private List<Item> items;
    @TypeConverters(ImageUrlsConverter.class)
    private List<String> imageUrls;
    @TypeConverters(TicketPricesConverter.class)
    private Map<Constants.customerType, Integer> ticketPrices = new HashMap<>();
    @TypeConverters(OpeningHoursConverter.class)
    private Map<Constants.weekDays, String> openingHours = new HashMap<>();
    @TypeConverters(ReviewsConverter.class)
    private List<Review> reviews;

    public Place() {
    }

    public Place(@NonNull String id, String title, String description, String locationDescription, int longitude, int altitude, ArrayList<Item> items, List<String> imageUrls, Map<Constants.customerType, Integer> ticketPrices, Map<Constants.weekDays, String> openingHours, List<Review> reviews) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.locationDescription = locationDescription;
        this.longitude = longitude;
        this.altitude = altitude;
        this.items = items;
        this.imageUrls = imageUrls;
        this.ticketPrices = ticketPrices;
        this.openingHours = openingHours;
        this.reviews = reviews;
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

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
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

    public String getLocationDescription() {
        return locationDescription;
    }

    public void setLocationDescription(String locationDescription) {
        this.locationDescription = locationDescription;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getAltitude() {
        return altitude;
    }

    public void setAltitude(double altitude) {
        this.altitude = altitude;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    public Map<Constants.customerType, Integer> getTicketPrices() {
        return ticketPrices;
    }

    public void setTicketPrices(Map<Constants.customerType, Integer> ticketPrices) {
        this.ticketPrices = ticketPrices;
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

    public String getDefault_image() {
        return default_image;
    }

    public void setDefault_image(String default_image) {
        this.default_image = default_image;
    }
}