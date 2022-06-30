package com.visitegypt.domain.usecase;

import com.visitegypt.domain.model.Place;
import com.visitegypt.domain.model.Review;
import com.visitegypt.domain.model.response.PlacePageResponse;
import com.visitegypt.domain.repository.PlaceRepository;
import com.visitegypt.domain.usecase.base.SingleUseCase;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

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