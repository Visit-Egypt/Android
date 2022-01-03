package com.visitegypt.presentation.signin;

import static com.visitegypt.utils.Constants.SHARED_PREF_USER_ID;

import android.content.SharedPreferences;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.visitegypt.domain.model.User;
import com.visitegypt.domain.usecase.GetUserUseCase;
import com.visitegypt.domain.usecase.LoginUserUseCase;
import com.visitegypt.utils.Encryption;

import org.json.JSONObject;

import java.security.NoSuchAlgorithmException;

import javax.crypto.NoSuchPaddingException;
import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.functions.Consumer;
import okhttp3.ResponseBody;
import retrofit2.HttpException;

@HiltViewModel
public class SignInViewModel extends ViewModel {
    private static final String TAG = "Sign in view model test";
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
        loginUserUseCase.saveUser(user);
        loginUserUseCase.execute(new Consumer<User>() {
            @Override
            public void accept(User user) throws Throwable {
                Log.d("TAG", "accept: Token "+user.getAccessToken());
                Log.d("TAG", "accept: Token "+user.getRefreshToken());
                loginUserUseCase.saveUserData(user);
                msgMutableLiveData.setValue("Your login done");
                //saveUserData();
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
    public Boolean checkUser()
    {
        return loginUserUseCase.isUserDataValid();
    }
/*
    private void saveUserData() {
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
                    Log.d("TAG", "accept catch: " + e.toString());
                }
            }
        });
    }

 */
}