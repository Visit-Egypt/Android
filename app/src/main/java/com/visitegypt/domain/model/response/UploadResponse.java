package com.visitegypt.domain.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UploadResponse {
    @SerializedName("url")
    private String url;
    @SerializedName("fields")
    private UploadFields fields;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public UploadFields getFields() {
        return fields;
    }

    public void setFields(UploadFields fields) {
        this.fields = fields;
    }

}


