package com.visitegypt.domain.usecase;

import android.content.SharedPreferences;
import android.util.Log;

import com.visitegypt.domain.model.Token;
import com.visitegypt.domain.model.User;
import com.visitegypt.domain.repository.CallBack;
import com.visitegypt.domain.repository.UserRepository;
import com.visitegypt.domain.usecase.base.SingleUseCase;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.rxjava3.core.Single;

import static com.visitegypt.utils.Constants.SHARED_PREF_USER_ACCESS_TOKEN;
import static com.visitegypt.utils.Constants.SHARED_PREF_USER_REFRESH_TOKEN;

public class GetRefreshTokenUseCase extends SingleUseCase<User> {
    UserRepository userRepository;
    SharedPreferences sharedPreferences;

    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    CallBack callBack;

    public void setToken(Token token) {
        this.token = token;
    }


    private Token token;

    @Inject
    public GetRefreshTokenUseCase(@Named("RefreshToken") UserRepository userRepository, SharedPreferences sharedPreferences) {
        this.userRepository = userRepository;
        this.sharedPreferences = sharedPreferences;
    }

    public void getNewToken() {
        this.execute(user -> {
            sharedPreferences.edit()
                    .putString(SHARED_PREF_USER_ACCESS_TOKEN, user.getAccessToken())
                    .putString(SHARED_PREF_USER_REFRESH_TOKEN, user.getRefreshToken())
                    .apply();
            callBack.callBack(user.getAccessToken());

        }, throwable -> {
            Log.d("TAG", "getNewToken: " + throwable);

        });
    }

    @Override
    protected Single<User> buildSingleUseCase() {
        return userRepository.refreshUserToken(token);
    }
}
