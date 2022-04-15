package com.visitegypt.domain.model;

import com.google.gson.annotations.SerializedName;

public class OptionFields {
    @SerializedName("file_size")
    private String fileSize;
    @SerializedName("presigned_url_interval")
    private String preSignedUrlInterval;

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public String getPreSignedUrlInterval() {
        return preSignedUrlInterval;
    }

    public void setPreSignedUrlInterval(String preSignedUrlInterval) {
        this.preSignedUrlInterval = preSignedUrlInterval;
    }
}
