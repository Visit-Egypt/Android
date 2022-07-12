package com.visitegypt.presentation.gamification;

import android.content.SharedPreferences;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.visitegypt.domain.model.Badge;
import com.visitegypt.domain.model.FullBadge;
import com.visitegypt.domain.model.FullExplore;
import com.visitegypt.domain.model.FullPlaceActivity;
import com.visitegypt.domain.model.Item;
import com.visitegypt.domain.model.Place;
import com.visitegypt.domain.model.User;
import com.visitegypt.domain.model.XPUpdate;
import com.visitegypt.domain.usecase.GetBadgesOfPlaceUseCase;
import com.visitegypt.domain.usecase.GetFullBadgeUseCase;
import com.visitegypt.domain.usecase.GetFullPlaceActivitiesUseCase;
import com.visitegypt.domain.usecase.GetItemsUseCase;
import com.visitegypt.domain.usecase.GetPlaceDetailUseCase;
import com.visitegypt.domain.usecase.GetUserExploresUseCase;
import com.visitegypt.domain.usecase.GetUserUseCase;
import com.visitegypt.domain.usecase.UpdateChatBotPlacePlaceActivity;
import com.visitegypt.domain.usecase.UpdatePostAPostActivityUseCase;
import com.visitegypt.domain.usecase.UpdateReviewActivityUseCase;
import com.visitegypt.domain.usecase.UpdateUserChatbotArtifactUseCase;
import com.visitegypt.domain.usecase.UpdateVisitPlaceActivityUseCase;
import com.visitegypt.utils.Constants;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;


@HiltViewModel
public class GamificationViewModel extends ViewModel {
    private static final String TAG = "Gamification ViewModel";

    MutableLiveData<Place> placesMutableLiveData = new MutableLiveData<>();
    MutableLiveData<List<Item>> itemMutableLiveData = new MutableLiveData<>();

    MutableLiveData<List<FullPlaceActivity>> fullPlaceActivitiesMutableLiveData = new MutableLiveData<>();
    MutableLiveData<List<FullBadge>> fullBadgesMutableLiveData = new MutableLiveData<>();
    MutableLiveData<List<FullExplore>> fullExploreMutableLiveData = new MutableLiveData<>();

    MutableLiveData<XPUpdate> updatedVisitLocationMutableLiveData = new MutableLiveData<>();
    MutableLiveData<XPUpdate> updateChatBotPlaceMutableLiveData = new MutableLiveData<>();
    MutableLiveData<XPUpdate> updateChatBotArtifactMutableLiveData = new MutableLiveData<>();
    MutableLiveData<XPUpdate> updatedExploreMutableLiveData = new MutableLiveData<>();
    MutableLiveData<XPUpdate> updatedReviewMutableLiveData = new MutableLiveData<>();
    MutableLiveData<List<Badge>> placeBadgesMutableLiveData = new MutableLiveData<>();
    MutableLiveData<User> userMutableLiveData = new MutableLiveData<>();


    private SharedPreferences sharedPreferences;

    private GetItemsUseCase getItemsUseCase;

    private GetPlaceDetailUseCase getPlaceDetailUseCase;

    private GetFullBadgeUseCase getFullBadgeUseCase;
    private GetFullPlaceActivitiesUseCase getFullPlaceActivitiesUseCase;
    private GetUserExploresUseCase getUserExploresUseCase;
    private UpdateChatBotPlacePlaceActivity updateChatBotPlacePlaceActivity;
    private UpdateUserChatbotArtifactUseCase updateUserChatbotArtifactUseCase;
    private UpdateReviewActivityUseCase updateReviewActivityUseCase;
    private UpdateVisitPlaceActivityUseCase updateVisitPlaceActivityUseCase;
    private UpdatePostAPostActivityUseCase updatePostAPostActivityUseCase;
    private GetBadgesOfPlaceUseCase getBadgesOfPlaceUseCase;
    private GetUserUseCase getUserUseCase;

    private String placeId;

