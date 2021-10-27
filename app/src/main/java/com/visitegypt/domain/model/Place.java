package com.visitegypt.domain.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.google.gson.annotations.SerializedName;
import com.visitegypt.domain.model.Converters.CategoriesConverter;
import com.visitegypt.domain.model.Converters.ImageUrlsConverter;
import com.visitegypt.domain.model.Converters.ItemsTypeConverter;
import com.visitegypt.domain.model.Converters.OpeningHoursConverter;
import com.visitegypt.domain.model.Converters.ReviewsConverter;
import com.visitegypt.domain.model.Converters.TicketPricesConverter;

import java.util.List;
import java.util.Map;

@Entity
public class Place {
    @PrimaryKey
    @NonNull
    private String id;
    private String title;

    @SerializedName("long_description")
    private String longDescription;
    @SerializedName("short_description")
    private String shortDescription;

    private String locationDescription;
    private double longitude;
    private double altitude;
    private String city;

    @SerializedName("default_image")
    private String defaultImage;

    @TypeConverters(CategoriesConverter.class)
    private List<String> categories;
    @TypeConverters(ItemsTypeConverter.class)
    private List<Item> items;
    @TypeConverters(ImageUrlsConverter.class)
    private List<String> imageUrls;

    @TypeConverters(TicketPricesConverter.class)
    @SerializedName("ticket_prices")
    private Map<String, Integer> ticketPrices;

    @TypeConverters(OpeningHoursConverter.class)
    private Map<String, String> openingHours;

    @TypeConverters(ReviewsConverter.class)
    private List<Review> reviews;

    public Place() {
    }


    public Place(String title, String longDescription, Map<String, Integer> ticketPrices) {
        this.title = title;
        this.longDescription = longDescription;
        this.ticketPrices = ticketPrices;
    }

    public Place(String title, String longDescription, List<String> mainImageUrl) {
        this.title = title;
        this.longDescription = longDescription;
        this.imageUrls = mainImageUrl;
    }

    public Place(String title, String longDescription) {
        this.title = title;
        this.longDescription = longDescription;
    }

    public Place(String title, String longDescription, List<String> mainImageUrl, Map<String, Integer> ticketPrices) {
        this.title = title;
        this.longDescription = longDescription;
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

    public String getLongDescription() {
        return longDescription;
    }

    public void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
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

    public Map<String, Integer> getTicketPrices() {
        return ticketPrices;
    }

    public void setTicketPrices(Map<String, Integer> ticketPrices) {
        this.ticketPrices = ticketPrices;
    }

    public Map<String, String> getOpeningHours() {
        return openingHours;
    }

    public void setOpeningHours(Map<String, String> openingHours) {
        this.openingHours = openingHours;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public String getDefaultImage() {
        return defaultImage;
    }

    public void setDefaultImage(String defaultImage) {
        this.defaultImage = defaultImage;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }
}
