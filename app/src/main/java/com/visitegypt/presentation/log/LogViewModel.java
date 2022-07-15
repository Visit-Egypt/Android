package com.visitegypt.presentation.log;

import android.content.SharedPreferences;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.Gson;
import com.visitegypt.domain.model.Token;
import com.visitegypt.domain.model.User;
import com.visitegypt.domain.usecase.ForgotPasswordUseCase;
import com.visitegypt.domain.usecase.GetGoogleLoginTokenUseCase;
import com.visitegypt.domain.usecase.GetGoogleRegisterTokenUseCase;
import com.visitegypt.domain.usecase.GetUserUseCase;
import com.visitegypt.domain.usecase.LoginUserUseCase;
import com.visitegypt.domain.usecase.RegisterDeviceToNotificationUseCase;
import com.visitegypt.domain.usecase.RegisterUseCase;
import com.visitegypt.domain.usecase.UpdateUserBadgeTaskProgUseCase;
import com.visitegypt.domain.usecase.UserValidation;
import com.visitegypt.utils.error.Error;

import org.json.JSONObject;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import okhttp3.ResponseBody;
import retrofit2.HttpException;

@HiltViewModel
public class LogViewModel extends ViewModel {
    private static final String TAG = "Log View Model";
    private static UserValidation userValidation;
    MutableLiveData<String> msgMutableLiveData = new MutableLiveData<>();
    MutableLiveData<User> userMutable = new MutableLiveData<>();
    MutableLiveData<String> forgotPasswordResponse = new MutableLiveData<>();
    MutableLiveData<String[]> mutableLiveDataErrors = new MutableLiveData<>();
    MutableLiveData<String> mutableLiveDataResponse = new MutableLiveData<>();
    private LoginUserUseCase loginUserUseCase;
    private SharedPreferences sharedPreferences;
    private GetUserUseCase getUserUseCase;
    private GetGoogleLoginTokenUseCase getGoogleLoginTokenUseCase;
    private ForgotPasswordUseCase forgotPasswordUseCase;
    private RegisterDeviceToNotificationUseCase registerDeviceToNotificationUseCase;
    private UpdateUserBadgeTaskProgUseCase updateUserBadgeTaskProgUseCase;
    private RegisterUseCase registerUseCase;
    private GetGoogleRegisterTokenUseCase getGoogleRegisterTokenUseCase;

    @Inject
    public LogViewModel(GetGoogleLoginTokenUseCase getGoogleLoginTokenUseCase,
                        ForgotPasswordUseCase forgotPasswordUseCase,
                        LoginUserUseCase loginUserUseCase, SharedPreferences sharedPreferences,
                        GetUserUseCase getUserUseCase,
                        RegisterDeviceToNotificationUseCase registerDeviceToNotificationUseCase,
                        GetGoogleRegisterTokenUseCase getGoogleRegisterTokenUseCase,
                        RegisterUseCase registerUseCase,
                        UpdateUserBadgeTaskProgUseCase updateUserBadgeTaskProgUseCase
    ) {
        this.loginUserUseCase = loginUserUseCase;
        this.sharedPreferences = sharedPreferences;
        this.getUserUseCase = getUserUseCase;
        this.getGoogleLoginTokenUseCase = getGoogleLoginTokenUseCase;
        this.forgotPasswordUseCase = forgotPasswordUseCase;
        this.registerDeviceToNotificationUseCase = registerDeviceToNotificationUseCase;
        this.registerUseCase = registerUseCase;
        this.sharedPreferences = sharedPreferences;
        this.updateUserBadgeTaskProgUseCase = updateUserBadgeTaskProgUseCase;
        this.getGoogleRegisterTokenUseCase = getGoogleRegisterTokenUseCase;
    }

    public void setUserValidation(UserValidation userValidation) {
        this.userValidation = userValidation;
    }

