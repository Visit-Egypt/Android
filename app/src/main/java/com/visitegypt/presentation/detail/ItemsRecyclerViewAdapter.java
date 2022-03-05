package com.visitegypt.presentation.detail;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.textview.MaterialTextView;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.visitegypt.R;
import com.visitegypt.domain.model.Item;
import com.visitegypt.presentation.item.ItemActivity;

import java.util.List;

public class ItemsRecyclerViewAdapter extends RecyclerView.Adapter<ItemsRecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "Items Adapter";
    private List<Item> itemsArrayList;

    private Context context;

    public ItemsRecyclerViewAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.detail_item_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Item currentItem = itemsArrayList.get(position);

        holder.itemTitleTextView.setText(currentItem.getTitle());
        holder.itemTitleTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDetail(view, currentItem);
            }
        });
        holder.itemImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDetail(view, currentItem);
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
                holder.itemImage.setVisibility(View.GONE);
            }
        } else {
            holder.itemImage.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        if (itemsArrayList == null)
            return 0;
        return itemsArrayList.size();
    }

    public void setItemsArrayList(List<Item> itemsArrayList) {
        this.itemsArrayList = itemsArrayList;
        notifyDataSetChanged();
    }

    public void showDetail(View view, Item currentItem) {
        Context contextt;
        contextt = view.getContext();
        Intent intent = new Intent(contextt, ItemActivity.class);

        intent.putStringArrayListExtra("images", currentItem.getImageUrls());
        intent.putExtra("title", currentItem.getTitle());
        intent.putExtra("place_id", currentItem.getPlaceId());
        intent.putExtra("description", currentItem.getDescription());
        contextt.startActivity(intent);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private CircularImageView itemImage;
        private MaterialTextView itemDescriptionTextView, itemTitleTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            itemImage = itemView.findViewById(R.id.itemCardImageView);
            itemTitleTextView = itemView.findViewById(R.id.itemCardTitleTextView);

        }
    }
}