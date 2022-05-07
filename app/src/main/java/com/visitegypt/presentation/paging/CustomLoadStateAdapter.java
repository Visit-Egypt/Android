package com.visitegypt.presentation.paging;

import android.view.View;
import android.view.ViewGroup;

import androidx.paging.LoadState;
import androidx.paging.LoadStateAdapter;

import org.jetbrains.annotations.NotNull;

public class CustomLoadStateAdapter extends LoadStateAdapter<LoadStateViewHolder> {
    private View.OnClickListener mRetryCallback;

    public CustomLoadStateAdapter(View.OnClickListener retryCallback) {
        mRetryCallback = retryCallback;
    }

    @NotNull
    @Override
    public LoadStateViewHolder onCreateViewHolder(@NotNull ViewGroup parent,
                                                  @NotNull LoadState loadState) {
        return new LoadStateViewHolder(parent, mRetryCallback);
    }

    @Override
    public void onBindViewHolder(@NotNull LoadStateViewHolder holder,
                                 @NotNull LoadState loadState) {
        holder.bind(loadState);
    }
}
