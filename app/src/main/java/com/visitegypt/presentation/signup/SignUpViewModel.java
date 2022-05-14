package com.visitegypt.presentation.signup;

import android.content.SharedPreferences;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.visitegypt.domain.model.Token;
import com.visitegypt.domain.model.User;
import com.visitegypt.domain.usecase.GetGoogleRegisterTokenUseCase;
import com.visitegypt.domain.usecase.RegisterDeviceToNotificationUseCase;
import com.visitegypt.domain.usecase.RegisterUseCase;
import com.visitegypt.domain.usecase.UpdateUserBadgeTaskProgUseCase;
import com.visitegypt.domain.usecase.UserValidation;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.functions.Consumer;

@HiltViewModel
public class SignUpViewModel extends ViewModel {

    private static final String TAG = "Signup view model";
    private static UserValidation userValidation;
    MutableLiveData<String[]> mutableLiveDataErrors = new MutableLiveData<>();
    MutableLiveData<String> mutableLiveDataResponse = new MutableLiveData<>();
    SharedPreferences sharedPreferences;
    private UpdateUserBadgeTaskProgUseCase updateUserBadgeTaskProgUseCase;
    private RegisterUseCase registerUseCase;
    private GetGoogleRegisterTokenUseCase getGoogleRegisterTokenUseCase;
    private RegisterDeviceToNotificationUseCase registerDeviceToNotificationUseCase;

    @Inject
    public SignUpViewModel(GetGoogleRegisterTokenUseCase getGoogleRegisterTokenUseCase, RegisterUseCase registerUseCase, SharedPreferences sharedPreferences,
                           UpdateUserBadgeTaskProgUseCase updateUserBadgeTaskProgUseCase,
                           RegisterDeviceToNotificationUseCase registerDeviceToNotificationUseCase

    ) {
        this.registerUseCase = registerUseCase;
        this.sharedPreferences = sharedPreferences;
        this.updateUserBadgeTaskProgUseCase = updateUserBadgeTaskProgUseCase;
        this.getGoogleRegisterTokenUseCase = getGoogleRegisterTokenUseCase;
        this.registerDeviceToNotificationUseCase = registerDeviceToNotificationUseCase;
    }

    public void setUserValidation(UserValidation userValidation) {
        this.userValidation = userValidation;
    }

    public void getUser() {
        User myUser = userValidation;
        registerUseCase.saveUser(userValidation);
        mutableLiveDataResponse.setValue("Your account was created successfully");
        registerUseCase.execute(u -> {
            mutableLiveDataResponse.setValue("Your account was created successfully");

        }, throwable -> {
            mutableLiveDataResponse.setValue("errorrr");

        });
        //                registerUseCase.execute(user -> {
//            user.setFirstName(myUser.getFirstName());
//            user.setLastName(myUser.getLastName());
//            user.setEmail(myUser.getEmail());
//            user.setPhoneNumber(myUser.getPhoneNumber());
//
////            Log.d(TAG, "getUser: " + user.getFirstName());
////            Log.d(TAG, "getUser: " + user.getUserId());
////            Log.d(TAG, "getUser: " + user.getLastName());
////            Log.d(TAG, "getUser: " + user.getAccessToken());
////            Log.d(TAG, "getUser: " + user.getRefreshToken());
////            registerUseCase.saveUserData(user);
//
//            mutableLiveDataResponse.setValue("Your account was created successfully");
//        }, throwable -> {
//            try {
//                Log.e(TAG, "getUser: " + throwable.getMessage());
//                ResponseBody body = ((HttpException) throwable).response().errorBody();
//                JSONObject jObjectError = new JSONObject(body.string());
//                Log.e(TAG, "accept try : " + jObjectError.getJSONArray("errors"));
//                if (jObjectError.getJSONArray("errors").toString().contains("msg")) {
//                    mutableLiveDataResponse.setValue(jObjectError.getJSONArray("errors").getJSONObject(0).getString("msg"));
//                } else {
//                    mutableLiveDataResponse.setValue(jObjectError.getJSONArray("errors").toString());
//                }
//            } catch (Exception e) {
//                Log.e(TAG, "accept catch: " + e.getMessage());
//            }
//        });
    }

    public Boolean checkUserValidation() {
        mutableLiveDataErrors.postValue(userValidation.checkValidations());
        if (userValidation.isValidUser()) {
            return true;
        }
        return false;
    }

    public void signUpWithGoogle(String token, String email) {
        getGoogleRegisterTokenUseCase.setToken(new Token(token));
        getGoogleRegisterTokenUseCase.execute(new Consumer<User>() {
            @Override
            public void accept(User user) throws Throwable {
                Log.d("TAG", "donee" + user);
                registerUseCase.saveUserData(user);
                mutableLiveDataResponse.setValue("Your google account was created successfully");
            }

        }, throwable -> {
            mutableLiveDataResponse.setValue("There is an error may You already have an account ");
            Log.d(TAG, "signUpWithGoogle: " + throwable.getMessage());
        });

    }
    public void test(String st)
    {
        registerDeviceToNotificationUseCase.setMsg(st);
        registerDeviceToNotificationUseCase.execute(map -> {
            Log.d(TAG, "test: " + map.get("message"));
        },throwable -> {
            Log.d(TAG, "test: "+ throwable.getMessage());
        });
    }

}