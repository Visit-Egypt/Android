package com.visitegypt.presentation.signup;

import android.content.SharedPreferences;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.visitegypt.domain.model.User;
import com.visitegypt.domain.usecase.RegisterUseCase;
import com.visitegypt.domain.usecase.UpdateUserBadgeTaskProgUseCase;
import com.visitegypt.domain.usecase.UserValidation;

import org.json.JSONObject;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import okhttp3.ResponseBody;
import retrofit2.HttpException;

@HiltViewModel
public class SignUpViewModel extends ViewModel {

    MutableLiveData<String[]> mutableLiveDataErrors = new MutableLiveData<>();
    MutableLiveData<String> mutableLiveDataResponse = new MutableLiveData<>();

    private static UserValidation userValidation;
    SharedPreferences sharedPreferences;

    private static final String TAG = "Signup view model";
    private UpdateUserBadgeTaskProgUseCase updateUserBadgeTaskProgUseCase;

    public void setUserValidation(UserValidation userValidation) {
        this.userValidation = userValidation;
    }

    private RegisterUseCase registerUseCase;

    @Inject
    public SignUpViewModel(RegisterUseCase registerUseCase, SharedPreferences sharedPreferences,
                           UpdateUserBadgeTaskProgUseCase updateUserBadgeTaskProgUseCase) {
        this.registerUseCase = registerUseCase;
        this.sharedPreferences = sharedPreferences;
        this.updateUserBadgeTaskProgUseCase = updateUserBadgeTaskProgUseCase;
    }

    public void getUser() {
        User myUser = userValidation;
        registerUseCase.saveUser(userValidation);
        registerUseCase.execute(user -> {
            user.setFirstName(myUser.getFirstName());
            user.setLastName(myUser.getLastName());
            user.setEmail(myUser.getEmail());
            user.setPhoneNumber(myUser.getPhoneNumber());
            Log.d(TAG, "getUser: " + user.getFirstName());
            Log.d(TAG, "getUser: " + user.getUserId());
            Log.d(TAG, "getUser: " + user.getLastName());
            Log.d(TAG, "getUser: " + user.getAccessToken());
            Log.d(TAG, "getUser: " + user.getRefreshToken());
            registerUseCase.saveUserData(user);
            mutableLiveDataResponse.setValue("Your account was created successfully");
        }, throwable -> {
            try {
                Log.e(TAG, "getUser: " + throwable.getMessage());
                ResponseBody body = ((HttpException) throwable).response().errorBody();
                JSONObject jObjectError = new JSONObject(body.string());
                Log.e(TAG, "accept try : " + jObjectError.getJSONArray("errors"));
                if (jObjectError.getJSONArray("errors").toString().contains("msg")) {
                    mutableLiveDataResponse.setValue(jObjectError.getJSONArray("errors").getJSONObject(0).getString("msg"));
                } else {
                    mutableLiveDataResponse.setValue(jObjectError.getJSONArray("errors").toString());
                }
            } catch (Exception e) {
                Log.e(TAG, "accept catch: " + e.getMessage());
            }
        });
    }

    public Boolean checkUserValidation() {
        mutableLiveDataErrors.postValue(userValidation.checkValidations());
        if (userValidation.isValidUser()) {
            return true;
        }
        return false;
    }
}