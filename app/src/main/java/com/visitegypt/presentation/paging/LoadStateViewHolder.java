package com.visitegypt.presentation.paging;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.paging.LoadState;
import androidx.recyclerview.widget.RecyclerView;

import com.visitegypt.R;
import com.visitegypt.databinding.LoadStateItemBinding;

class LoadStateViewHolder extends RecyclerView.ViewHolder {
    private ProgressBar mProgressBar;
    private TextView mErrorMsg;
    private Button mRetry;

    LoadStateViewHolder(
            @NonNull ViewGroup parent,
            @NonNull View.OnClickListener retryCallback) {
        super(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.load_state_item, parent, false));

        LoadStateItemBinding binding = LoadStateItemBinding.bind(itemView);
        mProgressBar = binding.progressBar;
        mErrorMsg = binding.errorMsg;
        mRetry = binding.retryButton;
    }

    public void bind(LoadState loadState) {
        if (loadState instanceof LoadState.Error) {
            LoadState.Error loadStateError = (LoadState.Error) loadState;
            mErrorMsg.setText(loadStateError.getError().getLocalizedMessage());
        }
        mProgressBar.setVisibility(loadState instanceof LoadState.Loading
                ? View.VISIBLE : View.GONE);
        mRetry.setVisibility(loadState instanceof LoadState.Error
                ? View.VISIBLE : View.GONE);
        mErrorMsg.setVisibility(loadState instanceof LoadState.Error
                ? View.VISIBLE : View.GONE);
    }
}