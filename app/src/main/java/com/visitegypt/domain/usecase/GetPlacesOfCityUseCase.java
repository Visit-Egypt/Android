package com.visitegypt.domain.usecase;

import com.visitegypt.domain.model.response.PlacePageResponse;
import com.visitegypt.domain.repository.PlaceRepository;
import com.visitegypt.domain.usecase.base.SingleUseCase;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Single;

public class GetPlacesOfCityUseCase extends SingleUseCase<PlacePageResponse> {
    PlaceRepository placeRepository;
    private String cityName;

    @Inject
    public GetPlacesOfCityUseCase(PlaceRepository placeRepository) {
        this.placeRepository = placeRepository;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    @Override
    protected Single<PlacePageResponse> buildSingleUseCase() {
        return placeRepository.getPlacesOfCity(cityName);
    }
}
