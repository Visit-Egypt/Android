package com.visitegypt.domain.usecase;

import com.visitegypt.domain.repository.PlaceRepository;
import com.visitegypt.domain.usecase.base.SingleUseCase;

import io.reactivex.rxjava3.core.Single;

public class GetPlaceDetailUseCase extends SingleUseCase {
    PlaceRepository placeRepository;
    String placeId;

    public GetPlaceDetailUseCase(PlaceRepository placeRepository, String placeId) {
        this.placeRepository = placeRepository;
        this.placeId = placeId;
    }

    @Override
    protected Single buildSingleUseCase() {
        return placeRepository.getPlaceById(placeId);
    }
}