package com.visitegypt.domain.model.converters;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.visitegypt.domain.model.Review;
import com.visitegypt.domain.model.SearchPlace;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

public class SearchPlaceConverter {
    @TypeConverter
    public static List<SearchPlace> stringToSearchPlace(String data) {
        Gson gson = new Gson();
        if (data == null) {
            return Collections.emptyList();
        }
        Type listType = new TypeToken<List<SearchPlace>>() {
        }.getType();
        return gson.fromJson(data, listType);
    }

    @TypeConverter
    public static String searchPlaceToString(List<SearchPlace> searchPlaces) {
        Gson gson = new Gson();
        return gson.toJson(searchPlaces);
    }
}
