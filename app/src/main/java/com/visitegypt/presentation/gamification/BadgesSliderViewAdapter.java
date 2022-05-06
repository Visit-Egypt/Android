package com.visitegypt.presentation.gamification;

import android.app.Dialog;
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
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textview.MaterialTextView;
import com.google.gson.Gson;
import com.jackandphantom.circularprogressbar.CircleProgressbar;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.visitegypt.R;
import com.visitegypt.domain.model.Badge;

import java.util.ArrayList;

public class BadgesSliderViewAdapter extends RecyclerView.Adapter<BadgesSliderViewAdapter.SliderAdapterViewHolder> {
    private static final String TAG = "Badges RecyclerView adapter";
    private ArrayList<Badge> badges;
    private Context context;

    public BadgesSliderViewAdapter(ArrayList<Badge> badges, Context context) {
        this.badges = badges;
        this.context = context;
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

    public void addBadge(ArrayList<Badge> badges) {
        this.badges = badges;
        notifyItemChanged(getItemCount());
    }

    @NonNull
    @Override
    public SliderAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.gamification_achievements_badges_card, parent, false);
        return new SliderAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SliderAdapterViewHolder viewHolder, final int position) {
        viewHolder.linearLayout.setOnClickListener(view -> showBadgeDialog(badges.get(position)));
        
        viewHolder.textView.setText(badges.get(position).getTitle());

        if (badges.get(position).isOwned()) {
            Log.d(TAG, "onBindViewHolder: owned badge");
            Log.d(TAG, "onBindViewHolder: badge max progress: " + badges.get(position).getMaxProgress());
            Log.d(TAG, "onBindViewHolder: badge progress: " + badges.get(position).getMaxProgress());
            viewHolder.circleProgressbar.setMaxProgress(badges.get(position).getMaxProgress());
            viewHolder.circleProgressbar.setProgressWithAnimation(badges.get(position).getMaxProgress());
            viewHolder.circleProgressbar.setForegroundProgressColor(viewHolder.circleProgressbar.getResources().getColor(R.color.camel));

            // badge owned => color
            Target target = new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    viewHolder.circleProgressbar.setBackground(new BitmapDrawable(viewHolder.circleProgressbar.getResources(), bitmap));
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
            viewHolder.circleProgressbar.setMaxProgress(badges.get(position).getMaxProgress());
            viewHolder.circleProgressbar.setProgressWithAnimation(badges.get(position).getProgress());

            // badge not owned => gray
            ColorMatrix colorMatrix = new ColorMatrix();
            colorMatrix.setSaturation(0);
            ColorMatrixColorFilter filter = new ColorMatrixColorFilter(colorMatrix);
            Target target = new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    Drawable drawable = new BitmapDrawable(viewHolder.circleProgressbar.getResources(), bitmap);
                    drawable.setColorFilter(filter);
                    viewHolder.circleProgressbar.setBackground(drawable);
                }

                @Override
                public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            };
            if (badges.get(position).getImageUrl() != null)
                if (!badges.get(position).getImageUrl().isEmpty())
                    Picasso.get().load(badges.get(position).getImageUrl()).into(target);

        }
    }

    private void showBadgeDialog(@NonNull Badge badge) {
        Log.d(TAG, "showBadgeDialog: " + new Gson().toJson(badge.getBadgeTasks()));
        Log.d(TAG, "showBadgeDialog called");
        System.out.println(badge);
        Dialog dialog = new Dialog(context);
        View v = LayoutInflater.from(context).inflate(R.layout.dialog_badge_gamification,
                null, false);

        MaterialTextView titleTextView = v.findViewById(R.id.badgeDialogTitleTextView);
        titleTextView.setText(badge.getTitle());

        MaterialTextView descriptionTextView = v.findViewById(R.id.badgeDialogDescriptionTextView);
        descriptionTextView.setText(badge.getDescription());

        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setSaturation(0);
        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(colorMatrix);

        CircleProgressbar circleProgressbar = v.findViewById(R.id.badgeDialogCircleProgressBar);
        if (badge.isOwned()) {
            //circleProgressbar.setMaxProgress(1);
            circleProgressbar.setProgressWithAnimation(circleProgressbar.getMaxProgress());
            circleProgressbar.setForegroundProgressColor(circleProgressbar.getResources().getColor(R.color.camel));
        } else {
            circleProgressbar.setMaxProgress(badge.getMaxProgress());
            circleProgressbar.setProgressWithAnimation(badge.getProgress());
        }
        Log.d(TAG, "show badge dialog: " + badge.getTitle() + ", progress: " +
                badge.getProgress() + "/" + badge.getMaxProgress());
        Target target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                Drawable drawable = new BitmapDrawable(circleProgressbar.getResources(), bitmap);
                if (!badge.isOwned())
                    drawable.setColorFilter(filter);
                circleProgressbar.setBackground(drawable);
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
            }
        };
        if (badge.getImageUrl() != null) {
            if (!badge.getImageUrl().isEmpty()) {
                Picasso.get().load(badge.getImageUrl()).into(target);
            }
        }
        dialog.setContentView(v);
        dialog.show();

        RecyclerView recyclerView = v.findViewById(R.id.dialogBadgeRecyclerView);
        GamificationBadgesDialogRecyclerViewAdapter gamificationBadgesDialogRecyclerViewAdapter =
                new GamificationBadgesDialogRecyclerViewAdapter(badge.getBadgeTasks());
        recyclerView.setAdapter(gamificationBadgesDialogRecyclerViewAdapter);

        Window window = dialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
    }

    public class SliderAdapterViewHolder extends RecyclerView.ViewHolder {
        private CircleProgressbar circleProgressbar;
        private TextView textView;
        private LinearLayout linearLayout;

        public SliderAdapterViewHolder(View itemView) {
            super(itemView);
            circleProgressbar = itemView.findViewById(R.id.badgeGamificationCardCircleProgressBar);
            textView = itemView.findViewById(R.id.badgeGamificationCardTitleTextView);
            linearLayout = itemView.findViewById(R.id.badgeGamificationCardLinearLayout);
        }
    }
}