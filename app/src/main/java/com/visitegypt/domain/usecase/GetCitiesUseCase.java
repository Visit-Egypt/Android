package com.visitegypt.domain.usecase;

import com.visitegypt.domain.repository.PlaceRepository;
import com.visitegypt.domain.usecase.base.SingleUseCase;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Single;

public class GetCitiesUseCase extends SingleUseCase<List<String>> {

    private PlaceRepository placeRepository;

    @Inject
    public GetCitiesUseCase(PlaceRepository placeRepository) {
        this.placeRepository = placeRepository;
    }

    @Override
    protected Single<List<String>> buildSingleUseCase() {
        return placeRepository.getAllAvailableCities();
    }
}
