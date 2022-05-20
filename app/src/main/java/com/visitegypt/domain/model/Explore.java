package com.visitegypt.domain.model;

import com.visitegypt.utils.GamificationRules;

import java.util.ArrayList;

public class Explore extends PlaceActivity {
    private String imageUrl;
    private ArrayList<Hint> hints;

    @Override
    public int getMaxProgress() {
        return 1;
    }

    @Override
    public String getType() {
        return PlaceActivity.EXPLORE;
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

    @Override
    public int getXp() {
        return GamificationRules.EXPLORE_XP;
    }
}
