package com.visitegypt.presentation.signin;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.visitegypt.domain.model.User;
import com.visitegypt.domain.usecase.LoginUserUseCase;

import org.json.JSONObject;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.functions.Consumer;
import okhttp3.ResponseBody;
import retrofit2.HttpException;

@HiltViewModel
public class SignInViewModel extends ViewModel {
    private static final String TAG = "Cannot invoke method length() on null object";
    MutableLiveData<String> msgMutableLiveData = new MutableLiveData<>();
    private LoginUserUseCase loginUserUseCase;

    @Inject
    public SignInViewModel(LoginUserUseCase loginUserUseCase) {
        this.loginUserUseCase = loginUserUseCase;
    }

    public void login(User user) {
        loginUserUseCase.saveUser(user);
        loginUserUseCase.execute(new Consumer<User>() {
            @Override
            public void accept(User user) throws Throwable {
                msgMutableLiveData.setValue("Your login done");
                Log.d("TAG", "accept: " + user.getUserId());
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


}