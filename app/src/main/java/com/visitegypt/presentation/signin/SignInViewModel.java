package com.visitegypt.presentation.signin;

import android.content.SharedPreferences;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.visitegypt.domain.model.Token;
import com.visitegypt.domain.model.User;
import com.visitegypt.domain.usecase.ForgotPasswordUseCase;
import com.visitegypt.domain.usecase.GetGoogleLoginTokenUseCase;
import com.visitegypt.domain.usecase.GetUserUseCase;
import com.visitegypt.domain.usecase.LoginUserUseCase;
import com.visitegypt.domain.usecase.RegisterDeviceToNotificationUseCase;

import org.json.JSONObject;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.functions.Consumer;
import okhttp3.ResponseBody;
import retrofit2.HttpException;

@HiltViewModel
public class SignInViewModel extends ViewModel {
    private static final String TAG = "Sign in view model test";

    MutableLiveData<String> msgMutableLiveData = new MutableLiveData<>();
    MutableLiveData<User> userMutable = new MutableLiveData<>();
    MutableLiveData<String> forgotPasswordResponse = new MutableLiveData<>();

    private LoginUserUseCase loginUserUseCase;
    private SharedPreferences sharedPreferences;
    private GetUserUseCase getUserUseCase;
    private GetGoogleLoginTokenUseCase getGoogleLoginTokenUseCase;
    private ForgotPasswordUseCase forgotPasswordUseCase;
    private RegisterDeviceToNotificationUseCase registerDeviceToNotificationUseCase;

    @Inject
    public SignInViewModel(GetGoogleLoginTokenUseCase getGoogleLoginTokenUseCase,
                           ForgotPasswordUseCase forgotPasswordUseCase,
                           LoginUserUseCase loginUserUseCase, SharedPreferences sharedPreferences,
                           GetUserUseCase getUserUseCase
            , RegisterDeviceToNotificationUseCase registerDeviceToNotificationUseCase

    ) {
        this.loginUserUseCase = loginUserUseCase;
        this.sharedPreferences = sharedPreferences;
        this.getUserUseCase = getUserUseCase;
        this.getGoogleLoginTokenUseCase = getGoogleLoginTokenUseCase;
        this.forgotPasswordUseCase = forgotPasswordUseCase;
        this.registerDeviceToNotificationUseCase = registerDeviceToNotificationUseCase;
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

    public void registerDeviceToNotification(String st)
    {
        registerDeviceToNotificationUseCase.setMsg(st);
        registerDeviceToNotificationUseCase.execute(map -> {
            Log.d(TAG, "test: " + map.get("message"));
        },throwable -> {
            Log.d(TAG, "test: "+ throwable.getMessage());
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