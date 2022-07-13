package com.visitegypt.data.repository;

import static com.visitegypt.utils.Constants.WEATHER_API_KEY;

import com.visitegypt.data.source.remote.RetrofitService;
import com.visitegypt.domain.model.WeatherModel;
import com.visitegypt.domain.repository.WeatherUtilRepository;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.rxjava3.core.Single;

public class WeatherUtilRepositoryImp implements WeatherUtilRepository {
    private final RetrofitService retrofitService;

    @Inject
    public WeatherUtilRepositoryImp(@Named("Weather") RetrofitService retrofitService) {
        this.retrofitService = retrofitService;
    }

    @Override
    public Single<WeatherModel> getWeatherByCity(String city) {
        return retrofitService.getWeatherByCity(city,WEATHER_API_KEY);
    }

    @Override
    public Single<WeatherModel> getWeatherByLocation(double lat, double lon) {
        return retrofitService.getWeatherByLocation(lat,lon,WEATHER_API_KEY);
    }
}
