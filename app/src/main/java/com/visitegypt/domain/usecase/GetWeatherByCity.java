package com.visitegypt.domain.usecase;

import com.visitegypt.domain.model.WeatherModel;
import com.visitegypt.domain.repository.WeatherUtilRepository;
import com.visitegypt.domain.usecase.base.SingleUseCase;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Single;

public class GetWeatherByCity extends SingleUseCase<WeatherModel> {
    private String city;
    private WeatherUtilRepository weatherUtilRepository;

    @Inject
    public GetWeatherByCity(WeatherUtilRepository weatherUtilRepository) {
        this.weatherUtilRepository = weatherUtilRepository;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Override
    protected Single<WeatherModel> buildSingleUseCase() {
        return weatherUtilRepository.getWeatherByCity(city);
    }
}
