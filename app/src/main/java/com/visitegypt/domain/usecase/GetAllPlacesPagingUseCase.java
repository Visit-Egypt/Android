package com.visitegypt.domain.usecase;

import androidx.annotation.NonNull;

import com.visitegypt.data.source.local.dao.PlaceDao;
import com.visitegypt.domain.model.Place;
import com.visitegypt.domain.model.response.PlacePageResponse;
import com.visitegypt.domain.repository.PlaceRepository;
import com.visitegypt.domain.usecase.base.PagingUseCase;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class GetAllPlacesPagingUseCase extends PagingUseCase<Place, PlacePageResponse> {
    PlaceRepository placeRepository;
    PlaceDao placeDao;

    @Inject
    public GetAllPlacesPagingUseCase(PlaceRepository placeRepository, PlaceDao placeDao) {
        this.placeRepository = placeRepository;
        this.placeDao = placeDao;
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
        placeDao.insert(response.getPlaces());
        return new LoadResult.Page(response.getPlaces(), page == 1 ? null : page - 1, response.isHasNext() ? page + 1 : null);
    }

    public Single<List<Place>> getAllCachedPlaces() {
        return placeDao
                .getALLCachedPlaces ()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


}
