package com.visitegypt.domain.usecase;

import androidx.annotation.NonNull;

import com.visitegypt.domain.model.Place;
import com.visitegypt.domain.model.response.PlacePageResponse;
import com.visitegypt.domain.repository.PlaceRepository;
import com.visitegypt.domain.usecase.base.PagingUseCase;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class GetAllPlacesPagingUseCase extends PagingUseCase<Place,PlacePageResponse> {
    PlaceRepository placeRepository;
    @Inject
    public GetAllPlacesPagingUseCase(PlaceRepository placeRepository) {
        this.placeRepository = placeRepository;
    }



    @NonNull
    @Override
    public Single<LoadResult<Integer, Place>> loadSingle(@NonNull LoadParams<Integer> loadParams) {
        int page = loadParams.getKey() != null ? loadParams.getKey() : 1;
        return placeRepository.getPlacesPaging(page)
                .subscribeOn(Schedulers.io())
                .map(places -> toLoadResult(places))
                .onErrorReturn(LoadResult.Error::new);
    }

    @Override
    protected LoadResult<Integer, Place> toLoadResult(@NonNull PlacePageResponse response) {
        int page = response.getCurrentPage();
        return new LoadResult.Page(response.getPlaces(), page == 1 ? null : page - 1, response.isHasNext() ? page + 1 : null);
    }


}
