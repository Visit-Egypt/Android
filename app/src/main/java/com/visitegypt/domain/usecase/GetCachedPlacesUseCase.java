package com.visitegypt.domain.usecase;


import static com.visitegypt.utils.Constants.MAXIMNumberOfCities;

import com.visitegypt.domain.model.Place;
import com.visitegypt.domain.model.response.PlacePageResponse;
import com.visitegypt.domain.repository.PlaceRepository;
import com.visitegypt.domain.usecase.base.SingleUseCase;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Single;

public class GetCachedPlacesUseCase extends SingleUseCase<PlacePageResponse> {
    private PlaceRepository placeRepository;

    @Inject
    public GetCachedPlacesUseCase(PlaceRepository placeRepository) {
        this.placeRepository = placeRepository;
    }


    @Override
    protected Single<PlacePageResponse> buildSingleUseCase() {

        return placeRepository.getALLCachedPlaces();

    }

}
