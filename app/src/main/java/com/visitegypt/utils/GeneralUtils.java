package com.visitegypt.utils;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.github.razir.progressbutton.DrawableButton;
import com.github.razir.progressbutton.DrawableButtonExtensionsKt;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.hanks.htextview.fade.FadeTextView;
import com.visitegypt.R;
import com.visitegypt.domain.model.BadgeTask;
import com.visitegypt.domain.model.PlaceActivity;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import kotlin.Unit;

public class GeneralUtils {

    private static final String TAG = "general utils";
    public static int userXP;

    public static void showUserProgress(Context context, View callingView,
                                        @Nullable ArrayList<PlaceActivity> placeActivityArrayList,
                                        @Nullable ArrayList<BadgeTask> badgeTaskArrayList,
                                        @Nullable int userXp,
                                        @Nullable int newXp) {
        Snackbar snackbar = Snackbar.make(callingView, "", BaseTransientBottomBar.LENGTH_LONG);
        Snackbar.SnackbarLayout snackbarLayout = (Snackbar.SnackbarLayout) snackbar.getView();

        LinearLayoutCompat linearLayoutCompat = new LinearLayoutCompat(context);
        linearLayoutCompat.setOrientation(LinearLayoutCompat.VERTICAL);
        linearLayoutCompat.setLayoutParams(new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));


        if (userXp != -1) {
            Log.d(TAG, "showUserProgress: old XP: " + userXp);
            Log.d(TAG, "showUserProgress: new XP: " + newXp);
            View levelView = LayoutInflater.from(context).inflate(R.layout.snack_level, null, false);

            int userLevel = GamificationRules.getLevelFromXp(userXp);

            FadeTextView userTitleFadeTextView = levelView.findViewById(R.id.userTitleFadeTextView);
            userTitleFadeTextView.animateText(GamificationRules.getTitleFromLevel(userLevel));

            FadeTextView userLevelTextView = levelView.findViewById(R.id.snackUserLevelFadeTextView);
            userLevelTextView.setText(userLevel + "");

            FadeTextView userNextLevelTextView = levelView.findViewById(R.id.snackUserNextLevelFadeTextView);
            userNextLevelTextView.setText(userLevel + 1 + "");

            FadeTextView userXPFadeTextView = levelView.findViewById(R.id.snackUserXPFadeTextView);
            userXPFadeTextView.setText(GamificationRules.getRemainingXPToNextLevel(userXp) + "XP");

            LinearProgressIndicator linearProgressIndicator = levelView.findViewById(R.id.snackUserXPProgressIndicator);
            linearProgressIndicator.setMax(GamificationRules.getLevelXp(userLevel + 1));
            linearProgressIndicator.setProgress(GamificationRules.getRemainingXPToNextLevel(userXp), true);


            if (newXp != 0) {

                int newUserLevel = GamificationRules.getLevelFromXp(newXp);
                new java.util.Timer().schedule(
                        new java.util.TimerTask() {
                            @Override
                            public void run() {
                                if (newUserLevel > userLevel) {
                                    // User leveled up
                                    Log.d(TAG, "run: new userLevel bigger: " + GamificationRules.getLevelFromXp(userXp));
                                    linearProgressIndicator.setMax(GamificationRules.getLevelXp(newUserLevel + 1));
                                    linearProgressIndicator.setProgress(linearProgressIndicator.getMax() -
                                            GamificationRules.getRemainingXPToNextLevel(newXp), true);

                                    userLevelTextView.post(() -> userLevelTextView.animateText(newUserLevel + ""));
                                    userNextLevelTextView.post(() -> userNextLevelTextView.animateText(newUserLevel + 1 + ""));
                                    userXPFadeTextView.post(() -> userXPFadeTextView.animateText("level up!"));
                                    userTitleFadeTextView.post(() -> userTitleFadeTextView.animateText(GamificationRules.getTitleFromLevel(newUserLevel)));
                                    new Timer().schedule(new TimerTask() {
                                        @Override
                                        public void run() {
                                            userXPFadeTextView.post(() -> userXPFadeTextView.animateText(GamificationRules.getRemainingXPToNextLevel(newXp) + "XP"));
                                        }
                                    }, 2000);


                                } else {
                                    Log.d(TAG, "run: " + GamificationRules.getLevelFromXp(userXp));
                                    Log.d(TAG, "run: " + GamificationRules.getLevelFromXp(newXp));
                                    Log.d(TAG, "run: " + GamificationRules.getRemainingXPToNextLevel(userXp));
                                    Log.d(TAG, "run: " + GamificationRules.getRemainingXPToNextLevel(newXp));
                                    linearProgressIndicator.setProgress(linearProgressIndicator.getMax() -
                                            GamificationRules.getRemainingXPToNextLevel(newXp), true);
                                    userXPFadeTextView.post(() -> userXPFadeTextView.animateText(GamificationRules.getRemainingXPToNextLevel(newXp) + "XP"));
                                }
                            }
                        },
                        1500
                );

            }

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
                xpTextView.setText(linearProgressIndicator.getProgress() + "XP");

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
                xpTextView.setText(linearProgressIndicator.getProgress() + "XP");

                linearLayoutCompat.addView(badgeTaskLayout);
            }
        }

        snackbar.getView().setBackgroundColor(Color.TRANSPARENT);
        snackbarLayout.setPadding(0, 0, 0, 0);
        snackbarLayout.addView(linearLayoutCompat, 0);
        snackbar.setDuration(6000);
        snackbar.show();
    }

    public static void showSnackError(Context context, View callingView, @Nonnull String error) {
        Snackbar snackbar = Snackbar.make(callingView, error, BaseTransientBottomBar.LENGTH_LONG);
        snackbar.setTextColor(Color.RED);
        snackbar.show();
    }

    public static void showSnackInfo(Context context, View callingView, @Nonnull String info) {
        Snackbar snackbar = Snackbar.make(callingView, info, BaseTransientBottomBar.LENGTH_LONG);
        //snackbar.setTextColor(Color.RED);
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




}
