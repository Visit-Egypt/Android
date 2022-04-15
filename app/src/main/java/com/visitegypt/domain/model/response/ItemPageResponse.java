package com.visitegypt.domain.model.response;

import com.google.gson.annotations.SerializedName;
import com.visitegypt.domain.model.Item;

import java.util.List;

public class ItemPageResponse {
    @SerializedName("current_page")
    private int currentPage;
    @SerializedName("has_next")
    private boolean hasNext;
    private List<Item> items;

    public ItemPageResponse() {

    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public boolean isHasNext() {
        return hasNext;
    }

    public void setHasNext(boolean hasNext) {
        this.hasNext = hasNext;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }
}
