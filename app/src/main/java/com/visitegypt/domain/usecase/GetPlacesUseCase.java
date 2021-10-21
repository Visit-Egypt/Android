package com.visitegypt.domain.usecase;

import com.visitegypt.domain.model.PlacePageResponse;
import com.visitegypt.domain.repository.PlaceRepository;
import com.visitegypt.domain.usecase.base.SingleUseCase;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Single;

public class GetPlacesUseCase extends SingleUseCase<PlacePageResponse> {
    PlaceRepository placeRepository;

    //TODO migrate to dependency injection
    @Inject
    public GetPlacesUseCase(PlaceRepository placeRepository) {
        this.placeRepository = placeRepository;
    }

    @Override
    protected Single buildSingleUseCase() {
        return placeRepository.getAllPlaces();
    }

}