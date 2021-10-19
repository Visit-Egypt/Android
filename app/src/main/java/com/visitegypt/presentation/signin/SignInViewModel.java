package com.visitegypt.presentation.signin;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.visitegypt.data.repository.UserRepositoryImp;
import com.visitegypt.domain.model.User;
import com.visitegypt.domain.usecase.LoginUserUseCase;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignInViewModel  extends ViewModel {
    private static final String TAG = "Cannot invoke method length() on null object";
    MutableLiveData<String> msgMutableLiveData = new MutableLiveData<>();
    public void loginViewModel(User user)
    {
        UserRepositoryImp userRepositoryImp = new UserRepositoryImp() ;
        LoginUserUseCase loginUserUseCase = new LoginUserUseCase(userRepositoryImp,user);
        loginUserUseCase.buildSingleUseCase().enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    msgMutableLiveData.setValue("Your login done");
                } else {
                    msgMutableLiveData.setValue("Sorry, email or password is not correct");
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                msgMutableLiveData.setValue(t.toString());
            }
        });
    }


}
