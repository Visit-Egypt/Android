package com.visitegypt.presentation.log;

import android.content.SharedPreferences;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

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

import org.json.JSONObject;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.functions.Consumer;
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
                        GetUserUseCase getUserUseCase
            , RegisterDeviceToNotificationUseCase registerDeviceToNotificationUseCase,
                        GetGoogleRegisterTokenUseCase getGoogleRegisterTokenUseCase, RegisterUseCase registerUseCase,
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

    public void test(String st) {
        registerDeviceToNotificationUseCase.setMsg(st);
        registerDeviceToNotificationUseCase.execute(map -> {
            Log.d(TAG, "test: " + map.get("message"));
        }, throwable -> {
            Log.d(TAG, "test: " + throwable.getMessage());
        });
    }

    public void login(User user) {
        String email = user.getEmail();
        loginUserUseCase.saveUser(user);
        loginUserUseCase.execute(new Consumer<User>() {
            @Override
            public void accept(User user) throws Throwable {
                Log.d("TAG", "accept: Token " + user.getAccessToken());
                Log.d("TAG", "accept: Token " + user.getRefreshToken());
                loginUserUseCase.saveUserData(user);
                saveUserData(user.getUserId(), email);
                userMutable.setValue(user);
                msgMutableLiveData.setValue("Your login done");

            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Throwable {
                try {
                    ResponseBody body = ((HttpException) throwable).response().errorBody();
                    JSONObject jObjectError = new JSONObject(body.string());
                    Log.d("TAG", "accept try : " + jObjectError.getJSONArray("errors").toString());
                    if (jObjectError.getJSONArray("errors").toString().contains("msg")) {

                        msgMutableLiveData.setValue(jObjectError.getJSONArray("errors").getJSONObject(0).getString("msg"));
                    } else {
                        msgMutableLiveData.setValue(jObjectError.getJSONArray("errors").toString());
                    }
                } catch (Exception e) {
                    Log.d("TAG", "accept catch: " + e.toString());
                }
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
                try {
                    ResponseBody body = ((HttpException) throwable).response().errorBody();
                    JSONObject jObjectError = new JSONObject(body.string());
                    Log.d("TAG", "accept try : " + jObjectError.getJSONArray("errors").toString());
                    if (jObjectError.getJSONArray("errors").toString().contains("msg")) {

                        msgMutableLiveData.setValue(jObjectError.getJSONArray("errors").getJSONObject(0).getString("msg"));
                    } else {
                        msgMutableLiveData.setValue(jObjectError.getJSONArray("errors").toString());
                    }
                } catch (Exception e) {
                    Log.e("TAG", "accept catch: " + e.toString());
                }
            }
        });
    }

    public void signInWithGoogle(String token, String email) {
        getGoogleLoginTokenUseCase.setToken(new Token(token));
        getGoogleLoginTokenUseCase.execute(new Consumer<User>() {
            @Override
            public void accept(User user) throws Throwable {
                Log.d("TAG", "donee" + user);
                loginUserUseCase.saveUserData(user);
                userMutable.setValue(user);
                msgMutableLiveData.setValue("Your google login done");
                saveUserData(user.getUserId(), email);
            }

        }, throwable -> Log.e(TAG, "signInWithGoogle: " + throwable.getMessage()));

    }

    public void registerDeviceToNotification(String st) {
        registerDeviceToNotificationUseCase.setMsg(st);
        registerDeviceToNotificationUseCase.execute(map -> {
            Log.d(TAG, "test: " + map.get("message"));
        }, throwable -> {
            Log.d(TAG, "test: " + throwable.getMessage());
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
            Log.e("TAG", "forgetpassword: " + throwable.getMessage());

        });

    }
}