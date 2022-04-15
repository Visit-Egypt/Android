package com.visitegypt.domain.usecase;

import com.visitegypt.domain.repository.PlaceRepository;
import com.visitegypt.domain.usecase.base.SingleUseCase;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.SchedulerRunnableIntrospection;

public class GetAllCitiesUseCase extends SingleUseCase<List<String>> {
    PlaceRepository placeRepository;
    @Inject
    public GetAllCitiesUseCase(PlaceRepository placeRepository) {
        this.placeRepository = placeRepository;
    }

    @Override
    protected Single<List<String>> buildSingleUseCase() {
        return placeRepository.getAllCities();
    }
}
