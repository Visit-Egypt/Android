package com.visitegypt.domain.model;

public class Reward {

    private static final String TYPES[] = {
            "level",
            "activity",
            "place",
    };

    private String id;
    private String title;
    private String type;
    private String description;
    private int unlockLevel;
    private String unlockPlaceId;
    private String unlockActivity;

    public static String[] getTYPES() {
        return TYPES;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getUnlockLevel() {
        return unlockLevel;
    }

    public void setUnlockLevel(int unlockLevel) {
        this.unlockLevel = unlockLevel;
    }

    public String getUnlockPlaceId() {
        return unlockPlaceId;
    }

    public void setUnlockPlaceId(String unlockPlaceId) {
        this.unlockPlaceId = unlockPlaceId;
    }

    public String getUnlockActivity() {
        return unlockActivity;
    }

    public void setUnlockActivity(String unlockActivity) {
        this.unlockActivity = unlockActivity;
    }
}
