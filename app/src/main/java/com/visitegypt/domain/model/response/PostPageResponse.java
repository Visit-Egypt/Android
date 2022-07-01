package com.visitegypt.domain.model.response;

import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.google.gson.annotations.SerializedName;
import com.visitegypt.domain.model.Post;
import com.visitegypt.domain.model.converters.PostConverter;

import java.util.List;

public class PostPageResponse {
    @SerializedName("current_page")
    int currentPage;
    @SerializedName("has_next")
    Boolean hasNext;
    @TypeConverters(PostConverter.class)
    @SerializedName("posts")
    List<Post> posts;
    @PrimaryKey
    private String id;

    public PostPageResponse() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public Boolean getHasNext() {
        return hasNext;
    }

    public void setHasNext(Boolean hasNext) {
        this.hasNext = hasNext;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }
}
