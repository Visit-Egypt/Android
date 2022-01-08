package com.visitegypt.domain.model.response;

import com.google.gson.annotations.SerializedName;

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


class  UploadFields {
    @SerializedName("acl")
    private String acl;
    @SerializedName("Content-Type")
    private String contentType;
    @SerializedName("key")
    private String key;
    @SerializedName("AWSAccessKeyId")
    private String AWSAccessKeyId;
    @SerializedName("policy")
    private String policy;
    @SerializedName("signature")
    private String signature;

}