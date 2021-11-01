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
        Map<String, String> map = new HashMap();
        map.put("place_id", placeId);
        return retrofitService.getAllItems(map);
    }
}
