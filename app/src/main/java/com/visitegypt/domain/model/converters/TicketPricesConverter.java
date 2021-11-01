package com.visitegypt.domain.model.converters;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Map;

public class TicketPricesConverter {
    @TypeConverter
    public static Map<String, Integer> fromString(String value) {
        Type mapType = new TypeToken<Map<String, Integer>>() {
        }.getType();
        return new Gson().fromJson(value, mapType);
    }

    @TypeConverter
    public static String fromStringMap(Map<String, Integer> map) {
        Gson gson = new Gson();
        return gson.toJson(map);
    }
}
