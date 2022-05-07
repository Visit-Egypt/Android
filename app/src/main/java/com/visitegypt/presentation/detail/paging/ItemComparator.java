package com.visitegypt.presentation.detail.paging;

import androidx.annotation.NonNull;

import com.visitegypt.domain.model.Item;
import com.visitegypt.presentation.paging.PagingComparator;

public class ItemComparator extends PagingComparator<Item> {
    @Override
    public boolean areItemsTheSame(@NonNull Item oldItem, @NonNull Item newItem) {
        return oldItem.getPlaceId().equals(newItem.getPlaceId());
    }
}
