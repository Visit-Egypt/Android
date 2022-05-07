package com.visitegypt.domain.repository;

import com.visitegypt.domain.model.response.ItemPageResponse;

import io.reactivex.rxjava3.core.Single;

public interface ItemRepository {
    Single<ItemPageResponse> getItemsWithPlaceId(String placeId);
    Single<ItemPageResponse> getItems(String placeId,int pageNumber);
}
