package com.visitegypt.presentation.detail.paging;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.paging.PagingDataAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.textview.MaterialTextView;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.smarteist.autoimageslider.SliderView;
import com.visitegypt.R;
import com.visitegypt.domain.model.Item;
import com.visitegypt.domain.model.Slider;
import com.visitegypt.presentation.detail.SliderAdapter;
import com.visitegypt.presentation.item.ItemActivity;

import java.util.ArrayList;


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
        holder.itemTitleTextView.setOnClickListener(view -> showDetail(currentItem));
        holder.itemImage.setOnClickListener(view -> showDetail(currentItem));
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

    public void showDetail(Item currentItem) {
        Intent intent = new Intent(context, ItemActivity.class);


        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.activity_item);

        TextView itemTitleTextView = dialog.findViewById(R.id.itemTitleTextView);
        TextView itemDescriptionTextView = dialog.findViewById(R.id.itemDescriptionTextView);
        SliderView sliderView = dialog.findViewById(R.id.sliderSliderView);
        ArrayList<Slider> sliderArrayList = new ArrayList<>();
        SliderAdapter sliderAdapter = new SliderAdapter(sliderArrayList);
        sliderView.setAutoCycleDirection(SliderView.LAYOUT_DIRECTION_LTR);
        sliderView.setSliderAdapter(sliderAdapter);
        sliderView.setScrollTimeInSec(3);
        sliderView.setAutoCycle(true);
        sliderView.startAutoCycle();

        itemTitleTextView.setText(currentItem.getTitle());

        itemDescriptionTextView.setText(currentItem.getDescription());

        ArrayList<String> images = currentItem.getImageUrls();

        if (images != null) {
            for (int i = 0; i < images.size(); i++) {
                sliderArrayList.add(new Slider(images.get(i)));
            }
            sliderAdapter.updateArrayList(sliderArrayList);
        } else {
            sliderView.setVisibility(View.GONE);
        }
        dialog.show();
        dialog.getWindow().setLayout(RecyclerView.LayoutParams.MATCH_PARENT,
                RecyclerView.LayoutParams.MATCH_PARENT);


//
//        intent.putStringArrayListExtra("images", currentItem.getImageUrls());
//        intent.putExtra("title", currentItem.getTitle());
//        intent.putExtra("place_id", currentItem.getPlaceId());
//        intent.putExtra("description", currentItem.getDescription());


        //context.startActivity(intent);
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
