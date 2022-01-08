package com.visitegypt.ui.setting;

import android.content.SharedPreferences;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.visitegypt.domain.model.User;
import com.visitegypt.domain.usecase.GetUserUseCase;
import com.visitegypt.utils.Constants;

import org.json.JSONObject;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.functions.Consumer;
import okhttp3.ResponseBody;
import retrofit2.HttpException;

@HiltViewModel
public class SettingViewModel extends ViewModel {
    private static final String TAG = "Setting View Model";
    GetUserUseCase getUserUseCase;
    SharedPreferences sharedPreferences;
    MutableLiveData<User> mutableLiveDataUser = new MutableLiveData<>();
    @Inject
    public SettingViewModel(GetUserUseCase getUserUseCase, SharedPreferences sharedPreferences) {
        this.getUserUseCase = getUserUseCase;
        this.sharedPreferences = sharedPreferences;
    }

    public void getUserData()
    {

        getUserUseCase.setUser(sharedPreferences.getString(Constants.SHARED_PREF_USER_ID,null),
                sharedPreferences.getString(Constants.SHARED_PREF_EMAIL,null));
        getUserUseCase.execute(new Consumer<User>() {
            @Override
            public void accept(User user) throws Throwable {
                Log.d("TAG", "onChanged: setting ++" + user.getFirstName());
                mutableLiveDataUser.setValue(user);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Throwable {
                try {
                    ResponseBody body = ((HttpException) throwable).response().errorBody();
                    JSONObject jObjectError = new JSONObject(body.string());
                    Log.d("TAG", "accept try : " + jObjectError.getJSONArray("errors").toString());
                    if (jObjectError.getJSONArray("errors").toString().contains("msg")) {


                    } else {

                    }
                } catch (Exception e) {
                    Log.d("TAG", "accept catch: " + e.toString());
                }
            }
        });
    }
}

