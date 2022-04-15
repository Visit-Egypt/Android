package com.visitegypt.domain.model.converters;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.visitegypt.domain.model.Post;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

public class PostConverter {
    @TypeConverter
    public static List<Post> StringToPost(String data) {
        Gson gson = new Gson();
        if (data == null) {
            return Collections.emptyList();
        }
        Type listType = new TypeToken<List<Post>>() {
        }.getType();
        return gson.fromJson(data, listType);
    }

    @TypeConverter
    public static String PostToString(List<Post> posts) {
        Gson gson = new Gson();
        return gson.toJson(posts);
    }
}
