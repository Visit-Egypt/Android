package com.visitegypt.presentation.account;

import static com.visitegypt.utils.Constants.SHARED_PREF_USER_IMAGE;

import android.content.SharedPreferences;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.visitegypt.domain.model.Badge;
import com.visitegypt.domain.model.Place;
import com.visitegypt.domain.model.PlaceActivity;
import com.visitegypt.domain.model.Post;
import com.visitegypt.domain.model.Tag;
import com.visitegypt.domain.model.User;
import com.visitegypt.domain.usecase.GetAllBadgesUseCase;
import com.visitegypt.domain.usecase.GetBadgesOfUserUseCase;
import com.visitegypt.domain.usecase.GetPlacesByPlaceActivityIdUseCase;
import com.visitegypt.domain.usecase.GetPlacesUseCase;
import com.visitegypt.domain.usecase.GetPostsByUser;
import com.visitegypt.domain.usecase.GetTagUseCase;
import com.visitegypt.domain.usecase.GetTagsNameByIds;
import com.visitegypt.domain.usecase.GetUserPlaceActivityUseCase;
import com.visitegypt.domain.usecase.GetUserUseCase;
import com.visitegypt.domain.usecase.UpdateUserInterestUseCase;
import com.visitegypt.utils.Constants;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import okhttp3.ResponseBody;
import retrofit2.HttpException;

@HiltViewModel
public class AccountViewModel extends ViewModel {

    private static final String TAG = "account view model";

    MutableLiveData<ArrayList<Place>> placesWithNeededPlaceActivities = new MutableLiveData<>();

    MutableLiveData<ArrayList<Badge>> userBadgesMutableLiveData = new MutableLiveData<>();

    MutableLiveData<List<Post>> mutableLiveDataMyPosts = new MutableLiveData<>();
    MutableLiveData<String> mutableLiveDataName = new MutableLiveData<>();
    MutableLiveData<String> mutableLiveDataUserImage = new MutableLiveData<>();
    MutableLiveData<ArrayList<Badge>> allBadgesMutableLiveData = new MutableLiveData<>();
    MutableLiveData<User> userMutableLiveData = new MutableLiveData<>();
    MutableLiveData<ArrayList<PlaceActivity>> userPlaceActivityMutableLiveData = new MutableLiveData<>();
    MutableLiveData<List<Tag>> mutableLiveDataUserTagNames = new MutableLiveData<>();
    MutableLiveData<List<Tag>> mutableLiveDataAllTags = new MutableLiveData<>();
    MutableLiveData<Boolean> mutableLiveUpdateIsDone = new MutableLiveData<>();
    private List<String> placeActivitiesId;
    private SharedPreferences sharedPreferences;
    private GetPostsByUser getPostsByUser;
    private GetBadgesOfUserUseCase getBadgesOfUserUseCase;
    private GetUserUseCase getUserUseCase;
    private GetAllBadgesUseCase getAllBadgesUseCase;
    private GetTagUseCase getTagUseCase;
    private GetUserPlaceActivityUseCase getUserPlaceActivityUseCase;
    private GetPlacesUseCase getPlacesUseCase;
    private GetPlacesByPlaceActivityIdUseCase getPlacesByPlaceActivityIdUseCase;
    private GetTagsNameByIds getTagsNameByIds;
    private UpdateUserInterestUseCase userInterestUseCase;

    @Inject
    public AccountViewModel(SharedPreferences sharedPreferences, GetPostsByUser getPostsByUser,
                            GetBadgesOfUserUseCase getBadgesOfUserUseCase,
                            GetAllBadgesUseCase getAllBadgesUseCase,
                            GetUserPlaceActivityUseCase getUserPlaceActivityUseCase,
                            GetPlacesUseCase getPlacesUseCase,
                            GetPlacesByPlaceActivityIdUseCase getPlacesByPlaceActivityIdUseCase,
                            GetUserUseCase getUserUseCase,
                            GetTagUseCase getTagUseCase,
                            GetTagsNameByIds getTagsNameByIds,
                            UpdateUserInterestUseCase userInterestUseCase
    ) {
        this.sharedPreferences = sharedPreferences;
        this.getPostsByUser = getPostsByUser;
        this.getBadgesOfUserUseCase = getBadgesOfUserUseCase;
        this.getUserUseCase = getUserUseCase;
        this.getAllBadgesUseCase = getAllBadgesUseCase;
        this.getTagUseCase = getTagUseCase;
        this.getUserPlaceActivityUseCase = getUserPlaceActivityUseCase;
        this.getPlacesUseCase = getPlacesUseCase;
        this.getPlacesByPlaceActivityIdUseCase = getPlacesByPlaceActivityIdUseCase;
        this.getTagsNameByIds = getTagsNameByIds;
        this.userInterestUseCase = userInterestUseCase;
    }

