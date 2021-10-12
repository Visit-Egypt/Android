package com.visitegypt.domain.model.Converters;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.visitegypt.utils.Constants;

import java.lang.reflect.Type;
import java.util.Map;

public class TicketPricesConverter {
    @TypeConverter
    public static Map<Constants.customerType, Integer> fromString(String value) {
        Type mapType = new TypeToken<Map<Constants.customerType, Integer>>() {
        }.getType();
        return new Gson().fromJson(value, mapType);
    }

    @TypeConverter
    public static String fromStringMap(Map<Constants.customerType, Integer> map) {
        Gson gson = new Gson();
        return gson.toJson(map);
    }
}
