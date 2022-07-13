package com.visitegypt.utils;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.github.razir.progressbutton.DrawableButton;
import com.github.razir.progressbutton.DrawableButtonExtensionsKt;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.hanks.htextview.fade.FadeTextView;
import com.squareup.picasso.Picasso;
import com.visitegypt.R;
import com.visitegypt.domain.model.BadgeTask;
import com.visitegypt.domain.model.PlaceActivity;
import com.visitegypt.domain.model.XPUpdate;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import kotlin.Unit;

public class GeneralUtils {

    private static final String TAG = "general utils";

    public static void showUserProgress(Context context, View callingView,
                                        @Nullable ArrayList<PlaceActivity> placeActivityArrayList,
                                        @Nullable ArrayList<BadgeTask> badgeTaskArrayList,
                                        @Nullable XPUpdate xpUpdate,
                                        @Nullable String imageUrl) {
        Snackbar snackbar = Snackbar.make(callingView, "", BaseTransientBottomBar.LENGTH_LONG);
        Snackbar.SnackbarLayout snackbarLayout = (Snackbar.SnackbarLayout) snackbar.getView();

        LinearLayoutCompat linearLayoutCompat = new LinearLayoutCompat(context);
        linearLayoutCompat.setOrientation(LinearLayoutCompat.VERTICAL);
        linearLayoutCompat.setLayoutParams(new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));

        if (xpUpdate != null)
            if (xpUpdate.getOldXP() != xpUpdate.getNewXp()) {
                Log.d(TAG, "showUserProgress: " + new Gson().toJson(xpUpdate));

                View levelView = LayoutInflater.from(context).inflate(R.layout.snack_level, null, false);

                int userLevel = GamificationRules.getLevelFromXp(xpUpdate.getOldXP());

                ImageView profileFrameImageView = levelView.findViewById(R.id.profileFrameImageViewSnackBar);
                Animation fade = new AlphaAnimation(0, 1);
                fade.setDuration(500);
                fade.setInterpolator(new DecelerateInterpolator());
                profileFrameImageView.setBackground(GamificationRules.getProfileFrameDrawable(context,
                        GamificationRules.getTitleFromLevel(userLevel)));
                profileFrameImageView.startAnimation(fade);

                if (imageUrl != null && !imageUrl.isEmpty()) {
                    ImageView accountImageView = levelView.findViewById(R.id.circularAccountImageViewSnackBar);
                    Picasso.get().load(imageUrl).into(accountImageView);
                }

                FadeTextView userTitleFadeTextView = levelView.findViewById(R.id.userTitleFadeTextView);
                userTitleFadeTextView.animateText(GamificationRules.getTitleFromLevel(userLevel));

                FadeTextView userLevelTextView = levelView.findViewById(R.id.snackUserLevelFadeTextView);
                userLevelTextView.setText(userLevel + "");
                userLevelTextView.animateText(userLevel + "");

                FadeTextView userNextLevelTextView = levelView.findViewById(R.id.snackUserNextLevelFadeTextView);
                userNextLevelTextView.setText(userLevel + 1 + "");
                userNextLevelTextView.animateText(userLevel + 1 + "");

                FadeTextView userXPFadeTextView = levelView.findViewById(R.id.snackUserXPFadeTextView);
                userXPFadeTextView.setText(GamificationRules.getRemainingXPToNextLevel(xpUpdate.getOldXP()) + "XP remaining");
                userXPFadeTextView.animateText(GamificationRules.getRemainingXPToNextLevel(xpUpdate.getOldXP()) + "XP remaining");

                LinearProgressIndicator linearProgressIndicator = levelView.findViewById(R.id.snackUserXPProgressIndicator);
                linearProgressIndicator.setMax(GamificationRules.getLevelXp(userLevel + 1) - GamificationRules.getLevelXp(userLevel));
                linearProgressIndicator.setProgress(linearProgressIndicator.getMax() -
                        GamificationRules.getRemainingXPToNextLevel(xpUpdate.getOldXP()), true);

                int newUserLevel = GamificationRules.getLevelFromXp(xpUpdate.getNewXp());
                new java.util.Timer().schedule(
                        new java.util.TimerTask() {
                            @Override
                            public void run() {
                                if (newUserLevel > userLevel) {
                                    // User leveled up
                                    Log.d(TAG, "run: new userLevel bigger: " + GamificationRules.getLevelFromXp(xpUpdate.getOldXP()));
                                    linearProgressIndicator.setMax(GamificationRules.getLevelXp(newUserLevel + 1) - GamificationRules.getLevelXp(newUserLevel));
                                    linearProgressIndicator.setProgress(linearProgressIndicator.getMax() -
                                            GamificationRules.getRemainingXPToNextLevel(xpUpdate.getNewXp()), true);

                                    userLevelTextView.post(() -> userLevelTextView.animateText(newUserLevel + ""));
                                    userNextLevelTextView.post(() -> userNextLevelTextView.animateText(newUserLevel + 1 + ""));
                                    userXPFadeTextView.post(() -> userXPFadeTextView.animateText("level up!"));
                                    if (!GamificationRules.getTitleFromLevel(newUserLevel).equals(GamificationRules.getTitleFromLevel(userLevel))) {
                                        profileFrameImageView.post(() -> {
                                            profileFrameImageView.setBackground(GamificationRules.getProfileFrameDrawable(context,
                                                    GamificationRules.getTitleFromLevel(newUserLevel)));
                                            profileFrameImageView.startAnimation(fade);
                                        });
                                        userTitleFadeTextView.post(() -> userTitleFadeTextView.animateText(GamificationRules.getTitleFromLevel(newUserLevel)));
                                        new Timer().schedule(new TimerTask() {
                                            @Override
                                            public void run() {
                                                userXPFadeTextView.post(() ->
                                                        userXPFadeTextView.animateText(GamificationRules.
                                                                getRemainingXPToNextLevel(xpUpdate.getNewXp()) + "XP remaining"));
                                            }
                                        }, 2000);
                                    }

                                } else {
                                    linearProgressIndicator.setProgress(linearProgressIndicator.getMax() -
                                            GamificationRules.getRemainingXPToNextLevel(xpUpdate.getNewXp()), true);
                                    userXPFadeTextView.post(() -> userXPFadeTextView.animateText(GamificationRules.getRemainingXPToNextLevel(xpUpdate.getNewXp()) + "XP remaining"));
                                }
                            }
                        },
                        1500
                );

                linearLayoutCompat.addView(levelView);

            }