    public void getPlaceActivitiesOfUser() {
        String userId = sharedPreferences.getString(Constants.SHARED_PREF_USER_ID, "");
        getUserPlaceActivityUseCase.setUserId(userId);
        getUserPlaceActivityUseCase.execute(placeActivities -> {
            userPlaceActivityMutableLiveData.setValue((ArrayList<PlaceActivity>) placeActivities);
        }, throwable -> {

        });
    }

    public void getUserInformation() {
        mutableLiveDataName.setValue(sharedPreferences.getString(Constants.SHARED_PREF_FIRST_NAME, null));
        mutableLiveDataUserImage.setValue(sharedPreferences.getString(SHARED_PREF_USER_IMAGE, ""));
        getUserPosts();
    }

    public void getUser() {
        String userId = sharedPreferences.getString(Constants.SHARED_PREF_USER_ID, "");
        String email = sharedPreferences.getString(Constants.SHARED_PREF_EMAIL, "");
        getUserUseCase.setUser(userId, email);
        getUserUseCase.execute(user -> {
                    userMutableLiveData.setValue(user);
                    if (user.getInterests() != null && user.getInterests().size() != 0) {
                        getTagsNameByIds.setTags(user.getInterests());
                        getTagsNameByIds.execute(tags -> {
                            mutableLiveDataUserTagNames.setValue(tags);
                        }, throwable -> {
                            Log.e(TAG, "getUser: ", throwable);
                        });
                    }

                }
                ,
                throwable -> {
                    Log.e(TAG, "getUser: error retrieving user: " + throwable.getMessage());
                });
    }

    public void getUserBadges() {
        Log.d(TAG, "getUserBadges: getting user badges...");
        getBadgesOfUserUseCase.execute(badges -> {
            Log.d(TAG, "getUserBadges: successfully retrieved user badges");
            userBadgesMutableLiveData.setValue((ArrayList<Badge>) badges);
        }, throwable -> {
            Log.e(TAG, "getUserBadges: " + throwable.getMessage());
        });
    }

    public void getAllBadges() {
        getAllBadgesUseCase.execute(badgeResponse -> {
            allBadgesMutableLiveData.setValue((ArrayList<Badge>) badgeResponse.getBadges());
        }, throwable -> {
            Log.e(TAG, "getAllBadges: " + throwable.getMessage());
        });
    }

    public void getUserPosts() {
        //when backend finishes there work start to implement
        Log.d(TAG, "accept List of posts:  welcome");
        getPostsByUser.execute(postPage -> mutableLiveDataMyPosts.setValue(postPage.getPosts()), throwable -> {
                    try {
                        ResponseBody body = ((HttpException) throwable).response().errorBody();
                        JSONObject jObjectError = new JSONObject(body.string());
                        Log.d(TAG, "accept try : " + jObjectError.getJSONArray("errors").toString());
                        if (jObjectError.getJSONArray("errors").toString().contains("msg")) {
                        } else {
                        }
                    } catch (Exception e) {
                        Log.d(TAG, "accept catch: " + e.toString());
                    }
                }
        );
    }


    public void getPlacesByPlaceActivities() {
        if (placeActivitiesId == null) {
            Log.e(TAG, "getPlacesByPlaceActivities: you must setPlaceActivitiesId");
        }
        getPlacesByPlaceActivityIdUseCase.setPlaceActivities(placeActivitiesId);
        getPlacesByPlaceActivityIdUseCase.execute(places -> {
            placesWithNeededPlaceActivities.setValue((ArrayList<Place>) places);
        }, throwable -> {
            Log.e(TAG, "getPlacesByPlaceActivities: " + throwable.getMessage());
        });

    }

    public void setPlaceActivitiesId(List<String> placeActivitiesId) {
        this.placeActivitiesId = placeActivitiesId;
    }

    public void getAllTags() {
        getTagUseCase.buildSingleUseCase()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(tags -> {
                    mutableLiveDataAllTags.setValue(tags);
                }, throwable -> {
                    Log.e(TAG, "getAllTags: ", throwable);
                })

        ;
    }

    public void updateYourInterest(HashSet<String> newInterests, HashSet<String> removedInterests) {
        userInterestUseCase.setNewInterest(newInterests);
        userInterestUseCase.setRemovedInterest(removedInterests);
        userInterestUseCase
                .updateInterest()
                .subscribe(() -> {
                    mutableLiveUpdateIsDone.setValue(true);
                    Log.d(TAG, "updateYourInterest: woooooooooow ");
                }, throwable -> {
                    Log.e(TAG, "updateYourInterest: ", throwable);
                })

        ;
    }
}
