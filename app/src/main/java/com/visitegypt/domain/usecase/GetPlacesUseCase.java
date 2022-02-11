package com.visitegypt.domain.usecase;

import com.visitegypt.domain.model.Review;
import com.visitegypt.domain.model.response.PlacePageResponse;
import com.visitegypt.domain.repository.PlaceRepository;
import com.visitegypt.domain.usecase.base.SingleUseCase;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Single;

public class GetPlacesUseCase extends SingleUseCase<PlacePageResponse> {
    PlaceRepository placeRepository;

    @Inject
    public GetPlacesUseCase(PlaceRepository placeRepository) {
        this.placeRepository = placeRepository;
    }


    @Override
    protected Single<PlacePageResponse> buildSingleUseCase() {
        return placeRepository.getAllPlaces();

    }
}