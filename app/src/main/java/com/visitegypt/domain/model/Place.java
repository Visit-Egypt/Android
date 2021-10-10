package com.visitegypt.domain.model;

public class Place {
    // TODO define the rest of the variables
    private String name;
    private String ticketPrice;

    public Place(){}

    // TODO finish constructor
    public Place(String name, String ticketPrice) {
        this.name = name;
        this.ticketPrice = ticketPrice;
    }

    // TODO make the rest of the setters and getters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTicketPrice() {
        return ticketPrice;
    }

    public void setTicketPrice(String ticketPrice) {
        this.ticketPrice = ticketPrice;
    }

}
