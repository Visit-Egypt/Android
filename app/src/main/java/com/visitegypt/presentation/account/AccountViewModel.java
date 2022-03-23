package com.visitegypt.presentation.account;

import static com.visitegypt.utils.Constants.SHARED_PREF_USER_IMAGE;

import android.content.SharedPreferences;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.visitegypt.domain.model.Badge;
import com.visitegypt.domain.model.Post;
import com.visitegypt.domain.usecase.GetBadgesOfUserUseCase;
import com.visitegypt.domain.usecase.GetPostsByUser;
import com.visitegypt.utils.Constants;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import okhttp3.ResponseBody;
import retrofit2.HttpException;

@HiltViewModel
public class AccountViewModel extends ViewModel {

    private static final String TAG = "account view model";

    SharedPreferences sharedPreferences;
    MutableLiveData<List<Post>> mutableLiveDataMyPosts = new MutableLiveData<>();
    MutableLiveData<String> mutableLiveDataName = new MutableLiveData<>();
    MutableLiveData<String> mutableLiveDataUserImage = new MutableLiveData<>();
    MutableLiveData<ArrayList<Badge>> badgesMutableLiveData = new MutableLiveData<>();
    GetPostsByUser getPostsByUser;
    GetBadgesOfUserUseCase getBadgesOfUserUseCase;

    @Inject
    public AccountViewModel(SharedPreferences sharedPreferences, GetPostsByUser getPostsByUser, GetBadgesOfUserUseCase getBadgesOfUserUseCase) {
        this.sharedPreferences = sharedPreferences;
        this.getPostsByUser = getPostsByUser;
        this.getBadgesOfUserUseCase = getBadgesOfUserUseCase;
    }

    public void getUserInformation() {
        mutableLiveDataName.setValue(sharedPreferences.getString(Constants.SHARED_PREF_FIRST_NAME, null));
        mutableLiveDataUserImage.setValue(sharedPreferences.getString(SHARED_PREF_USER_IMAGE,""));
        getUserPosts();

    }

    public void getUserBadges() {
        Log.d(TAG, "getUserBadges: getting user badges...");
        getBadgesOfUserUseCase.execute(badges -> {
            Log.d(TAG, "getUserBadges: successfully retrieved user badges");
            badgesMutableLiveData.setValue((ArrayList<Badge>) badges);
        }, throwable -> {
            Log.e(TAG, "getUserBadges: " + throwable.getMessage());
        });
    }

    private void getUserPosts() {
        //when backend finishes there work start to implement
        Log.d("TAG", "accept List of posts:  welcome");
        getPostsByUser.execute(postPage -> mutableLiveDataMyPosts.setValue(postPage.getPosts()), throwable -> {
                    try {
                        ResponseBody body = ((HttpException) throwable).response().errorBody();
                        JSONObject jObjectError = new JSONObject(body.string());
                        Log.d("TAG", "accept try : " + jObjectError.getJSONArray("errors").toString());
                        if (jObjectError.getJSONArray("errors").toString().contains("msg")) {
                        } else {
                        }
                    } catch (Exception e) {
                        Log.d("TAG", "accept catch: " + e.toString());
                    }
                }
        );
    }

}
