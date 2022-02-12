package com.visitegypt.presentation.signin;

import android.content.SharedPreferences;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.visitegypt.domain.model.User;
import com.visitegypt.domain.usecase.GetUserUseCase;
import com.visitegypt.domain.usecase.LoginUserUseCase;
import com.visitegypt.utils.Error;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.functions.Consumer;

@HiltViewModel
public class SignInViewModel extends ViewModel {
    private static final String TAG = "Sign In View Model";
    MutableLiveData<String> msgMutableLiveData = new MutableLiveData<>();
    private LoginUserUseCase loginUserUseCase;
    private SharedPreferences sharedPreferences;
    private GetUserUseCase getUserUseCase;


    @Inject
    public SignInViewModel(LoginUserUseCase loginUserUseCase, SharedPreferences sharedPreferences, GetUserUseCase getUserUseCase) {
        this.loginUserUseCase = loginUserUseCase;
        this.sharedPreferences = sharedPreferences;
        this.getUserUseCase = getUserUseCase;
    }

    public void login(User user) {
        String email = user.getEmail();
        loginUserUseCase.saveUser(user);
        loginUserUseCase.execute(new Consumer<User>() {
            @Override
            public void accept(User user) throws Throwable {

                Log.d(TAG, "acceptAccessToken: " + user.getAccessToken());
                Log.d(TAG, "acceptRefreshToken: " + user.getRefreshToken());
                loginUserUseCase.saveUserData(user);
                saveUserData(user.getUserId(), email);
                msgMutableLiveData.setValue("Your login done");
                Log.e(TAG, "no error ");
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Throwable {
                Error error = new Error();
                String errorMsg = error.errorType(throwable);
                Log.d(TAG,"error is:"+errorMsg);
                msgMutableLiveData.setValue(errorMsg);
            }
        });
    }

    public Boolean checkUser() {
        return loginUserUseCase.isUserDataValid();
    }

    private void saveUserData(String userID, String email) {
        getUserUseCase.setUser(userID, email);
        getUserUseCase.execute(new Consumer<User>() {
            @Override
            public void accept(User user) throws Throwable {
                getUserUseCase.saveUserData(user);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Throwable {
                Error error = new Error();
                String errorMsg = error.errorType(throwable);
                Log.d(TAG, "error is:" + errorMsg);
                msgMutableLiveData.setValue(errorMsg);
            }
        });
    }
}