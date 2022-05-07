package com.visitegypt.presentation.home.child.discover.paging;

import androidx.annotation.NonNull;

import com.visitegypt.domain.model.Place;
import com.visitegypt.presentation.paging.PagingComparator;

public class PlaceserComparator extends PagingComparator<Place> {

    @Override
    public boolean areItemsTheSame(@NonNull Place oldItem, @NonNull Place newItem) {
        return oldItem.getId().equals(newItem.getId());
    }
}
