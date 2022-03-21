package com.visitegypt.domain.model.response;

import com.google.gson.annotations.SerializedName;
import com.visitegypt.domain.model.Badge;
import com.visitegypt.domain.model.Item;

import java.util.List;

public class BadgeResponse {
    @SerializedName("current_page")
    private int currentPage;
    @SerializedName("has_next")
    private boolean hasNext;
    private List<Badge> badges;

}