    public void getUser() {
        registerUseCase.saveUser(userValidation);
        registerUseCase.execute(u -> {
            Log.d(TAG, "getUser: " + new Gson().toJson(u));
            registerUseCase.saveUserData(userValidation);
            mutableLiveDataResponse.setValue("Your account was created successfully");
        }, throwable -> {
            mutableLiveDataResponse.setValue("Account creation failed");
            Log.e(TAG, "getUser: ", throwable);
            Log.e(TAG, "getUser: " + throwable.getMessage());

            try {
                Log.d(TAG, "getUser: " + throwable.getMessage());
                ResponseBody body = ((HttpException) throwable).response().errorBody();
                JSONObject jObjectError = new JSONObject(body.string());
                Log.d(TAG, "accept try : " + jObjectError.getJSONArray("errors"));
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

    public void signUpWithGoogle(String token, String email) {
        getGoogleRegisterTokenUseCase.setToken(new Token(token));
        getGoogleRegisterTokenUseCase.execute(user -> {
            Log.d(TAG, "donee" + user);
            registerUseCase.saveUserData(user);
            mutableLiveDataResponse.setValue("Your google account was created successfully");
        }, throwable -> {
            mutableLiveDataResponse.setValue("There is an error may You already have an account ");
            Log.e(TAG, "signUpWithGoogle: " + throwable.getMessage());
        });

    }

    public void test(String st) {
        registerDeviceToNotificationUseCase.setMsg(st);
        registerDeviceToNotificationUseCase.execute(map -> {
            Log.d(TAG, "test: " + map.get("message"));
        }, throwable -> {
            Log.e(TAG, "test: " + throwable.getMessage());
        });
    }

    public void login(User user) {
        String email = user.getEmail();
        loginUserUseCase.saveUser(user);
        loginUserUseCase.execute(user1 -> {
            Log.d(TAG, "accept: Token " + user1.getAccessToken());
            Log.d(TAG, "accept: Token " + user1.getRefreshToken());
            loginUserUseCase.saveUserData(user1);
            saveUserData(user1.getUserId(), email);
            userMutable.setValue(user1);
            msgMutableLiveData.setValue("Your login done");
        }, throwable -> {
            Log.e(TAG, "login: ", throwable);
            Error error = new Error();
            msgMutableLiveData.setValue(error.errorType(throwable));
        });
    }

    public Boolean checkUser() {
        return loginUserUseCase.isUserDataValid();
    }

    private void saveUserData(String userID, String email) {
        getUserUseCase.setUser(userID, email);
        getUserUseCase.execute(user -> getUserUseCase.saveUserData(user), throwable -> {
            try {
                ResponseBody body = ((HttpException) throwable).response().errorBody();
                JSONObject jObjectError = new JSONObject(body.string());
                Log.d(TAG, "accept try : " + jObjectError.getJSONArray("errors"));
                if (jObjectError.getJSONArray("errors").toString().contains("msg")) {
                    msgMutableLiveData.setValue(jObjectError.getJSONArray("errors").getJSONObject(0).getString("msg"));
                } else {
                    msgMutableLiveData.setValue(jObjectError.getJSONArray("errors").toString());
                }
            } catch (Exception e) {
                Log.e(TAG, "accept catch: " + e);
            }
        });
    }

    public void signInWithGoogle(String token, String email) {
        getGoogleLoginTokenUseCase.setToken(new Token(token));
        getGoogleLoginTokenUseCase.execute(user -> {
            Log.d(TAG, "donee" + user);
            loginUserUseCase.saveUserData(user);
            userMutable.setValue(user);
            msgMutableLiveData.setValue("Your google login done");
            saveUserData(user.getUserId(), email);
        }, throwable -> Log.e(TAG, "signInWithGoogle: " + throwable.getMessage()));
    }

    public void registerDeviceToNotification(String st) {
        registerDeviceToNotificationUseCase.setMsg(st);
        registerDeviceToNotificationUseCase.execute(map -> {
            Log.d(TAG, "registerDeviceToNotification: " + map.get("message"));
        }, throwable -> {
            Log.e(TAG, "registerDeviceToNotification: " + throwable.getMessage());
        });
    }

    public void forgotPassword(String email) {
        forgotPasswordUseCase.setEmail(email);
        Log.d(TAG, "forgotPassword: " + email);
        forgotPasswordUseCase.execute(s -> {
            Log.d(TAG, "forgotPassword: donee");
            forgotPasswordResponse.setValue("reset done");
        }, throwable -> {
            forgotPasswordResponse.setValue("Not found");
            Log.e(TAG, "forgetpassword: " + throwable.getMessage());
        });
    }
}