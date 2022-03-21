package com.visitegypt.presentation.gamification;

import android.content.SharedPreferences;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.visitegypt.domain.model.Badge;
import com.visitegypt.domain.model.BadgeTask;
import com.visitegypt.domain.model.Item;
import com.visitegypt.domain.model.Place;
import com.visitegypt.domain.usecase.GetBadgesOfPlaceUseCase;
import com.visitegypt.domain.usecase.GetBadgesOfUserUseCase;
import com.visitegypt.domain.usecase.GetItemsUseCase;
import com.visitegypt.domain.usecase.GetPlaceDetailUseCase;
import com.visitegypt.domain.usecase.UpdateBadgeOfUserUseCase;
import com.visitegypt.domain.usecase.UpdateUserBadgeTaskProgUseCase;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;


@HiltViewModel
public class GamificationViewModel extends ViewModel {
    private static final String TAG = "Gamification ViewModel";
    SharedPreferences sharedPreferences;
    GetPlaceDetailUseCase getPlaceDetailUseCase;
    MutableLiveData<Place> placesMutableLiveData = new MutableLiveData<>();
    MutableLiveData<List<Item>> itemMutableLiveData = new MutableLiveData<>();
    MutableLiveData<List<Badge>> badgesMutableLiveData = new MutableLiveData<>();

    private String placeId;
    private GetItemsUseCase getItemsUseCase;
    private GetBadgesOfUserUseCase getBadgesOfUserUseCase;
    private UpdateUserBadgeTaskProgUseCase updateUserBadgeTaskProgUseCase;
    private UpdateBadgeOfUserUseCase updateBadgeOfUserUseCase;
    private GetBadgesOfPlaceUseCase getBadgesOfPlaceUseCase;

    @Inject
    public GamificationViewModel(SharedPreferences sharedPreferences, GetPlaceDetailUseCase getPlaceDetailUseCase,
                                 GetItemsUseCase getItemsUseCase, UpdateBadgeOfUserUseCase updateBadgeOfUserUseCase,
                                 GetBadgesOfUserUseCase getBadgesOfUserUseCase,
                                 UpdateUserBadgeTaskProgUseCase updateUserBadgeTaskProgUseCase,
                                 GetBadgesOfPlaceUseCase getBadgesOfPlaceUseCase) {
        this.getPlaceDetailUseCase = getPlaceDetailUseCase;
        this.getItemsUseCase = getItemsUseCase;
        this.sharedPreferences = sharedPreferences;
        this.getBadgesOfUserUseCase = getBadgesOfUserUseCase;
        this.updateUserBadgeTaskProgUseCase = updateUserBadgeTaskProgUseCase;
        this.updateBadgeOfUserUseCase = updateBadgeOfUserUseCase;
        this.getBadgesOfPlaceUseCase = getBadgesOfPlaceUseCase;
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