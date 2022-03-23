package com.visitegypt.presentation.gamification;

import android.content.SharedPreferences;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.visitegypt.domain.model.Badge;
import com.visitegypt.domain.model.BadgeTask;
import com.visitegypt.domain.model.Item;
import com.visitegypt.domain.model.Place;
import com.visitegypt.domain.model.PlaceActivity;
import com.visitegypt.domain.usecase.GetBadgesOfPlaceUseCase;
import com.visitegypt.domain.usecase.GetBadgesOfUserUseCase;
import com.visitegypt.domain.usecase.GetItemsUseCase;
import com.visitegypt.domain.usecase.GetPlaceDetailUseCase;
import com.visitegypt.domain.usecase.GetUserPlaceActivityUseCase;
import com.visitegypt.domain.usecase.UpdateBadgeOfUserUseCase;
import com.visitegypt.domain.usecase.UpdateUserBadgeTaskProgUseCase;
import com.visitegypt.domain.usecase.UpdateUserPlaceActivityUseCase;
import com.visitegypt.utils.Constants;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;


@HiltViewModel
public class GamificationViewModel extends ViewModel {
    private static final String TAG = "Gamification ViewModel";

    MutableLiveData<List<PlaceActivity>> userPlaceActivitiesMutableLiveData = new MutableLiveData<>();

    MutableLiveData<Place> placesMutableLiveData = new MutableLiveData<>();
    MutableLiveData<List<Item>> itemMutableLiveData = new MutableLiveData<>();
    MutableLiveData<List<Badge>> badgesMutableLiveData = new MutableLiveData<>();
    private SharedPreferences sharedPreferences;

    private GetItemsUseCase getItemsUseCase;
    private GetBadgesOfUserUseCase getBadgesOfUserUseCase;
    private UpdateUserBadgeTaskProgUseCase updateUserBadgeTaskProgUseCase;
    private UpdateBadgeOfUserUseCase updateBadgeOfUserUseCase;
    private GetBadgesOfPlaceUseCase getBadgesOfPlaceUseCase;
    private UpdateUserPlaceActivityUseCase updateUserPlaceActivityUseCase;
    private GetUserPlaceActivityUseCase getUserPlaceActivityUseCase;
    private GetPlaceDetailUseCase getPlaceDetailUseCase;

    private String placeId;
    private PlaceActivity placeActivity;

    @Inject
    public GamificationViewModel(SharedPreferences sharedPreferences, GetPlaceDetailUseCase getPlaceDetailUseCase,
                                 GetItemsUseCase getItemsUseCase, UpdateBadgeOfUserUseCase updateBadgeOfUserUseCase,
                                 GetBadgesOfUserUseCase getBadgesOfUserUseCase,
                                 UpdateUserBadgeTaskProgUseCase updateUserBadgeTaskProgUseCase,
                                 GetBadgesOfPlaceUseCase getBadgesOfPlaceUseCase,
                                 UpdateUserPlaceActivityUseCase updateUserPlaceActivityUseCase,
                                 GetUserPlaceActivityUseCase getUserPlaceActivityUseCase) {
        this.getPlaceDetailUseCase = getPlaceDetailUseCase;
        this.getItemsUseCase = getItemsUseCase;
        this.sharedPreferences = sharedPreferences;
        this.getBadgesOfUserUseCase = getBadgesOfUserUseCase;
        this.updateUserBadgeTaskProgUseCase = updateUserBadgeTaskProgUseCase;
        this.updateBadgeOfUserUseCase = updateBadgeOfUserUseCase;
        this.getBadgesOfPlaceUseCase = getBadgesOfPlaceUseCase;
        this.updateUserPlaceActivityUseCase = updateUserPlaceActivityUseCase;
        this.getUserPlaceActivityUseCase = getUserPlaceActivityUseCase;
    }


    public void getPlaceDetail() throws Exception {
        if (placeId.isEmpty()) throw new Exception("you must use setPlaceId first");
        getPlaceDetailUseCase.setPlaceId(placeId);
        getPlaceDetailUseCase.execute(place -> placesMutableLiveData.setValue(place), throwable -> {
        });
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public void setPlaceActivity(PlaceActivity placeActivity) {
        this.placeActivity = placeActivity;
    }

    public void updatePlaceActivityForUser() throws Exception {
        if (placeActivity == null) throw new Exception("must call setPlaceActivity");
        if (placeActivity.getProgress() != placeActivity.getMaxProgress()) {
            placeActivity.setProgress(placeActivity.getProgress() + 1);
            updateUserPlaceActivityUseCase.setPlaceActivity(placeActivity);
            updateUserPlaceActivityUseCase.execute(placeActivities -> {

            }, throwable -> {
                Log.e(TAG, "updatePlaceActivityForUser: failed to update activity progress" + throwable.getMessage());
            });
        }
    }

    private void getPlaceActivity() {

    }

    public void getUserPlaceActivity() {
        // TODO getUserPlaceActivityUseCase.setUserId();
        String userId = sharedPreferences.getString(Constants.SHARED_PREF_USER_ID, "");
        if (userId.isEmpty()) {
            Log.e(TAG, "getUserPlaceActivity: user not found");
        }
        getUserPlaceActivityUseCase.setUserId(userId);
        getUserPlaceActivityUseCase.execute(placeActivities -> {
            userPlaceActivitiesMutableLiveData.setValue(placeActivities);
        }, throwable -> {
            Log.e(TAG, "getUserPlaceActivity: failed to get place activities for the user: " + throwable.getMessage());
        });
    }

    public void getItemsByPlaceId(String placeId) {
        getItemsUseCase.setPlaceId(placeId);
        getItemsUseCase.execute(itemPageResponse -> {
                    itemMutableLiveData.setValue(itemPageResponse.getItems());
                }
                ,
                throwable -> {
                    Log.e(TAG, "error retrieving items: " + throwable.getMessage());
                });
    }

    public void getPlaceBadges() {
        getBadgesOfPlaceUseCase.setPlaceId(placeId);
        getBadgesOfPlaceUseCase.execute(badgeResponse -> badgesMutableLiveData.setValue(badgeResponse.getBadges()), throwable -> Log.e(TAG, "error retrieving badges: " + throwable.getMessage()));

        getBadgesOfUserUseCase.execute(badges -> {
            badgesMutableLiveData.setValue(badges);
        }, throwable -> {
            Log.e(TAG, "error getting user badges: " + throwable.getMessage());
        });
    }

    public void updateBadeTaskProgress(BadgeTask badgeTask) {
        updateUserBadgeTaskProgUseCase.setBadgeTask(badgeTask);
        updateUserBadgeTaskProgUseCase.execute(badgeTasks -> {

        }, throwable -> {

        });
    }

    public void updateBadgeProgress(Badge badge) {

    }
}