package com.visitegypt.domain.usecase;

import androidx.annotation.NonNull;

import com.visitegypt.domain.model.Item;
import com.visitegypt.domain.model.response.ItemPageResponse;
import com.visitegypt.domain.repository.ItemRepository;
import com.visitegypt.domain.usecase.base.PagingUseCase;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class GetItemPagingUseCase extends PagingUseCase<Item, ItemPageResponse> {
    private ItemRepository itemRepository;
    private String placeId;

    @Inject
    public GetItemPagingUseCase(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public void setPlaceId(String placeId) {
         this.placeId = placeId;
    }



    @NonNull
    @Override
    public Single<LoadResult<Integer, Item>> loadSingle(@NonNull LoadParams<Integer> loadParams) {
        int page = loadParams.getKey() != null ? loadParams.getKey() : 1;
        return itemRepository.getItems(placeId,page)
                .subscribeOn(Schedulers.io())
                .map(itemPageResponse -> toLoadResult(itemPageResponse))
                .onErrorReturn(LoadResult.Error::new);

    }

    @Override
    protected LoadResult<Integer, Item> toLoadResult(@NonNull ItemPageResponse response) {
        int page = response.getCurrentPage();
        return new LoadResult.Page(response.getItems(), page == 1 ? null : page - 1, response.isHasNext() ? page + 1 : null);
    }
}
