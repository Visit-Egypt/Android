package com.visitegypt.utils;

import android.content.Context;
import android.graphics.Color;
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
import com.visitegypt.R;
import com.visitegypt.domain.model.BadgeTask;
import com.visitegypt.domain.model.PlaceActivity;

import java.util.ArrayList;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import kotlin.Unit;

public class GeneralUtils {

    public static void showUserProgress(Context context, View callingView,
                                        @Nullable ArrayList<PlaceActivity> placeActivityArrayList,
                                        @Nullable ArrayList<BadgeTask> badgeTaskArrayList) {
        Snackbar snackbar = Snackbar.make(callingView, "", BaseTransientBottomBar.LENGTH_LONG);
        Snackbar.SnackbarLayout snackbarLayout = (Snackbar.SnackbarLayout) snackbar.getView();

        LinearLayoutCompat linearLayoutCompat = new LinearLayoutCompat(context);
        linearLayoutCompat.setOrientation(LinearLayoutCompat.VERTICAL);
        linearLayoutCompat.setLayoutParams(new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));

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

                TextView descriptionTextView = snackbar.getView().findViewById(R.id.badgeSnackbarDescriptionTextView);
                descriptionTextView.setText(badgeTask.getTaskTitle());

                TextView xpTextView = snackbar.getView().findViewById(R.id.badgeSnackXpTextView);
                LinearProgressIndicator linearProgressIndicator =
                        snackbar.getView().findViewById(R.id.badgeSnackbarProgressIndicator);

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

        snackbar.show();
    }

    public static void showSnackError(Context context, View callingView, @Nonnull String error) {
        Snackbar snackbar = Snackbar.make(callingView, error, BaseTransientBottomBar.LENGTH_LONG);
        snackbar.setTextColor(Color.RED);
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

    public static void showButtonLoaded(final Button button, @Nullable String newText) {
        DrawableButtonExtensionsKt.hideProgress(button, newText == null ? "Complete" : newText);
    }

    public static void showButtonFailed(final Button button, @Nullable String error, @Nullable String newButtonText) {
        button.setEnabled(true);
        DrawableButtonExtensionsKt.hideProgress(button, newButtonText);
        GeneralUtils.showSnackError(button.getContext(), button, error == null ? "An error has occurred" : error);
    }


}
