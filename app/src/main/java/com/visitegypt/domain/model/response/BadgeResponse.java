package com.visitegypt.domain.model.response;

import com.google.gson.annotations.SerializedName;
import com.visitegypt.domain.model.Badge;

import java.util.List;

public class BadgeResponse {
    @SerializedName("current_page")
    private int currentPage;
    @SerializedName("has_next")
    private boolean hasNext;
    private List<Badge> badges;

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public boolean hasNext() {
        return hasNext;
    }

    public void setHaveNext(boolean hasNext) {
        this.hasNext = hasNext;
    }

    public List<Badge> getBadges() {
        return badges;
    }

    public void setBadges(List<Badge> badges) {
        this.badges = badges;
    }
}
