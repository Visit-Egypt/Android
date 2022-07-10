package com.visitegypt.presentation.setting;

import static com.visitegypt.utils.Constants.SHARED_PREF_USER_ID;
import static com.visitegypt.utils.Constants.SHARED_PREF_USER_IMAGE;

import android.content.SharedPreferences;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.visitegypt.domain.model.User;
import com.visitegypt.domain.model.UserUpdateRequest;
import com.visitegypt.domain.usecase.GetUserUseCase;
import com.visitegypt.domain.usecase.LogOutUseCase;
import com.visitegypt.domain.usecase.UpdateUserUseCase;
import com.visitegypt.domain.usecase.UploadUserPhotoUseCase;
import com.visitegypt.utils.Constants;

import org.json.JSONObject;

import java.io.File;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.functions.Consumer;
import okhttp3.ResponseBody;
import retrofit2.HttpException;

@HiltViewModel
public class SettingViewModel extends ViewModel implements UploadUserPhotoUseCase.uploadPhotoApiCallBack {
    private static final String TAG = "Setting View Model";
    GetUserUseCase getUserUseCase;
    UpdateUserUseCase updateUserUseCase;
    LogOutUseCase logOutUseCase;
    SharedPreferences sharedPreferences;
    UploadUserPhotoUseCase uploadUserPhotoUseCase;
    MutableLiveData<String> url = new MutableLiveData<>();
    MutableLiveData<Boolean> isLoged = new MutableLiveData<>();
    public MutableLiveData<User> mutableLiveDataUser = new MutableLiveData<>();

    @Inject
    public SettingViewModel(GetUserUseCase getUserUseCase, SharedPreferences sharedPreferences, UpdateUserUseCase updateUserUseCase, LogOutUseCase logOutUseCase, UploadUserPhotoUseCase uploadUserPhotoUseCase) {
        this.getUserUseCase = getUserUseCase;
        this.sharedPreferences = sharedPreferences;
        this.updateUserUseCase = updateUserUseCase;
        this.logOutUseCase = logOutUseCase;
        this.uploadUserPhotoUseCase = uploadUserPhotoUseCase;

    }

    public void getUserData() {

        getUserUseCase.setUser(sharedPreferences.getString(SHARED_PREF_USER_ID, null),
                sharedPreferences.getString(Constants.SHARED_PREF_EMAIL, null));
        getUserUseCase.execute(new Consumer<User>() {
            @Override
            public void accept(User user) throws Throwable {
                Log.d("TAG", "onChanged: setting ++" + user.getFirstName());
                mutableLiveDataUser.setValue(user);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Throwable {
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
        });
    }


    public void uploadUserProfilePhoto(File userPhoto, String contentType) {
        uploadUserPhotoUseCase.setContentType(contentType);
        uploadUserPhotoUseCase.setUserFile(userPhoto);
        uploadUserPhotoUseCase.upload();

    }

    public void logOut() {
        logOutUseCase.setUserId(sharedPreferences.getString(SHARED_PREF_USER_ID, null));
        logOutUseCase.execute(s -> {
            sharedPreferences.edit().clear().commit();
            isLoged.setValue(false);
        }, throwable -> {
            Log.d(TAG, "logOut: " + throwable.getMessage());

        });
    }

    public void updateUser(UserUpdateRequest userUpdateRequest) {
        updateUserUseCase.setUser(userUpdateRequest);
        updateUserUseCase.execute(user -> {
            saveNewUserUpdate(user);
            mutableLiveDataUser.setValue(user);
        }, throwable -> {
            try {
                ResponseBody body = ((HttpException) throwable).response().errorBody();
                JSONObject jObjectError = new JSONObject(body.string());
                Log.d("TAG", "accept try : " + jObjectError.getJSONArray("errors"));
                if (jObjectError.getJSONArray("errors").toString().contains("msg")) {


                } else {

                }
            } catch (Exception e) {
                Log.e("TAG", "accept catch: " + e);
            }
        });

    }

    public void saveNewData(User user) {
        sharedPreferences.edit()
                .putString(SHARED_PREF_USER_ID, user.getUserId())
                .putString(SHARED_PREF_USER_IMAGE, user.getPhotoUrl())
                .putString(Constants.SHARED_PREF_FIRST_NAME, user.getFirstName() + " " + user.getLastName())
                .apply();
    }

    public void saveNewUserUpdate(User user) {
        if (user.getFirstName() != null && !user.getFirstName().isEmpty())
            sharedPreferences.edit()
                    .putString(Constants.SHARED_PREF_FIRST_NAME, user.getFirstName() + " " + user.getLastName())
                    .apply();

        if (user.getPhotoUrl() != null && !user.getPhotoUrl().isEmpty()) {
            sharedPreferences.edit().
                    putString(SHARED_PREF_USER_IMAGE, user.getPhotoUrl())
                    .apply();
        }
    }

    @Override
    public void confirmUpload(int code, List<String> url) {
        if (code == 200) {
            this.url.setValue(url.get(0));

        }
    }

    public void initCallBack() {
        uploadUserPhotoUseCase.setUploadPhotoApiCallBack(this::confirmUpload);
    }
}
