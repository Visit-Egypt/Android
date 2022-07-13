package com.visitegypt.domain.repository;

import com.visitegypt.domain.model.WeatherModel;

import io.reactivex.rxjava3.core.Single;

public interface WeatherUtilRepository {
    Single<WeatherModel> getWeatherByCity(String city);
    Single<WeatherModel> getWeatherByLocation(double lat, double lon);
}
