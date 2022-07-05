package com.visitegypt.domain.model;

public class UserTitle {
    boolean passed;
    private int level;
    private String Title;

    public UserTitle(int level, String title, boolean passed) {
        this.level = level;
        Title = title;
        this.passed = passed;
    }

    public UserTitle(int level, String title) {
        this.level = level;
        Title = title;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public boolean isPassed() {
        return passed;
    }

    public void setPassed(boolean passed) {
        this.passed = passed;
    }
}
