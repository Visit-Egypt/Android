package com.visitegypt.domain.model.converters;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.visitegypt.domain.model.PlaceActivity;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class PlaceActivityConverter {
    @TypeConverter
    public static ArrayList<PlaceActivity> StringToPlaceActivity(String data) {
        Gson gson = new Gson();
        if (data == null) {
            return new ArrayList<>();
        }
        Type listType = new TypeToken<List<String>>() {
        }.getType();
        return gson.fromJson(data, listType);
    }

    @TypeConverter
    public static String HintToString(ArrayList<PlaceActivity> placeActivities) {
        Gson gson = new Gson();
        return gson.toJson(placeActivities);
    }
}
