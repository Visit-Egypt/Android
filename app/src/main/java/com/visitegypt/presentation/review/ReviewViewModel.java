package com.visitegypt.presentation.review;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.visitegypt.domain.model.BadgeTask;
import com.visitegypt.domain.model.Place;
import com.visitegypt.domain.model.PlaceActivity;
import com.visitegypt.domain.model.Review;
import com.visitegypt.domain.usecase.GetPlaceDetailUseCase;
import com.visitegypt.domain.usecase.SubmitReviewUseCase;
import com.visitegypt.domain.usecase.UpdateUserBadgeTaskProgUseCase;
import com.visitegypt.domain.usecase.UpdateUserPlaceActivityUseCase;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import retrofit2.HttpException;
import retrofit2.Response;

@HiltViewModel
public class ReviewViewModel extends ViewModel {

    private static final String TAG = "Review View Model";

    MutableLiveData<Place> placesMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<List<Review>> reviewMutableLiveData = new MutableLiveData<>();

    public MutableLiveData<List<BadgeTask>> badgeTaskMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<List<PlaceActivity>> placeActivitiesMutableLiveData = new MutableLiveData<>();

    public MutableLiveData<Integer> placeActivityUpdateResponseCode = new MutableLiveData<>();
    public MutableLiveData<Integer> badgeTaskUpdateResponseCode = new MutableLiveData<>();
    public MutableLiveData<Integer> mutableLiveDataResponseCode = new MutableLiveData<>();

    private SubmitReviewUseCase submitReviewUseCase;
    private GetPlaceDetailUseCase getPlaceDetailUseCase;
    private UpdateUserPlaceActivityUseCase updateUserPlaceActivityUseCase;
    private UpdateUserBadgeTaskProgUseCase updateUserBadgeTaskProgUseCase;

    private PlaceActivity reviewPlaceActivity;
    private BadgeTask reviewBadgeTask;

    @Inject
    public ReviewViewModel(GetPlaceDetailUseCase getPlaceDetailUseCase,
                           SubmitReviewUseCase submitReviewUseCase,
                           UpdateUserPlaceActivityUseCase updateUserPlaceActivityUseCase,
                           UpdateUserBadgeTaskProgUseCase updateUserBadgeTaskProgUseCase) {
        this.getPlaceDetailUseCase = getPlaceDetailUseCase;
        this.submitReviewUseCase = submitReviewUseCase;
        this.updateUserPlaceActivityUseCase = updateUserPlaceActivityUseCase;
        this.updateUserBadgeTaskProgUseCase = updateUserBadgeTaskProgUseCase;
    }


    public void getPlace(String placeId) {
        getPlaceDetailUseCase.setPlaceId(placeId);
        getPlaceDetailUseCase.execute(
                place -> {
                    placesMutableLiveData.setValue(place);
                }
                , throwable -> {
                    Log.e(TAG, "place detail retrieve failed", throwable);
                }
        );
    }

    private void updateBadgeTask() {
        if (reviewBadgeTask == null) {
            Log.e(TAG, "submitReview: unable to update badge progress, you must call setBadgeTask()");
        } else if (reviewBadgeTask.getProgress() < reviewBadgeTask.getMaxProgress()) {
            Log.d(TAG, "submitReview: updating review badge progress");
            reviewBadgeTask.setProgress(reviewBadgeTask.getProgress() + 1);
            updateUserBadgeTaskProgUseCase.setBadgeTask(reviewBadgeTask);
            updateUserBadgeTaskProgUseCase.execute(badgeTasks -> {
                badgeTaskMutableLiveData.setValue(badgeTasks);
                badgeTaskUpdateResponseCode.setValue(200);
            }, throwable -> {
            });
        } else {
            Log.d(TAG, "submitReview: you already earned the badge");
        }
    }

    private void updatePlaceActivity() {
        if (reviewPlaceActivity != null) {
            Log.d(TAG, "updatePlaceActivity: checking place activities");
            if (reviewPlaceActivity.getProgress() < reviewPlaceActivity.getMaxProgress()) {
                Log.d(TAG, "updatePlaceActivity: unfinished activity, increasing it...");
                reviewPlaceActivity.setProgress(reviewPlaceActivity.getProgress() + 1);
                Log.d(TAG, "updatePlaceActivity: task progress: " + reviewPlaceActivity.getProgress());
            }
            updateUserPlaceActivityUseCase.setPlaceActivity(reviewPlaceActivity);
            updateUserPlaceActivityUseCase.execute(placeActivities -> {
                Log.d(TAG, "updatePlaceActivity: updated user place activity progress");
                placeActivitiesMutableLiveData.setValue(placeActivities);
                placeActivityUpdateResponseCode.setValue(200);
            }, throwable -> {
                try {
                    Log.e(TAG, "updatePlaceActivity: " + throwable.getMessage());
                    Response<?> response = ((HttpException) throwable).response();
                    placeActivityUpdateResponseCode.setValue(response.code());

                } catch (Exception e) {
                    Log.e(TAG, "updatePlaceActivity: accept catch: " + e.getMessage());
                }
                Log.e(TAG, "updatePlaceActivity: " + throwable.getMessage());
            });
        }
    }

    public void setReviewBadgeTask(BadgeTask reviewBadgeTask) {
        this.reviewBadgeTask = reviewBadgeTask;
    }

    public void setReviewPlaceActivity(PlaceActivity reviewPlaceActivity) {
        this.reviewPlaceActivity = reviewPlaceActivity;
    }

    public void submitReview(String placeId, Review review) {
        if (reviewPlaceActivity == null) {
            Log.w(TAG, "submitReview: consider calling setPlaceActivity to update user progress");
        }
        Log.d(TAG, "submitReview: init");
        submitReviewUseCase.setPlaceId(placeId);
        submitReviewUseCase.setReview(review);
        submitReviewUseCase.execute(reviews -> {
            Log.d(TAG, "submitReview: Review is submitted");
            reviewMutableLiveData.setValue(reviews);
            mutableLiveDataResponseCode.setValue(200);
            updatePlaceActivity();
            updateBadgeTask();
        }, throwable -> {
            try {
                Log.e(TAG, "submitReview: " + throwable.getMessage());
                Response<?> response = ((HttpException) throwable).response();
                mutableLiveDataResponseCode.setValue(response.code());

            } catch (Exception e) {
                Log.e(TAG, "accept catch: " + e.getMessage());
            }
        });

    }

}
