package com.visitegypt.domain.model;

import com.google.gson.annotations.SerializedName;

public class ARResponsePage {
    @SerializedName("ar_obj")
    String arObj;
    @SerializedName("ar_png")
    String arPng;
    @SerializedName("ar_mtl")
    String arMtl;

    public ARResponsePage() {
    }

    public String getArObj() {
        return arObj;
    }

    public void setArObj(String arObj) {
        this.arObj = arObj;
    }

    public String getArPng() {
        return arPng;
    }

    public void setArPng(String arPng) {
        this.arPng = arPng;
    }

    public String getArMtl() {
        return arMtl;
    }

    public void setArMtl(String arMtl) {
        this.arMtl = arMtl;
    }
}
