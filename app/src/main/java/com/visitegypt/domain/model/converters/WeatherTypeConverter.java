package com.visitegypt.domain.model.converters;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.visitegypt.domain.model.Place;
import com.visitegypt.domain.model.WeatherModel;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class WeatherTypeConverter {
    @TypeConverter
    public static List<WeatherModel.Weather> stringToWeather(String data) {
        Gson gson = new Gson();
        if (data == null) {
            return Collections.emptyList();
        }
        Type listType = new TypeToken<List<WeatherModel.Weather>>() {
        }.getType();
        return gson.fromJson(data, listType);
    }

    @TypeConverter
    public static String weatherToString(List<WeatherModel.Weather> weathers) {
        Gson gson = new Gson();
        return gson.toJson(weathers);
    }
}
