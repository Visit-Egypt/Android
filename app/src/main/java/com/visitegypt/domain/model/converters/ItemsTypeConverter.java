package com.visitegypt.domain.model.converters;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.visitegypt.domain.model.Item;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

public class ItemsTypeConverter {
    @TypeConverter
    public static List<Item> stringToItems(String data) {
        Gson gson = new Gson();
        if (data == null) {
            return Collections.emptyList();
        }
        Type listType = new TypeToken<List<Item>>() {
        }.getType();
        return gson.fromJson(data, listType);
    }

    @TypeConverter
    public static String itemsToString(List<Item> items) {
        Gson gson = new Gson();
        return gson.toJson(items);
    }
}
