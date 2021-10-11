package com.visitegypt.domain.model;

public class Place {
    // TODO define the rest of the variables
    private String title;
    private String description;
    private String mainImageUrl;
    private int ticketPrice = -1;

    public Place() {
    }

    public Place(String title, String description, int ticketPrice) {
        this.title = title;
        this.description = description;
        this.ticketPrice = ticketPrice;
    }

    public Place(String title, String description, String mainImageUrl) {
        this.title = title;
        this.description = description;
        this.mainImageUrl = mainImageUrl;
    }

    public Place(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public Place(String title, String description, String mainImageUrl, int ticketPrice) {
        this.title = title;
        this.description = description;
        this.mainImageUrl = mainImageUrl;
        this.ticketPrice = ticketPrice;
    }

    // TODO make the rest of the setters and getters
    public String getName() {
        return title;
    }

    public void setName(String name) {
        this.title = name;
    }

    public int getTicketPrice() {
        return ticketPrice;
    }

    public void setTicketPrice(int ticketPrice) {
        this.ticketPrice = ticketPrice;
    }

}
