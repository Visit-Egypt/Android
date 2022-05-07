package com.visitegypt.presentation.detail.paging;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.paging.PagingDataAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.textview.MaterialTextView;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.visitegypt.R;
import com.visitegypt.domain.model.Item;
import com.visitegypt.presentation.detail.ItemsRecyclerViewAdapter;
import com.visitegypt.presentation.item.ItemActivity;


public class ItemPagingAdapter extends PagingDataAdapter<Item, ItemPagingAdapter.ItemPagingHolder> {

    private static final String TAG = "Items Adapter";
    private Context context;

    public ItemPagingAdapter(@NonNull DiffUtil.ItemCallback<Item> diffCallback) {
        super(diffCallback);
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ItemPagingHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.detail_item_card, parent, false);
        return new ItemPagingHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemPagingHolder holder, int position) {
        Item currentItem = getItem(position);

        holder.itemTitleTextView.setText(currentItem.getTitle());
        holder.itemTitleTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDetail(currentItem);
            }
        });
        holder.itemImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDetail(currentItem);
            }
        });
        if (currentItem.getImageUrls() != null) {
            if (!currentItem.getImageUrls().isEmpty()) {
                Log.d(TAG, "found image for item: " + currentItem.getImageUrls().get(0).toString());
                Glide.with(holder.itemView)
                        .load(currentItem.getImageUrls().get(0))
                        .fitCenter()
                        .into(holder.itemImage);

            } else {
            }
        } else {
            holder.itemImage.setVisibility(View.GONE);
        }
    }
    public void showDetail( Item currentItem) {

        Intent intent = new Intent(context, ItemActivity.class);

        intent.putStringArrayListExtra("images", currentItem.getImageUrls());
        intent.putExtra("title", currentItem.getTitle());
        intent.putExtra("place_id", currentItem.getPlaceId());
        intent.putExtra("description", currentItem.getDescription());
        context.startActivity(intent);
    }

    public class ItemPagingHolder extends RecyclerView.ViewHolder {
        private CircularImageView itemImage;
        private MaterialTextView itemTitleTextView;

        public ItemPagingHolder(@NonNull View itemView) {
            super(itemView);

            itemImage = itemView.findViewById(R.id.itemCardImageView);
            itemTitleTextView = itemView.findViewById(R.id.itemCardTitleTextView);
        }
    }
}
