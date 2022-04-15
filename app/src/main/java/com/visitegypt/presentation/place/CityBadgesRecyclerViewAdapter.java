package com.visitegypt.presentation.place;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jackandphantom.circularprogressbar.CircleProgressbar;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.visitegypt.R;
import com.visitegypt.domain.model.Badge;

import java.util.ArrayList;

public class CityBadgesRecyclerViewAdapter extends RecyclerView.Adapter<CityBadgesRecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "Badges Adapter In City";
    private ArrayList<Badge> badges;

    private Context context;

    public CityBadgesRecyclerViewAdapter(ArrayList<Badge> badges, Context context) {
        this.badges = badges;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.gamification_achievements_badges_card, parent, false);
        return new ViewHolder(view);
    }
//
//
//        holder.itemTitleTextView.setText(currentItem.getTitle());
//        holder.itemTitleTextView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                showDetail(view, currentItem);
//            }
//        });
//        holder.itemImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//            }
//        });
//        if (currentItem.getImageUrls() != null) {
//            if (!currentItem.getImageUrls().isEmpty()) {
//                Log.d(TAG, "found image for item: " + currentItem.getImageUrls().get(0).toString());
//                Glide.with(holder.itemView)
//                        .load(currentItem.getImageUrls().get(0))
//                        .fitCenter()
//                        .into(holder.itemImage);
//
//            } else {
//            }
//        } else {
//            holder.itemImage.setVisibility(View.GONE);
//        }
//
//    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        if (badges.get(position).isOwned()) {
            Log.d(TAG, "onBindViewHolder: owned badge");
            Log.d(TAG, "onBindViewHolder: badge max progress: " + badges.get(position).getMaxProgress());
            Log.d(TAG, "onBindViewHolder: badge progress: " + badges.get(position).getMaxProgress());
            holder.circleProgressbar.setMaxProgress(badges.get(position).getMaxProgress());
            holder.circleProgressbar.setProgress(badges.get(position).getMaxProgress());

            // badge owned => color
            Target target = new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    holder.circleProgressbar.setBackground(new BitmapDrawable(holder.circleProgressbar.getResources(), bitmap));
                }

                @Override
                public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            };
            Picasso.get().load(badges.get(position).getImageUrl()).into(target);
        } else {
            Log.d(TAG, "onBindViewHolder: unowned badge");
            Log.d(TAG, "onBindViewHolder: badge max progress: " + badges.get(position).getMaxProgress());
            Log.d(TAG, "onBindViewHolder: badge progress: " + badges.get(position).getProgress());
            holder.circleProgressbar.setMaxProgress(badges.get(position).getMaxProgress());
            holder.circleProgressbar.setProgress(badges.get(position).getProgress());

            // badge not owned => gray
            ColorMatrix colorMatrix = new ColorMatrix();
            colorMatrix.setSaturation(0);
            ColorMatrixColorFilter filter = new ColorMatrixColorFilter(colorMatrix);
            Target target = new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    Drawable drawable = new BitmapDrawable(holder.circleProgressbar.getResources(), bitmap);
                    drawable.setColorFilter(filter);
                    holder.circleProgressbar.setBackground(drawable);
                }

                @Override
                public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            };
            Picasso.get().load(badges.get(position).getImageUrl()).into(target);

        }
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


    public class ViewHolder extends RecyclerView.ViewHolder {

        private CircleProgressbar circleProgressbar;
        private TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            circleProgressbar = itemView.findViewById(R.id.badgeGamificationCardCircleProgressBar);
            textView = itemView.findViewById(R.id.badgeGamificationCardTitleTextView);

        }
    }
}