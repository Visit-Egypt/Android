package com.visitegypt.domain.usecase;

import com.visitegypt.domain.model.response.ItemPageResponse;
import com.visitegypt.domain.repository.ItemRepository;
import com.visitegypt.domain.usecase.base.SingleUseCase;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Single;

public class GetItemsUseCase extends SingleUseCase<ItemPageResponse> {
    private ItemRepository itemRepository;
    private String placeId;

    @Inject
    public GetItemsUseCase(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    @Override
    protected Single<ItemPageResponse> buildSingleUseCase() {
        return itemRepository.getItemsWithPlaceId(placeId);
    }
}
