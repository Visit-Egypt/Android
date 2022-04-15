package com.visitegypt.domain.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UploadedFilesResponse {
    @SerializedName("files_urls")
    private List<String> filesUrls;

    public List<String> getFilesUrls() {
        return filesUrls;
    }
}
