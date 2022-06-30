package com.visitegypt.domain.usecase;

import com.visitegypt.domain.model.Place;
import com.visitegypt.domain.repository.PlaceRepository;
import com.visitegypt.domain.usecase.base.CompletableUseCase;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Completable;

public class CachePlacesUseCase extends CompletableUseCase<Void> {
    private PlaceRepository placeRepository;
    private List<Place> places;

    @Inject
    public CachePlacesUseCase(PlaceRepository placeRepository) {
        this.placeRepository = placeRepository;
    }

    public void setPlaces(List<Place> places) {
        this.places = places;
    }

    @Override
    protected Completable buildCompletableUseCase() {
        return placeRepository.cachingPlaces(places);
    }
}

