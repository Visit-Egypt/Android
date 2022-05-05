package com.visitegypt.domain.usecase;

import com.visitegypt.domain.model.Place;
import com.visitegypt.domain.repository.PlaceRepository;
import com.visitegypt.domain.usecase.base.SingleUseCase;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Single;

public class GetPlacesByPlaceActivityIdUseCase extends SingleUseCase<List<Place>> {

    private List<String> placeActivitiesIds;

    private PlaceRepository placeRepository;

    @Inject
    public GetPlacesByPlaceActivityIdUseCase(PlaceRepository placeRepository) {
        this.placeRepository = placeRepository;
    }

    @Override
    protected Single<List<Place>> buildSingleUseCase() {
        return placeRepository.getPlacesByPlaceActivityIds(placeActivitiesIds);
    }

    public void setPlaceActivities(List<String> placeActivities) {
        this.placeActivitiesIds = placeActivities;
    }
}
