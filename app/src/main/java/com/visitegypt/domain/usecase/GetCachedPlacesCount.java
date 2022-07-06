package com.visitegypt.domain.usecase;

import com.visitegypt.domain.repository.PlaceRepository;
import com.visitegypt.domain.usecase.base.SingleUseCase;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Single;

public class GetCachedPlacesCount extends SingleUseCase<Integer> {
    private PlaceRepository placeRepository;
    @Inject
    public GetCachedPlacesCount(PlaceRepository placeRepository) {
        this.placeRepository = placeRepository;
    }

    @Override
    protected Single<Integer> buildSingleUseCase() {
        return placeRepository.getCountOfPlaces();
    }
}
