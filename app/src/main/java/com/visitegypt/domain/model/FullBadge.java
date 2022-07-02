package com.visitegypt.domain.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FullBadge {

    Badge badge;
    int progress;
    boolean owned;
    boolean pinned;
    @SerializedName("badge_tasks")
    List<BadgeTask> badgeTasks;

    public Badge getBadge() {
        return badge;
    }

    public void setBadge(Badge badge) {
        this.badge = badge;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public boolean isOwned() {
        return owned;
    }

    public void setOwned(boolean owned) {
        this.owned = owned;
    }

    public boolean isPinned() {
        return pinned;
    }

    public void setPinned(boolean pinned) {
        this.pinned = pinned;
    }

    public List<BadgeTask> getBadgeTasks() {
        return badgeTasks;
    }

    public void setBadgeTasks(List<BadgeTask> badgeTasks) {
        this.badgeTasks = badgeTasks;
    }
}
