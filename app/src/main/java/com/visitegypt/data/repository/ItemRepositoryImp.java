package com.visitegypt.data.repository;

import com.visitegypt.data.source.remote.RetrofitService;
import com.visitegypt.domain.model.response.ItemPageResponse;
import com.visitegypt.domain.repository.ItemRepository;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Single;

public class ItemRepositoryImp implements ItemRepository {
    private RetrofitService retrofitService;

    @Inject
    public ItemRepositoryImp(RetrofitService retrofitService) {
        this.retrofitService = retrofitService;
    }

    @Override
    public Single<ItemPageResponse> getItemsWithPlaceId(String placeId) {
        String placeQuery = "{\"place_id\":\"" +placeId + "\"}";
        return retrofitService.getAllItems(placeQuery,1,10);
    }

    @Override
    public Single<ItemPageResponse> getItems(String placeId, int pageNumber) {
        String placeQuery = "{\"place_id\":\"" +placeId + "\"}";
        return retrofitService.getAllItems(placeQuery,pageNumber,10);
    }
}
