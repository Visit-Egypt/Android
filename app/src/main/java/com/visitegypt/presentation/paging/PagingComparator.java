package com.visitegypt.presentation.paging;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.visitegypt.domain.model.Place;

public abstract class PagingComparator<T> extends DiffUtil.ItemCallback<T>{



    @SuppressLint("DiffUtilEquals")
    @Override
    public boolean areContentsTheSame(@NonNull T oldItem, @NonNull T newItem) {
         return oldItem.equals(newItem);
    }
}
