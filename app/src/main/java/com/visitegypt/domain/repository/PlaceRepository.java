package com.visitegypt.domain.repository;

import com.visitegypt.domain.model.Place;

import java.util.List;

import io.reactivex.rxjava3.core.Single;

public interface PlaceRepository {
    // TODO compare to https://github.com/ZahraHeydari/Android-Clean-Architecture-MVVM-Hilt-RX/blob/master/app/src/main/java/com/android/artgallery/domain/repository/AlbumRepository.kt
    Single<List<Place>> getAllPlaces();

    Single<Place> getPlaceById(String placeId);
}
