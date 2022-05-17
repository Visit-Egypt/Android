package com.visitegypt.presentation.userProfile;

import static com.visitegypt.utils.Constants.SHARED_PREF_USER_ID;

import android.content.SharedPreferences;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.visitegypt.data.source.local.dao.TagDao;
import com.visitegypt.domain.model.Tag;
import com.visitegypt.domain.model.TripMateRequest;
import com.visitegypt.domain.model.User;
import com.visitegypt.domain.repository.TagRepository;
import com.visitegypt.domain.usecase.FollowUserUseCase;
import com.visitegypt.domain.usecase.GetTagUseCase;
import com.visitegypt.domain.usecase.GetUserUseCase;
import com.visitegypt.domain.usecase.RequestTripMateUseCase;
import com.visitegypt.domain.usecase.UnFollowUseCase;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

@HiltViewModel
public class UserProfileViewModel extends ViewModel {
    // TODO: Implement the ViewModel
    private GetUserUseCase getUserUseCase;
    private FollowUserUseCase followUserUseCase;
    private RequestTripMateUseCase requestTripMateUseCase;
    private GetTagUseCase getTagUseCase;
    private SharedPreferences sharedPreferences;
    private String userId;
    private TripMateRequest tripMateRequest;
    private UnFollowUseCase unFollowUseCase;
    private TagDao tagDao;
    private static final String TAG = "User Profile View Model";
    MutableLiveData<User> mutableLiveDataUser = new MutableLiveData<>();
    MutableLiveData<String> mutableLiveDataFollow = new MutableLiveData<>();
    MutableLiveData<Boolean> mutableLiveDataIsFollowing = new MutableLiveData<>();
    MutableLiveData<Boolean> mutableLiveDataIsRequested = new MutableLiveData<>();
    MutableLiveData<String> mutableLiveDataUnFollow = new MutableLiveData<>();
    MutableLiveData<List<Tag>>  mutableLiveDataTagNames = new MutableLiveData<>();

    @Inject
    public UserProfileViewModel(GetUserUseCase getUserUseCase,
                                FollowUserUseCase followUserUseCase,
                                SharedPreferences sharedPreferences,
                                RequestTripMateUseCase requestTripMateUseCase,
                                GetTagUseCase getTagUseCase,
                                UnFollowUseCase unFollowUseCase,
                                TagDao tagDao
                                ) {
        this.getUserUseCase = getUserUseCase;
        this.followUserUseCase = followUserUseCase;
        this.sharedPreferences = sharedPreferences;
        this.requestTripMateUseCase = requestTripMateUseCase;
        this.unFollowUseCase = unFollowUseCase;
        this.getTagUseCase = getTagUseCase;
        this.tagDao = tagDao;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void getUser() {
        getUserUseCase.setUser(userId, "");
        getUserUseCase.execute(user -> {
            mutableLiveDataUser.setValue(user);
            if (user.getInterests() != null && user.getInterests().size() != 0)
            {
                tagDao.getTagsNameByIds(user.getInterests())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe((tags, throwable) -> {
                            mutableLiveDataTagNames.setValue(tags);
                        });
            }
            if (user.getFollowers().contains(sharedPreferences.getString(SHARED_PREF_USER_ID,"")))
                mutableLiveDataIsFollowing.setValue(true);

        }, throwable -> {
            Log.d(TAG, "getUser: " + throwable.getMessage());
        });
    }

    public void followUser() {
        followUserUseCase.setUserId(userId);
        followUserUseCase.execute(stringStringHashMap -> {
            mutableLiveDataFollow.setValue(stringStringHashMap.get("followers_num"));
        },throwable -> {
            mutableLiveDataFollow.setValue("Error");
        });
    }
    public void unFollow() {
        unFollowUseCase.setUserId(userId);
        unFollowUseCase.execute(stringStringHashMap -> {
            mutableLiveDataUnFollow.setValue(stringStringHashMap.get("followers_num"));
        },throwable -> {
            mutableLiveDataUnFollow.setValue("Error");
        });
    }

    public void setRequestMateBody(TripMateRequest tripMateRequest) {
        this.tripMateRequest = tripMateRequest;
    }

    public void requestTripMate(){
        requestTripMateUseCase.setUserId(userId);
        requestTripMateUseCase.setRequestMateBody(tripMateRequest);
        requestTripMateUseCase.execute(user -> {
            mutableLiveDataIsRequested.setValue(true);
        },throwable -> {
            Log.d(TAG, "requestTripMate: "+throwable.getMessage());

        });
    }

}