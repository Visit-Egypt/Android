package com.visitegypt.domain.usecase;

import android.content.SharedPreferences;

import com.visitegypt.domain.model.RecommendationPlaces;
import com.visitegypt.domain.repository.PlaceRepository;
import com.visitegypt.domain.usecase.base.SingleUseCase;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Single;

public class GetRecommendedPlacesUseCase extends SingleUseCase<RecommendationPlaces> {
    private PlaceRepository placeRepository;

    @Inject
    public GetRecommendedPlacesUseCase(PlaceRepository placeRepository) {
        this.placeRepository = placeRepository;
    }

    @Override
    protected Single<RecommendationPlaces> buildSingleUseCase() {
        return placeRepository.getRecommendationPlaces();
    }
}
