package com.visitegypt.domain.model.Converters;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.visitegypt.utils.Constants;

import java.lang.reflect.Type;
import java.util.Map;

public class OpeningHoursConverter {
    @TypeConverter
    public static Map<Constants.weekDays, String> fromString(String value) {
        Type mapType = new TypeToken<Map<Constants.weekDays, String>>() {
        }.getType();
        return new Gson().fromJson(value, mapType);
    }

    @TypeConverter
    public static String fromStringMap(Map<Constants.weekDays, String> map) {
        Gson gson = new Gson();
        return gson.toJson(map);
    }
}
