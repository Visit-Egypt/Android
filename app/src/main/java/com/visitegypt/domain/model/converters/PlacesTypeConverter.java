package com.visitegypt.domain.model.converters;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.visitegypt.domain.model.Item;
import com.visitegypt.domain.model.Place;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

public class PlacesTypeConverter {
    @TypeConverter
    public static List<Place> stringToPlaces(String data) {
        Gson gson = new Gson();
        if (data == null) {
            return Collections.emptyList();
        }
        Type listType = new TypeToken<List<Place>>() {
        }.getType();
        return gson.fromJson(data, listType);
    }

    @TypeConverter
    public static String placeToString(List<Place> places) {
        Gson gson = new Gson();
        return gson.toJson(places);
    }
}
