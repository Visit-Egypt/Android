package com.visitegypt.domain.usecase;

import com.visitegypt.domain.model.Place;
import com.visitegypt.domain.repository.PlaceRepository;
import com.visitegypt.domain.usecase.base.SingleUseCase;

import java.util.List;

import io.reactivex.rxjava3.core.Single;

public class GetPlacesUseCase extends SingleUseCase<List<Place>> {
    PlaceRepository placeRepository;

    public GetPlacesUseCase(PlaceRepository placeRepository) {
        this.placeRepository = placeRepository;

    }

    @Override
    protected Single buildSingleUseCase() {
        return placeRepository.getAllPlaces();
    }


    void execute() {

    }
    // TODO compare to https://github.com/ZahraHeydari/Android-Clean-Architecture-MVVM-Hilt-RX/blob/master/app/src/main/java/com/android/artgallery/domain/usecase/GetAlbumsUseCase.kt

}
