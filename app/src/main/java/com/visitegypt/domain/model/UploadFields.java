package com.visitegypt.domain.model;

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

    public String getAcl() {
        return acl;
    }

    public void setAcl(String acl) {
        this.acl = acl;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getAWSAccessKeyId() {
        return AWSAccessKeyId;
    }

    public void setAWSAccessKeyId(String AWSAccessKeyId) {
        this.AWSAccessKeyId = AWSAccessKeyId;
    }

    public String getPolicy() {
        return policy;
    }

    public void setPolicy(String policy) {
        this.policy = policy;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

}
