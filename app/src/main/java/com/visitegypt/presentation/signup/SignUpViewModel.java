package com.visitegypt.presentation.signup;

import android.content.SharedPreferences;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.visitegypt.domain.model.User;
import com.visitegypt.domain.usecase.RegisterUseCase;
import com.visitegypt.domain.usecase.UserValidation;

import org.json.JSONObject;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.functions.Consumer;
import okhttp3.ResponseBody;
import retrofit2.HttpException;
import retrofit2.Response;

@HiltViewModel
public class SignUpViewModel extends ViewModel {
    MutableLiveData<String[]> mutableLiveDataErrors = new MutableLiveData<>();
    MutableLiveData<String> mutableLiveDataResponse = new MutableLiveData<>();
    private static UserValidation userValidation;
    SharedPreferences sharedPreferences;
    public void setUserValidation(UserValidation userValidation) {
        this.userValidation = userValidation;
    }

    private RegisterUseCase registerUseCase;

    @Inject
    public SignUpViewModel(RegisterUseCase registerUseCase , SharedPreferences sharedPreferences) {
        this.registerUseCase = registerUseCase;
        this.sharedPreferences = sharedPreferences;
    }

    public void getUser() {
            User myUser = userValidation;
            registerUseCase.saveUser(userValidation);
            try {
                registerUseCase.execute(new Consumer<User>() {
                    @Override
                    public void accept(User user) throws Throwable {
                        user.setFirstName(myUser.getFirstName());
                        user.setLastName(myUser.getLastName());
                        user.setEmail(myUser.getEmail());
                        user.setPhoneNumber(myUser.getPhoneNumber());
                        Log.d("TAG", "getUser: " + user.getFirstName());
                        Log.d("TAG", "getUser: " + user.getUserId());
                        Log.d("TAG", "getUser: " + user.getLastName());
                        Log.d("TAG", "getUser: " + user.getAccessToken());
                        Log.d("TAG", "getUser: " + user.getRefreshToken());
                        registerUseCase.saveUserData(user);
                        mutableLiveDataResponse.setValue("Your account was created successfully");
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Throwable {
                        try {
                            ResponseBody body = ((HttpException) throwable).response().errorBody();

                            JSONObject jObjectError = new JSONObject(body.string());
                            Log.d("TAG", "accept try : " + jObjectError.getJSONArray("errors").toString());
                            if (jObjectError.getJSONArray("errors").toString().contains("msg")) {

                                mutableLiveDataResponse.setValue(jObjectError.getJSONArray("errors").getJSONObject(0).getString("msg"));
                            } else {
                                mutableLiveDataResponse.setValue(jObjectError.getJSONArray("errors").toString());
                            }
                        } catch (Exception e) {
                            Log.d("TAG", "accept catch: " + e.toString());
                        }
                    }
                });
            } catch (Exception e) {
                Log.d("TAG", "getUser: " + e);
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