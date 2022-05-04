package com.visitegypt.presentation.userProfile;

import static com.visitegypt.utils.Constants.SHARED_PREF_USER_ID;

import android.content.SharedPreferences;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.visitegypt.domain.model.TripMateRequest;
import com.visitegypt.domain.model.User;
import com.visitegypt.domain.repository.TagRepository;
import com.visitegypt.domain.usecase.FollowUserUseCase;
import com.visitegypt.domain.usecase.GetTagUseCase;
import com.visitegypt.domain.usecase.GetUserUseCase;
import com.visitegypt.domain.usecase.RequestTripMateUseCase;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class UserProfileViewModel extends ViewModel {
    // TODO: Implement the ViewModel
    GetUserUseCase getUserUseCase;
    FollowUserUseCase followUserUseCase;
    RequestTripMateUseCase requestTripMateUseCase;
    GetTagUseCase getTagUseCase;
    SharedPreferences sharedPreferences;
    private String userId;
    private TripMateRequest tripMateRequest;
    private static final String TAG = "User Profile View Model";
    MutableLiveData<User> mutableLiveDataUser = new MutableLiveData<>();
    MutableLiveData<Boolean> mutableLiveDataFollow = new MutableLiveData<>();
    MutableLiveData<Boolean> mutableLiveDataIsFollowing = new MutableLiveData<>();
    MutableLiveData<Boolean> mutableLiveDataIsRequested = new MutableLiveData<>();
    @Inject
    public UserProfileViewModel(GetUserUseCase getUserUseCase, FollowUserUseCase followUserUseCase,SharedPreferences sharedPreferences,RequestTripMateUseCase requestTripMateUseCase,GetTagUseCase getTagUseCase) {
        this.getUserUseCase = getUserUseCase;
        this.followUserUseCase = followUserUseCase;
        this.sharedPreferences = sharedPreferences;
        this.requestTripMateUseCase = requestTripMateUseCase;
        this.getTagUseCase = getTagUseCase;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void getUser() {
        getUserUseCase.setUser(userId, "");
        getUserUseCase.execute(user -> {
            mutableLiveDataUser.setValue(user);
            if (user.getFollowers().contains(sharedPreferences.getString(SHARED_PREF_USER_ID,"")))
                mutableLiveDataIsFollowing.setValue(true);


        }, throwable -> {
            Log.d(TAG, "getUser: " + throwable.getMessage());
        });
    }

    public void followUser() {
        followUserUseCase.setUserId(userId);
        followUserUseCase.execute(aBoolean -> {
            mutableLiveDataFollow.setValue(aBoolean);
        }, throwable -> {
            Log.d(TAG, "followUser: " + throwable.getMessage());
        });
    }

    public void setRequestMateBody(TripMateRequest tripMateRequest) {
        this.tripMateRequest = tripMateRequest;
    }

    public void requestTripMate(){
        requestTripMateUseCase.setUserId(userId);
        requestTripMateUseCase.setRequestMateBody(tripMateRequest);
        requestTripMateUseCase.execute(user -> {
            Log.d(TAG, "requestTripMate: Request is sent successfully");
            mutableLiveDataIsRequested.setValue(true);
        },throwable -> {
            Log.d(TAG, "requestTripMate: "+throwable.getMessage());

        });
    }

}