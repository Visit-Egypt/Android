package com.visitegypt.domain.model.converters;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.visitegypt.domain.model.Hint;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

public class HintsConverter {

    @TypeConverter
    public static List<Hint> StringToHint(String data) {
        Gson gson = new Gson();
        if (data == null) {
            return Collections.emptyList();
        }
        Type listType = new TypeToken<List<String>>() {
        }.getType();
        return gson.fromJson(data, listType);
    }

    @TypeConverter
    public static String HintToString(List<Hint> hints) {
        Gson gson = new Gson();
        return gson.toJson(hints);
    }
}

