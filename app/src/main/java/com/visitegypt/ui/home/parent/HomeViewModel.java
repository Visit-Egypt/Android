package com.visitegypt.ui.home.parent;

import android.content.SharedPreferences;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import static com.visitegypt.utils.Constants.SHARED_PREF_EMAIL;
import static com.visitegypt.utils.Constants.SHARED_PREF_FIRST_NAME;
import static com.visitegypt.utils.Constants.SHARED_PREF_LAST_NAME;
import static com.visitegypt.utils.Constants.SHARED_PREF_USER_ID;

import com.visitegypt.domain.usecase.LogOutUseCase;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class HomeViewModel extends ViewModel {
    SharedPreferences sharedPreferences;
    LogOutUseCase logOutUseCase;
    MutableLiveData<String> userNameMutable = new MutableLiveData<>();
    MutableLiveData<String> userEmailMutable = new MutableLiveData<>();
    MutableLiveData<Boolean> isLoged = new MutableLiveData<>();

    @Inject
    public HomeViewModel(SharedPreferences sharedPreferences, LogOutUseCase logOutUseCase) {
        this.sharedPreferences = sharedPreferences;
        this.logOutUseCase = logOutUseCase;
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


}
