package com.visitegypt.domain.model;

import java.util.ArrayList;

public class Explore {
    private String title;
    private String imageUrl;
    private ArrayList<Hint> hints;

    public Explore(String title, String imageUrl) {
        this.title = title;
        this.imageUrl = imageUrl;
    }

    public Explore(String title, String imageUrl, ArrayList<Hint> hints) {
        this.title = title;
        this.imageUrl = imageUrl;
        this.hints = hints;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public ArrayList<Hint> getHints() {
        return hints;
    }

    public void setHints(ArrayList<Hint> hints) {
        this.hints = hints;
    }
}
