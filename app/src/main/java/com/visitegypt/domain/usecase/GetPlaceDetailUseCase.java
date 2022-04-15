package com.visitegypt.domain.usecase;

import com.visitegypt.domain.model.Place;
import com.visitegypt.domain.model.Review;
import com.visitegypt.domain.repository.PlaceRepository;
import com.visitegypt.domain.usecase.base.SingleUseCase;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Single;

public class GetPlaceDetailUseCase extends SingleUseCase<Place> {
    private PlaceRepository placeRepository;
    private String placeId;

    @Inject
    public GetPlaceDetailUseCase(PlaceRepository placeRepository) {
        this.placeRepository = placeRepository;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    @Override
    protected Single<Place> buildSingleUseCase() {
        return placeRepository.getPlaceById(placeId);
    }
}