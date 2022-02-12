package com.visitegypt.presentation.signup;

import android.content.SharedPreferences;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.visitegypt.domain.model.User;
import com.visitegypt.domain.usecase.RegisterUseCase;
import com.visitegypt.domain.usecase.UserValidation;
import com.visitegypt.utils.Error;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class SignUpViewModel extends ViewModel {
    private static final String TAG = "Sign Up View Model";
    private static UserValidation userValidation;
    MutableLiveData<String[]> mutableLiveDataErrors = new MutableLiveData<>();
    MutableLiveData<String> mutableLiveDataResponse = new MutableLiveData<>();
    SharedPreferences sharedPreferences;
    private RegisterUseCase registerUseCase;

    @Inject
    public SignUpViewModel(RegisterUseCase registerUseCase, SharedPreferences sharedPreferences) {
        this.registerUseCase = registerUseCase;
        this.sharedPreferences = sharedPreferences;
    }

    public void setUserValidation(UserValidation userValidation) {
        this.userValidation = userValidation;
    }

    public void getUser() {
        User myUser = userValidation;
        registerUseCase.saveUser(userValidation);
        try {
            registerUseCase.execute(user -> {
                user.setFirstName(myUser.getFirstName());
                user.setLastName(myUser.getLastName());
                user.setEmail(myUser.getEmail());
                user.setPhoneNumber(myUser.getPhoneNumber());
//                Log.d(TAG, "getUserFirstName: " + user.getFirstName());
//                Log.d(TAG, "getUserId: " + user.getUserId());
//                Log.d(TAG, "getUserLastName: " + user.getLastName());
//                Log.d(TAG, "getUserAccessToken: " + user.getAccessToken());
//                Log.d(TAG, "getUserRefreshToken: " + user.getRefreshToken());
                registerUseCase.saveUserData(user);
                mutableLiveDataResponse.setValue("Your account was created successfully");

            }, throwable -> {
                Error error = new Error();
                String errorMsg = error.errorType(throwable);
                Log.d(TAG, "error is:" + errorMsg);
                mutableLiveDataResponse.setValue(errorMsg);

            });
        } catch (Exception e) {
            Log.d(TAG, "getUser: " + e);
        }
    }

    public Boolean checkUserValidation() {
        mutableLiveDataErrors.postValue(userValidation.checkValidations());
        if (userValidation.isValidUser()) {
            return true;
        }
        return false;
    }
}