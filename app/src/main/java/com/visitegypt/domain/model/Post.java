package com.visitegypt.domain.model;

import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Post {
    public Post(String caption, List<String> listOfImages, String placeId, String userId, String userName, List<String> likes) {
        this.caption = caption;
        this.listOfImages = listOfImages;
        this.placeId = placeId;
        this.userId = userId;
        this.userName = userName;
        this.likes = likes;
    }
    public Post(String caption, List<String> listOfImages, String placeId, String userId, String userName) {
        this.caption = caption;
        this.listOfImages = listOfImages;
        this.placeId = placeId;
        this.userId = userId;
        this.userName = userName;
    }
    public Post() {
    }
    /*****************************************************************************************************/
    @PrimaryKey
    private String id;
    private String caption;
    @SerializedName("list_of_images")
    private List<String> listOfImages;
    @SerializedName("place_id")
    private String placeId;
    @SerializedName("user_id")
    private String userId;
    @SerializedName("user_name")
    private String userName;
    @SerializedName("likes")
    private List<String> likes;
    /*****************************************************************************/
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public List<String> getListOfImages() {
        return listOfImages;
    }

    public void setListOfImages(List<String> listOfImages) {
        this.listOfImages = listOfImages;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public List<String> getLikes() {
        return likes;
    }

    public void setLikes(List<String> likes) {
        this.likes = likes;
    }
    /******************************************************************************************/

}
