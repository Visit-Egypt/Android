package com.visitegypt.domain.model;

import com.google.gson.annotations.SerializedName;

public class PreSignedURL {
    @SerializedName("url")
    private String url;
    @SerializedName("fields")
    private UploadFields uploadFields;
    @SerializedName("options")
    private OptionFields optionFields;

    public void setUrl(String url) {
        this.url = url;
    }

    public void setUploadFields(UploadFields uploadFields) {
        this.uploadFields = uploadFields;
    }

    public void setOptionFields(OptionFields optionFields) {
        this.optionFields = optionFields;
    }

    public String getUrl() {
        return url;
    }

    public UploadFields getUploadFields() {
        return uploadFields;
    }

    public OptionFields getOptionFields() {
        return optionFields;
    }


}
