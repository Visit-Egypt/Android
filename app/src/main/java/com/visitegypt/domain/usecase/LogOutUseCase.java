package com.visitegypt.domain.usecase;

import static com.visitegypt.utils.Constants.SHARED_PREF_USER_ACCESS_TOKEN;
import static com.visitegypt.utils.Constants.SHARED_PREF_USER_REFRESH_TOKEN;

import android.content.SharedPreferences;
import android.util.Log;

import com.visitegypt.domain.model.Token;
import com.visitegypt.domain.model.User;
import com.visitegypt.domain.repository.UserRepository;
import com.visitegypt.domain.usecase.base.SingleUseCase;

import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Provides;
import io.reactivex.rxjava3.core.Single;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class LogOutUseCase extends SingleUseCase<String> {

    UserRepository userRepository;
    SharedPreferences sharedPreferences;
    private String userId;


    @Inject
    public LogOutUseCase(@Named("Normal") UserRepository userRepository, SharedPreferences sharedPreferences) {
        this.userRepository = userRepository;
        this.sharedPreferences = sharedPreferences;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    protected Single<String> buildSingleUseCase() {
        return userRepository.logOut(userId);
    }
}
