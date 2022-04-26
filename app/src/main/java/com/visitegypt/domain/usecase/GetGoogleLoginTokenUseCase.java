package com.visitegypt.domain.usecase;

import static com.visitegypt.utils.Constants.SHARED_PREF_USER_ACCESS_TOKEN;
import static com.visitegypt.utils.Constants.SHARED_PREF_USER_REFRESH_TOKEN;

import android.content.SharedPreferences;
import android.util.Log;

import com.visitegypt.domain.model.Token;
import com.visitegypt.domain.model.User;
import com.visitegypt.domain.repository.UserRepository;
import com.visitegypt.domain.usecase.base.SingleUseCase;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.rxjava3.core.Single;

public class GetGoogleLoginTokenUseCase extends SingleUseCase<User> {

    UserRepository userRepository;
    SharedPreferences sharedPreferences;
    private Token token;

    @Inject
    public GetGoogleLoginTokenUseCase(@Named("Normal") UserRepository userRepository, SharedPreferences sharedPreferences) {
        this.userRepository = userRepository;
        this.sharedPreferences = sharedPreferences;
    }


    public void setToken(Token token) {
        this.token = token;
    }

    public void getNewToken() {
        this.execute(user -> {
            sharedPreferences.edit()
                    .putString(SHARED_PREF_USER_ACCESS_TOKEN, user.getAccessToken())
                    .putString(SHARED_PREF_USER_REFRESH_TOKEN, user.getRefreshToken())
                    .apply();

        }, throwable -> {
            Log.d("TAG", "getNewToken: " + throwable);

        });
    }

    @Override
    protected Single<User> buildSingleUseCase() {
        return userRepository.googleLoginUserToken(token);
    }
}
