package com.visitegypt.domain.model;

public class Hint {
    private String hint;
    private String imageUrl;

    // DO NOT ADD TO BACKEND
    private boolean expanded = false;

    public Hint(String hint, String imageUrl) {
        this.hint = hint;
        this.imageUrl = imageUrl;
    }

    public Hint() {
    }

    public Hint(String hint) {
        this.hint = hint;
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }
}
