package com.visitegypt.domain.usecase.base;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.paging.PagingState;
import androidx.paging.rxjava3.RxPagingSource;

import com.visitegypt.domain.model.Place;
import com.visitegypt.domain.model.response.PlacePageResponse;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import io.reactivex.rxjava3.core.Single;

public abstract class PagingUseCase<T,R> extends RxPagingSource<Integer, T> {
    @Nullable
    @Override
    public Integer getRefreshKey(@NotNull PagingState<Integer, T> state) {
        Integer anchorPosition = state.getAnchorPosition();
        if (anchorPosition == null) {
            return null;
        }

        LoadResult.Page<Integer, T> anchorPage = state.closestPageToPosition(anchorPosition);
        if (anchorPage == null) {
            return null;
        }

        Integer prevKey = anchorPage.getPrevKey();
        if (prevKey != null) {
            return prevKey + 1;
        }

        Integer nextKey = anchorPage.getNextKey();
        if (nextKey != null) {
            return nextKey - 1;
        }

        return null;
    }

    protected abstract LoadResult<Integer, T> toLoadResult(@NonNull R response) ;
}
