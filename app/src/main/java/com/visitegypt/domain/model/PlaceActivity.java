package com.visitegypt.domain.model;

import java.util.ArrayList;

public class PlaceActivity {
    // Types
    public static final String VISIT_LOCATION = "0", POST_STORY = "1", POST_POST = "2", ASK_CHAT_BOT = "3", POST_REVIEW = "4", GENERAL = "5", EXPLORE = "6";
    private String id;
    private int progress;
    boolean customXp = false;
    private int xp;
    private String type;
    private boolean finished;
    private String title;
    private String description;
    private int duration;
    private int maxProgress;

    private ArrayList<Integer> badgeIds;

    // logic DO NOT ADD TO SCHEME
    private boolean expanded = false;

    public PlaceActivity() {

    }

    public PlaceActivity(int xp, String type, String title, String description) {
        this.xp = xp;
        this.type = type;
        this.title = title;
        this.description = description;
        this.expanded = false;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public PlaceActivity(String id, int progress, boolean finished) {
        this.id = id;
        this.progress = progress;
        this.finished = finished;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = this.progress == 0 ? progress : this.progress;
    }

    public int getXp() {
        return xp;
    }

    public void setXp(int xp) {
        this.xp = this.xp == 0 ? xp : this.xp;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isFinished() {
        return getProgress() == getMaxProgress();
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

    public int getMaxProgress() {
//        if (!customXp) {
//            switch (type) {
//                case VISIT_LOCATION:
//                    return GamificationRules.XP_VISIT_LOCATION;
//                case POST_STORY:
//                    return GamificationRules.XP_POST_STORY;
//                case POST_POST:
//                    return GamificationRules.XP_POST_POST;
//                case POST_REVIEW:
//                    return GamificationRules.XP_POST_REVIEW;
//                case ASK_CHAT_BOT:
//                    return GamificationRules.XP_ASK_CHAT_BOT;
//                case GENERAL:
//                    return GamificationRules.XP_GENERAL;
//            }
//        }
        return maxProgress;
    }

    public void setMaxProgress(int maxProgress) {
        this.maxProgress = this.maxProgress == 0 ? maxProgress : this.maxProgress;
    }
}