    @Inject
    public GamificationViewModel(SharedPreferences sharedPreferences,
                                 GetItemsUseCase getItemsUseCase,
                                 GetPlaceDetailUseCase getPlaceDetailUseCase,
                                 GetFullBadgeUseCase getFullBadgeUseCase,
                                 GetFullPlaceActivitiesUseCase getFullPlaceActivitiesUseCase,
                                 GetUserExploresUseCase getUserExploresUseCase,
                                 UpdateChatBotPlacePlaceActivity updateChatBotPlacePlaceActivity,
                                 UpdateUserChatbotArtifactUseCase updateUserChatbotArtifactUseCase,
                                 UpdateReviewActivityUseCase updateReviewActivityUseCase,
                                 UpdateVisitPlaceActivityUseCase updateVisitPlaceActivityUseCase,
                                 UpdatePostAPostActivityUseCase updatePostAPostActivityUseCase,
                                 GetBadgesOfPlaceUseCase getBadgesOfPlaceUseCase,
                                 GetUserUseCase getUserUseCase) {
        this.sharedPreferences = sharedPreferences;
        this.getItemsUseCase = getItemsUseCase;
        this.getPlaceDetailUseCase = getPlaceDetailUseCase;
        this.getFullBadgeUseCase = getFullBadgeUseCase;
        this.getFullPlaceActivitiesUseCase = getFullPlaceActivitiesUseCase;
        this.getUserExploresUseCase = getUserExploresUseCase;
        this.updateChatBotPlacePlaceActivity = updateChatBotPlacePlaceActivity;
        this.updateUserChatbotArtifactUseCase = updateUserChatbotArtifactUseCase;
        this.updateReviewActivityUseCase = updateReviewActivityUseCase;
        this.updateVisitPlaceActivityUseCase = updateVisitPlaceActivityUseCase;
        this.updatePostAPostActivityUseCase = updatePostAPostActivityUseCase;
        this.getBadgesOfPlaceUseCase = getBadgesOfPlaceUseCase;
        this.getUserUseCase = getUserUseCase;
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
                },
                throwable -> {
                    Log.e(TAG, "error retrieving items: " + throwable.getMessage());
                });
    }

    public void finishVisitLocation() {
        updateVisitPlaceActivityUseCase.setPlaceId(placeId);
        updateVisitPlaceActivityUseCase.execute(xpUpdate -> {
            updatedVisitLocationMutableLiveData.setValue(xpUpdate);
        }, throwable -> {
            Log.e(TAG, "finishVisitLocation: " + throwable.getMessage());
            updatedVisitLocationMutableLiveData.setValue(null);
        });
    }

    public void finishChatBotPlace() {
        updateChatBotPlacePlaceActivity.setPlaceId(placeId);
        updateChatBotPlacePlaceActivity.execute(unused -> {

        }, throwable -> {

        });
    }

    public void finishChatBotArtifact() {
        updateUserChatbotArtifactUseCase.setPlaceId(placeId);
        updateUserChatbotArtifactUseCase.execute(unused -> {

        }, throwable -> {

        });
    }

    public void finishReview() {
        updateReviewActivityUseCase.setPlaceId(placeId);
        updateReviewActivityUseCase.execute(xpUpdate -> {
            updatedReviewMutableLiveData.setValue(xpUpdate);
        }, throwable -> {
            Log.e(TAG, "finishReview: ", throwable);
            updatedReviewMutableLiveData.setValue(null);
        });
    }

    public void finishPost() {
        updatePostAPostActivityUseCase.setPlaceId(placeId);
        updatePostAPostActivityUseCase.execute(unused -> {

        }, throwable -> {

        });
    }

    public void getFullPlaceActivities() {
        getFullPlaceActivitiesUseCase.setPlaceId(placeId);
        Log.d(TAG, "getFullPlaceActivities: getting place activities: " + placeId);
        getFullPlaceActivitiesUseCase.execute(fullPlaceActivities -> {
            fullPlaceActivitiesMutableLiveData.setValue(fullPlaceActivities);
        }, throwable -> {
            fullPlaceActivitiesMutableLiveData.setValue(null);
            Log.e(TAG, "getFullPlaceActivities: " + throwable.getMessage());
        });
    }

    public void getFullBadges() {
        getFullBadgeUseCase.execute(fullBadges -> {
            fullBadgesMutableLiveData.setValue(fullBadges);
        }, throwable -> {
            fullBadgesMutableLiveData.setValue(null);
            Log.e(TAG, "getFullBadges: " + throwable.getMessage());
        });
    }

    public void getFullExplores() {
        getUserExploresUseCase.execute(fullExplores -> {
            fullExploreMutableLiveData.setValue(fullExplores);
        }, throwable -> {
            fullExploreMutableLiveData.setValue(null);
            Log.e(TAG, "getFullExplores: " + throwable.getMessage());
        });
    }

    public void getFullPlaceBadges() {
        getBadgesOfPlaceUseCase.setPlaceId(placeId);
        getBadgesOfPlaceUseCase.execute(badgeResponse -> {
            placeBadgesMutableLiveData.setValue(badgeResponse.getBadges());
        }, throwable -> {
            Log.e(TAG, "getFullPlaceBadges: ", throwable);
            placeBadgesMutableLiveData.setValue(null);
        });
    }

    public void getUser() {
        String userId = sharedPreferences.getString(Constants.SHARED_PREF_USER_ID, "");
        String email = sharedPreferences.getString(Constants.SHARED_PREF_EMAIL, "");
        getUserUseCase.setUser(userId, email);
        getUserUseCase.execute(user -> {
                    userMutableLiveData.setValue(user);
                },
                throwable -> {
                    Log.e(TAG, "getUser: error retrieving user: " + throwable.getMessage());
                });
    }

}