package com.visitegypt.domain.usecase;

import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.visitegypt.data.source.local.dao.PlaceDao;
import com.visitegypt.data.source.local.dao.PlacePageResponseDao;
import com.visitegypt.domain.model.Place;
import com.visitegypt.domain.model.response.PlacePageResponse;
import com.visitegypt.domain.repository.PlaceRepository;
import com.visitegypt.domain.usecase.base.PagingUseCase;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class GetAllPlacesPagingWithRoom extends PagingUseCase<Place, PlacePageResponse> {
    private static final String TAG = "GetAllPlacesPagingWithRoom";
    PlaceRepository placeRepository;
    PlacePageResponseDao placePageResponseDao;

    @Inject
    public GetAllPlacesPagingWithRoom(PlaceRepository placeRepository,
                                      PlacePageResponseDao placePageResponseDao
    ) {
        this.placeRepository = placeRepository;
        this.placePageResponseDao = placePageResponseDao;
    }

    @NonNull
    @Override
    public Single<LoadResult<Integer, Place>> loadSingle(@NonNull LoadParams<Integer> loadParams) {
        int page = loadParams.getKey() != null ? loadParams.getKey() : 1;
        Single<PlacePageResponse> remoteSource = placeRepository.
                getPlacesPaging(page)
                .subscribeOn(Schedulers.io());
        return placePageResponseDao.getALLCachedPlaces(page)
                .subscribeOn(Schedulers.computation())
                .map(placePageResponse -> {
                    if (placePageResponse != null) {
                        Log.d(TAG, "loadSingle: placePageResponse");

                        return toLoadResult(placePageResponse);
                    }
                    return null;
                })
                .onErrorResumeNext(throwable -> {
                    return remoteSource.map(placePageResponse -> {
                        placePageResponseDao.insertAll(placePageResponse)
                                .subscribeOn(Schedulers.io())
                                .subscribe(() -> {
                                }, throwable1 -> {
                                });
                        return toLoadResult(placePageResponse);
                    });
                })
                .onErrorReturn(LoadResult.Error::new);

    }

    @Override
    protected LoadResult<Integer, Place> toLoadResult(@NonNull PlacePageResponse response) {
        int page = response.getCurrentPage();
        return new LoadResult.Page(response.getPlaces(), page == 1 ? null : page - 1, response.isHasNext() ? page + 1 : null);
    }


}
