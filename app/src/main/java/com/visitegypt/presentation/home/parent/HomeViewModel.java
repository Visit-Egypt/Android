package com.visitegypt.presentation.home.parent;

import android.content.SharedPreferences;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import static com.visitegypt.utils.Constants.SHARED_PREF_EMAIL;
import static com.visitegypt.utils.Constants.SHARED_PREF_FIRST_NAME;
import static com.visitegypt.utils.Constants.SHARED_PREF_USER_ID;
import static com.visitegypt.utils.Constants.SHARED_PREF_USER_IMAGE;

import com.visitegypt.domain.model.User;
import com.visitegypt.domain.usecase.GetUserUseCase;
import com.visitegypt.domain.usecase.LogOutUseCase;
import com.visitegypt.utils.Constants;

import org.json.JSONObject;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.functions.Consumer;
import okhttp3.ResponseBody;
import retrofit2.HttpException;

@HiltViewModel
public class HomeViewModel extends ViewModel {
    SharedPreferences sharedPreferences;
    LogOutUseCase logOutUseCase;
    GetUserUseCase getUserUseCase;
    MutableLiveData<String> userNameMutable = new MutableLiveData<>();
    MutableLiveData<String> userEmailMutable = new MutableLiveData<>();
    MutableLiveData<Boolean> isLoged = new MutableLiveData<>();
    public MutableLiveData<User> mutableLiveDataUser = new MutableLiveData<>();

    @Inject
    public HomeViewModel(SharedPreferences sharedPreferences, LogOutUseCase logOutUseCase, GetUserUseCase getUserUseCase) {
        this.sharedPreferences = sharedPreferences;
        this.logOutUseCase = logOutUseCase;
        this.getUserUseCase = getUserUseCase;
    }

    public void getUserInfo() {
        //This is not right
        Boolean flag = true;
        while (flag) {
            if (sharedPreferences.getString(SHARED_PREF_FIRST_NAME, null) != null) {
                break;
            }
        }


        userNameMutable.setValue(sharedPreferences.getString(SHARED_PREF_FIRST_NAME, null));
        userEmailMutable.setValue(sharedPreferences.getString(SHARED_PREF_EMAIL, null));
    }

    public void logOut() {
        logOutUseCase.setUserId(sharedPreferences.getString(SHARED_PREF_USER_ID, null));
        logOutUseCase.execute(s -> {
            sharedPreferences.edit().clear().commit();
            isLoged.setValue(false);
        }, throwable -> {
            Log.d("TAG", "logOut: " + throwable.getMessage());

        });
    }

    public void getUserData() {

        getUserUseCase.setUser(sharedPreferences.getString(SHARED_PREF_USER_ID, null),
                sharedPreferences.getString(Constants.SHARED_PREF_EMAIL, null));
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
    public void saveUserImage(String url)
    {
        sharedPreferences.edit().putString(SHARED_PREF_USER_IMAGE,url).apply();

    }

}
