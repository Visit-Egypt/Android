package com.visitegypt.domain.model;

import com.google.gson.annotations.SerializedName;

public class FullExplore {

    String id;
    boolean finished;
    int progress;

    @SerializedName("activity")
    Explore explore;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public Explore getExplore() {
        return explore;
    }

    public void setExplore(Explore explore) {
        this.explore = explore;
    }
}
