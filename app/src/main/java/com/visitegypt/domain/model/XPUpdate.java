package com.visitegypt.domain.model;

import com.google.gson.annotations.SerializedName;

public class XPUpdate {
    @SerializedName("old_xp")
    private int oldXP;
    @SerializedName("new_xp")
    private int newXp;

    public XPUpdate() {
    }

    public XPUpdate(int oldXP, int newXp) {
        this.oldXP = oldXP;
        this.newXp = newXp;
    }

    public int getOldXP() {
        return oldXP;
    }

    public void setOldXP(int oldXP) {
        this.oldXP = oldXP;
    }

    public int getNewXp() {
        return newXp;
    }

    public void setNewXp(int newXp) {
        this.newXp = newXp;
    }
}
