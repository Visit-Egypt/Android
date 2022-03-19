package com.visitegypt.domain.model;

import android.util.Log;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Badge {
    private static final String TAG = "badge model";

    private String id;
    private int progress;
    @SerializedName("image_url")
    private String imageUrl;
    @SerializedName("max_progress")
    private int maxProgress;
    private String title;
    private boolean owned;
    private int type;
    private int xp;
    private String description;
    @SerializedName("badge_tasks")
    private ArrayList<BadgeTask> badgeTasks;
    @SerializedName("badge_frame_image_url")
    private boolean pinned;
    private String placeId;

    @Deprecated
    private String badgeFrameUrl;

    public Badge(String id, String imageUrl, boolean owned, Type type, int xp) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.owned = owned;
        this.type = type.ordinal();
        this.xp = xp;
    }

    public String getTitle() {
        return title;
    }

    public Badge(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Badge() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isOwned() {
        return owned;
    }

    public void setOwned(boolean owned) {
        this.owned = owned;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getXp() {
        return xp;
    }

    public void setXp(int xp) {
        this.xp = xp;
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

    public int getProgress() {
        if (badgeTasks != null) {
            for (int i = 0; i < badgeTasks.size(); i++) {
                progress += badgeTasks.get(i).getProgress();
            }
        }
        return progress;
    }

    @Deprecated
    public void setProgress(int progress) {
        Log.w(TAG, "progress is automatically generated from badge tasks, don't use it");
        this.progress = progress;
    }

    public int getMaxProgress() {
        if (badgeTasks != null) {
            for (int i = 0; i < badgeTasks.size(); i++) {
                maxProgress += badgeTasks.get(i).getMaxProgress();
            }
        }
        return maxProgress;
    }

    @Deprecated
    public void setMaxProgress(int maxProgress) {
        Log.w(TAG, "maxProgress is automatically generated from badge tasks, don't use it");
        this.maxProgress = maxProgress;
    }

    public ArrayList<BadgeTask> getBadgeTasks() {
        return badgeTasks;
    }

    public void setBadgeTasks(ArrayList<BadgeTask> badgeTasks) {
        this.badgeTasks = badgeTasks;
    }

    public boolean isPinned() {
        return pinned;
    }

    public void setPinned(boolean pinned) {
        this.pinned = pinned;
    }

    public String getBadgeFrameUrl() {
        return badgeFrameUrl;
    }

    public void setBadgeFrameUrl(String badgeFrameUrl) {
        this.badgeFrameUrl = badgeFrameUrl;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public enum Type {
        PLACE,
        CITY,
        REGION,
        GENERAL,
    }
}
