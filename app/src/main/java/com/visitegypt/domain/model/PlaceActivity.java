package com.visitegypt.domain.model;

import java.util.ArrayList;

public class PlaceActivity {
    // Types
    public static final int VISIT_LOCATION = 0, POST_STORY = 1, POST_POST = 2, ASK_CHAT_BOT = 3, POST_REVIEW = 4, GENERAL = 5;

    private int progress;
    private int xp;
    private int type;
    private boolean finished;
    private String title;
    private String description;
    private int duration;

    private ArrayList<Integer> badgeIds;

    // logic DO NOT ADD TO SCHEME
    private boolean expanded = false;

    public PlaceActivity() {

    }

    public PlaceActivity(int xp, int type, String title, String description) {
        this.xp = xp;
        this.type = type;
        this.title = title;
        this.description = description;
        this.expanded = false;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public int getXp() {
        return xp;
    }

    public void setXp(int xp) {
        this.xp = xp;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
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

    public ArrayList<Integer> getBadgeIds() {
        return badgeIds;
    }

    public void setBadgeIds(ArrayList<Integer> badgeIds) {
        this.badgeIds = badgeIds;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }
}