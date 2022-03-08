package com.visitegypt.presentation.gamification;

import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.visitegypt.R;
import com.visitegypt.domain.model.Badge;

import java.util.ArrayList;

public class BadgesSliderViewAdapter extends RecyclerView.Adapter<BadgesSliderViewAdapter.SliderAdapterViewHolder> {
    private static final String TAG = "Badges RecyclerView adapter";
    private ArrayList<Badge> badges;

    public BadgesSliderViewAdapter(ArrayList<Badge> badges) {
        this.badges = badges;
    }

    @Override
    public int getItemCount() {
        if (badges == null)
            return 0;
        return badges.size();
    }

    public void setBadges(ArrayList<Badge> badges) {
        this.badges = badges;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SliderAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.detail_item_card, null, false);
        return new SliderAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SliderAdapterViewHolder viewHolder, final int position) {

        Badge sliderItem = badges.get(position);

        if (badges.get(position).isOwned()) {
            // badge owned => color
            Glide.with(viewHolder.itemView)
                    .load(sliderItem.getImageUrl())
                    .fitCenter()
                    .into(viewHolder.imageView);
        } else {
            // badge not owned => gray
            Glide.with(viewHolder.itemView)
                    .load(sliderItem.getImageUrl())
                    .apply(RequestOptions.circleCropTransform())
                    .into(viewHolder.imageView);

            ColorMatrix colorMatrix = new ColorMatrix();
            colorMatrix.setSaturation(0);
            ColorMatrixColorFilter filter = new ColorMatrixColorFilter(colorMatrix);
            viewHolder.imageView.setColorFilter(filter);

        }
    }

    public class SliderAdapterViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public SliderAdapterViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.itemCardImageView);
        }
    }
}
