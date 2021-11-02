package com.visitegypt.presentation.signup;

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

@HiltViewModel
public class SignUpViewModel extends ViewModel {
    MutableLiveData<String[]> mutableLiveDataErrors = new MutableLiveData<>();
    MutableLiveData<String> mutableLiveDataResponse = new MutableLiveData<>();

    private RegisterUseCase registerUseCase;

    @Inject
    public SignUpViewModel(RegisterUseCase registerUseCase) {
        this.registerUseCase = registerUseCase;
    }

    public void getUser(UserValidation userValidation) {
        mutableLiveDataErrors.postValue(userValidation.checkValidations());
        if (userValidation.isValidUser()) {
            Log.d("TAG", "getUser: This is valid User");
            User myUser = userValidation;
            registerUseCase.saveUser(userValidation);
            Log.d("TAG", "getUser: " + myUser.getFirstName() + myUser.getLastName());
            try {
                registerUseCase.execute(new Consumer<User>() {
                    @Override
                    public void accept(User user) throws Throwable {
                        mutableLiveDataResponse.setValue("Your account was created successfully, please Sign in");

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Throwable {
                        try {
                            ResponseBody body = ((HttpException) throwable).response().errorBody();
                            JSONObject jObjectError  = new JSONObject(body.string());
                            Log.d("TAG", "accept try : " + jObjectError.getJSONArray("errors").toString());
                            if(jObjectError.getJSONArray("errors").toString().contains("msg")) {

                                mutableLiveDataResponse.setValue( jObjectError.getJSONArray("errors").getJSONObject(0).getString("msg"));
                            } else {
                                mutableLiveDataResponse.setValue( jObjectError.getJSONArray("errors").toString());
                            }
                        }catch (Exception e) {
                            Log.d("TAG", "accept catch: " + e.toString());
                        }
                    }
                });
            }catch (Exception e) {
                Log.d("TAG", "getUser: " + e);
            }

        }


    }
}