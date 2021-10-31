package com.visitegypt.presentation.detailplace;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.textview.MaterialTextView;
import com.visitegypt.R;
import com.visitegypt.domain.model.Item;

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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Item currentItem = itemsArrayList.get(position);
        holder.itemDescriptionTextView.setText(currentItem.getDescription());
        holder.itemTitleTextView.setText(currentItem.getTitle());
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

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView itemImage;
        private MaterialTextView itemDescriptionTextView, itemTitleTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemImage = itemView.findViewById(R.id.itemCardImageView);
            itemDescriptionTextView = itemView.findViewById(R.id.itemCardDescriptionTextView);
            itemTitleTextView = itemView.findViewById(R.id.itemCardTitleTextView);
        }
    }
}