        if (placeActivityArrayList != null) {
            for (PlaceActivity placeActivity : placeActivityArrayList) {
                View placeActivityLayout = LayoutInflater.from(context).inflate(R.layout.snack_place_activity,
                        null, false);

                TextView descriptionTextView = placeActivityLayout.findViewById(R.id.activitySnackbarDescriptionTextView);
                descriptionTextView.setText(placeActivity.getTitle());

                TextView xpTextView = placeActivityLayout.findViewById(R.id.activitySnackXpTextView);
                LinearProgressIndicator linearProgressIndicator =
                        placeActivityLayout.findViewById(R.id.activitySnackbarProgressIndicator);

                linearProgressIndicator.setProgress(0);
                linearProgressIndicator.setMax(placeActivity.getXp());
                linearProgressIndicator.setProgress(linearProgressIndicator.getMax(), true);
                xpTextView.setText(linearProgressIndicator.getProgress() + "XP left");

                linearLayoutCompat.addView(placeActivityLayout);

            }

        }
        if (badgeTaskArrayList != null) {
            for (BadgeTask badgeTask : badgeTaskArrayList) {
                View badgeTaskLayout = LayoutInflater.from(context).inflate(R.layout.snack_badge_task,
                        null, false);

                TextView descriptionTextView = badgeTaskLayout.findViewById(R.id.badgeSnackbarDescriptionTextView);
                descriptionTextView.setText(badgeTask.getTaskTitle());

                TextView xpTextView = badgeTaskLayout.findViewById(R.id.badgeSnackXpTextView);
                LinearProgressIndicator linearProgressIndicator =
                        badgeTaskLayout.findViewById(R.id.badgeSnackbarProgressIndicator);

                linearProgressIndicator.setProgress(0);
                linearProgressIndicator.setMax(badgeTask.getMaxProgress());
                linearProgressIndicator.setProgress(linearProgressIndicator.getMax(), true);
                xpTextView.setText(linearProgressIndicator.getProgress() + "XP left");

                linearLayoutCompat.addView(badgeTaskLayout);
            }
        }

        snackbar.getView().setBackgroundColor(Color.TRANSPARENT);
        snackbarLayout.setPadding(0, 0, 0, 0);
        snackbarLayout.addView(linearLayoutCompat, 0);
        snackbar.setDuration(6000);
        snackbar.setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE);
        snackbar.show();
    }

    public static void showSnackError(Context context, View callingView, @Nonnull String error) {
        Snackbar snackbar = Snackbar.make(callingView, error, BaseTransientBottomBar.LENGTH_LONG);
        snackbar.setTextColor(Color.RED);
        snackbar.setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE);
        snackbar.show();
    }

    public static void showSnackInfo(Context context, View callingView, @Nonnull String info) {
        Snackbar snackbar = Snackbar.make(callingView, info, BaseTransientBottomBar.LENGTH_LONG);
        //snackbar.setTextColor(Color.RED);
        snackbar.setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE);
        snackbar.show();
    }

    public void showPlaceActivity(Context context) {

    }

    public static class LiveDataUtil {
        public static <T> void observeOnce(final LiveData<T> liveData, final Observer<T> observer) {
            liveData.observeForever(new Observer<T>() {
                @Override
                public void onChanged(T t) {
                    liveData.removeObserver(this);
                    observer.onChanged(t);
                }
            });
        }
    }

    public static void showButtonLoading(final Button button) {
        DrawableButtonExtensionsKt.showProgress(button, progressParams -> {
            progressParams.setButtonTextRes(R.string.loading);
            progressParams.setProgressColor(Color.WHITE);
            progressParams.setGravity(DrawableButton.GRAVITY_TEXT_START);
            return Unit.INSTANCE;
        });
        button.setEnabled(false);
    }

    public static void showButtonLoadingRight(final Button button) {
        DrawableButtonExtensionsKt.showProgress(button, progressParams -> {
            progressParams.setButtonText("  please wait");
            progressParams.setProgressColor(Color.WHITE);
            return Unit.INSTANCE;
        });
        button.setEnabled(false);
    }

    public static void showButtonLoaded(final Button button, @Nullable String newText) {
        DrawableButtonExtensionsKt.hideProgress(button, newText == null ? "Complete" : newText);
    }

    public static void showButtonFailed(final Button button, @Nullable String error, @Nullable String newButtonText) {
        button.setEnabled(true);
        DrawableButtonExtensionsKt.hideProgress(button, newButtonText);
        GeneralUtils.showSnackError(button.getContext(), button, error == null ? "An error has occurred" : error);
    }
    public static void showButtonSaveLoading(final Button button) {
        DrawableButtonExtensionsKt.showProgress(button, progressParams -> {
            progressParams.setButtonTextRes(R.string.loading);
            progressParams.setProgressColor(Color.WHITE);
            progressParams.setGravity(DrawableButton.GRAVITY_TEXT_START);
            return Unit.INSTANCE;
        });
        button.setEnabled(true);
    }




}
