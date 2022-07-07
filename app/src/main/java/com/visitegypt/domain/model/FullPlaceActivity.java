package com.visitegypt.domain.model;

import com.google.gson.annotations.SerializedName;

public class FullPlaceActivity {
    String id;
    int progress;
    boolean finished;

    @SerializedName("activity")
    PlaceActivity placeActivity;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public PlaceActivity getPlaceActivity() {
        return placeActivity;
    }

    public void setPlaceActivity(PlaceActivity placeActivity) {
        this.placeActivity = placeActivity;
    }
}
