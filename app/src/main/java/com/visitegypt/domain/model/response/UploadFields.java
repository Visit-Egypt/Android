package com.visitegypt.domain.model.response;

import com.google.gson.annotations.SerializedName;

public class UploadFields {
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
