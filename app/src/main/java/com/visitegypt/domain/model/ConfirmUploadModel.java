package com.visitegypt.domain.model;

import androidx.room.TypeConverters;

import com.google.gson.annotations.SerializedName;
import com.visitegypt.domain.model.converters.ErrorKeysConverter;
import com.visitegypt.domain.model.converters.ImageKeysConverter;
import com.visitegypt.domain.model.converters.PostConverter;

import java.util.List;

public class ConfirmUploadModel {
    @TypeConverters(ImageKeysConverter.class)
    @SerializedName("images_keys")
    private List<String> imagesKeys;
    @TypeConverters(ErrorKeysConverter.class)
    @SerializedName("error_images")
    private List<String> errorKeys;
    @SerializedName("user_id")
    private String userId;

    public ConfirmUploadModel(List<String> imagesKeys, String userId) {
        this.imagesKeys = imagesKeys;
//        this.errorKeys = errorKeys;
        this.userId = userId;
    }

    public ConfirmUploadModel() {
    }

//    public List<String> getErrorKeys() {
//        return errorKeys;
//    }
//
//    public void setErrorKeys(List<String> errorKeys) {
//        this.errorKeys = errorKeys;
//    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<String> getImagesKeys() {
        return imagesKeys;
    }

    public void setImagesKeys(List<String> imagesKeys) {
        this.imagesKeys = imagesKeys;
    }
}
