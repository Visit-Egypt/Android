package com.visitegypt.domain.repository;

import com.visitegypt.domain.model.Place;
import com.visitegypt.domain.model.PlacePageResponse;

import io.reactivex.rxjava3.core.Single;

public interface PlaceRepository {
    // TODO compare to https://github.com/ZahraHeydari/Android-Clean-Architecture-MVVM-Hilt-RX/blob/master/app/src/main/java/com/android/artgallery/domain/repository/AlbumRepository.kt
    Single<PlacePageResponse> getAllPlaces();

    Single<Place> getPlaceById(String placeId);
}
