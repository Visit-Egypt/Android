package com.visitegypt.presentation.gamification;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textview.MaterialTextView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.visitegypt.R;
import com.visitegypt.domain.model.Hint;

import java.util.ArrayList;

import uk.co.senab.photoview.PhotoViewAttacher;

public class GamificationHintRecyclerViewAdapter extends RecyclerView.Adapter<GamificationHintRecyclerViewAdapter.ViewHolder> {

    private ArrayList<Hint> hints;

    public GamificationHintRecyclerViewAdapter(ArrayList<Hint> hints) {
        this.hints = hints;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.dialog_explore_hint_card, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Hint hint = hints.get(position);

        holder.hintNumberTextView.setText("Hint " + (position + 1));
        holder.hintTextView.setText(hint.getHint());

        if (hint.getImageUrl() != null && !hint.getImageUrl().isEmpty()) {
            holder.zoomableImageView.setVisibility(View.VISIBLE);
            Target target = new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    holder.zoomableImageView.setImageBitmap(bitmap);
                    PhotoViewAttacher photoViewAttacher = new PhotoViewAttacher(holder.zoomableImageView);
                    photoViewAttacher.update();
                }

                @Override
                public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            };
            Picasso.get().load(hint.getImageUrl()).into(target);
        } else {
            holder.zoomableImageView.setVisibility(View.GONE);
        }

        holder.expandableLayout.setVisibility(hint.isExpanded() ? View.VISIBLE : View.GONE);
        holder.arrowTextView.setText(hint.isExpanded() ? "↑" : "↓");
    }

    @Override
    public int getItemCount() {
        return hints.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private MaterialTextView hintNumberTextView, hintTextView, arrowTextView;
        private ImageView zoomableImageView;
        private LinearLayoutCompat expandableLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            hintNumberTextView = itemView.findViewById(R.id.dialogExploreHintNumberTextView);
            hintTextView = itemView.findViewById(R.id.dialogExploreHintTextView);
            arrowTextView = itemView.findViewById(R.id.dialogExploreHintArrowTextView);

            zoomableImageView = itemView.findViewById(R.id.dialogExploreHintZoomableImage);

            expandableLayout = itemView.findViewById(R.id.dialogExploreHintExpandableLayout);

            itemView.setOnClickListener(view -> {
                Hint hint = hints.get(getAdapterPosition());
                hint.setExpanded(!hint.isExpanded());
                notifyItemChanged(getAdapterPosition());
            });
        }
    }
}
