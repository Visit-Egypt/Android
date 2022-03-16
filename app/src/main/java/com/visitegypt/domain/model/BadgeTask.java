package com.visitegypt.domain.model;

public class BadgeTask {
    private String imageUrl;
    private String taskTitle;
    private int progress;
    private int maxProgress = 0;

    public BadgeTask(String imageUrl, String taskTitle, int maxProgress) {
        this.imageUrl = imageUrl;
        this.taskTitle = taskTitle;
        this.maxProgress = maxProgress;
    }

    public BadgeTask(String imageUrl, String taskTitle, int progress, int maxProgress) {
        this.imageUrl = imageUrl;
        this.taskTitle = taskTitle;
        this.progress = progress;
        this.maxProgress = maxProgress;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTaskTitle() {
        return taskTitle;
    }

    public void setTaskTitle(String taskTitle) {
        this.taskTitle = taskTitle;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public int getMaxProgress() {
        return maxProgress;
    }

    public void setMaxProgress(int maxProgress) {
        this.maxProgress = maxProgress;
    }

    public enum Type {
        VISIT_PLACE,
        REVIEW,
        CHAT_BOT,
        POST_POST,
        POST_STORY
    }
}
