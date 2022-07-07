package com.visitegypt.presentation.home.parent;

import static com.visitegypt.utils.Constants.SHARED_PREF_EMAIL;
import static com.visitegypt.utils.Constants.SHARED_PREF_FIRST_NAME;
import static com.visitegypt.utils.Constants.SHARED_PREF_USER_ID;
import static com.visitegypt.utils.Constants.SHARED_PREF_USER_IMAGE;

import android.content.SharedPreferences;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.Gson;
import com.visitegypt.domain.model.Badge;
import com.visitegypt.domain.model.BadgeTask;
import com.visitegypt.domain.model.User;
import com.visitegypt.domain.usecase.GetAllBadgesUseCase;
import com.visitegypt.domain.usecase.GetTagUseCase;
import com.visitegypt.domain.usecase.GetUserUseCase;
import com.visitegypt.domain.usecase.LogOutUseCase;
import com.visitegypt.domain.usecase.UpdateUserBadgeTaskProgUseCase;
import com.visitegypt.utils.Constants;

import org.json.JSONObject;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import okhttp3.ResponseBody;
import retrofit2.HttpException;

@HiltViewModel
public class HomeViewModel extends ViewModel {

    private static final String TAG = "home view model";

    SharedPreferences sharedPreferences;

    MutableLiveData<Boolean> isLogged = new MutableLiveData<>();
    MutableLiveData<User> mutableLiveDataUser = new MutableLiveData<>();
    MutableLiveData<List<BadgeTask>> userBadgeTasksMutableLiveData = new MutableLiveData<>();
    MutableLiveData<List<Badge>> allBadgesMutableLiveData = new MutableLiveData<>();
    MutableLiveData<String> userNameMutable = new MutableLiveData<>();
    MutableLiveData<String> userEmailMutable = new MutableLiveData<>();

    private LogOutUseCase logOutUseCase;
    private GetUserUseCase getUserUseCase;
    private UpdateUserBadgeTaskProgUseCase updateUserBadgeTaskProgUseCase;
    private GetAllBadgesUseCase getAllBadgesUseCase;
    private BadgeTask badgeTask;
    private GetTagUseCase getTagUseCase;


    @Inject
    public HomeViewModel(SharedPreferences sharedPreferences, LogOutUseCase logOutUseCase,
                         GetUserUseCase getUserUseCase, UpdateUserBadgeTaskProgUseCase updateUserBadgeTaskProgUseCase,
                         GetAllBadgesUseCase getAllBadgesUseCase,
                         GetTagUseCase getTagUseCase
    ) {
        this.sharedPreferences = sharedPreferences;
        this.logOutUseCase = logOutUseCase;
        this.getUserUseCase = getUserUseCase;
        this.updateUserBadgeTaskProgUseCase = updateUserBadgeTaskProgUseCase;
        this.getAllBadgesUseCase = getAllBadgesUseCase;
        this.getTagUseCase = getTagUseCase;
    }

    public void earnFirstBadge() {
        if (badgeTask == null) {
            Log.e(TAG, "earnFirstBadge: must call setBadgeTask()");
        }
        badgeTask.setType("general");
        Log.d(TAG, "earnFirstBadge: setting badge task: " + new Gson().toJson(badgeTask));

        updateUserBadgeTaskProgUseCase.setBadgeTask(badgeTask);
        updateUserBadgeTaskProgUseCase.execute(badges -> {
            userBadgeTasksMutableLiveData.setValue(badges);
            Log.d(TAG, "earnFirstBadge: success");
        }, throwable -> Log.e(TAG, "failed to earn first badge: " + throwable.getMessage()));
    }

    public void getAllBadges() {
        getAllBadgesUseCase.execute(badgeResponse -> {
            allBadgesMutableLiveData.setValue(badgeResponse.getBadges());
        }, throwable -> {
            Log.e(TAG, "getAllBadges: failed" + throwable.getMessage());
        });
    }

    public void getUserInfo() {
        //This is not right
        Boolean flag = true;
        while (flag) {
            if (sharedPreferences.getString(SHARED_PREF_FIRST_NAME, null) != null) {
                break;
            }
        }

        userNameMutable.setValue(sharedPreferences.getString(SHARED_PREF_FIRST_NAME, null));
        userEmailMutable.setValue(sharedPreferences.getString(SHARED_PREF_EMAIL, null));
    }

    public void logOut() {
        logOutUseCase.setUserId(sharedPreferences.getString(SHARED_PREF_USER_ID, null));
        logOutUseCase.execute(s -> {
            sharedPreferences.edit().clear().commit();
            isLogged.setValue(false);
        }, throwable -> {
            // TODO delete on 403 error only
            sharedPreferences.edit().clear().commit();
            isLogged.setValue(false);
            Log.e("TAG", "logOut: " + throwable.getMessage());
        });
    }

    public void getUserData() {
        getUserUseCase.setUser(sharedPreferences.getString(SHARED_PREF_USER_ID, null),
                sharedPreferences.getString(Constants.SHARED_PREF_EMAIL, null));
        getUserUseCase.execute(user -> {
            Log.d("TAG", "onChanged: setting ++" + user.getFirstName());
            mutableLiveDataUser.setValue(user);
        }, throwable -> {
            try {
                ResponseBody body = ((HttpException) throwable).response().errorBody();
                JSONObject jObjectError = new JSONObject(body.string());
                Log.d("TAG", "accept try : " + jObjectError.getJSONArray("errors").toString());
                if (jObjectError.getJSONArray("errors").toString().contains("msg")) {
                } else {
                }
            } catch (Exception e) {
                Log.e("TAG", "accept catch: " + e.toString());
            }
        });
    }

    public void saveUserImage(String url) {
        sharedPreferences.edit().putString(SHARED_PREF_USER_IMAGE, url).apply();
    }

    public void setBadgeTask(BadgeTask badgeTask) {
        this.badgeTask = badgeTask;
    }

    public void getAllTags() {
        getTagUseCase.buildSingleUseCase().subscribe(tags -> {
        }, throwable -> {
            Log.e(TAG, "Tag: from Home  " + throwable.getMessage());
        });
    }
}
