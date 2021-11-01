package com.visitegypt.domain.usecase;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.visitegypt.domain.model.User;
import com.visitegypt.domain.repository.UserRepository;
import com.visitegypt.domain.usecase.base.SingleUseCase;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Single;

public class LoginUserUseCase extends SingleUseCase<User> {
    UserRepository userRepository;
    SharedPreferences sharedPreferences;
    User user;

    @Inject
    public LoginUserUseCase(UserRepository userRepository,SharedPreferences sharedPreferences ) {
        this.userRepository = userRepository;
        this.sharedPreferences = sharedPreferences;
    }

    public void saveUser(User user) {
        this.user = user;
    }
    public void saveUserData(User user)
    {

        sharedPreferences.edit()
                .putString("accessToken",user.getAccessToken())
                .putString("tokenType",user.getTokenType())
                .putString("refreshToken",user.getRefreshToken())
                .putString("userId",user.getUserId())
                .commit();
    }


    @Override
    protected Single<User> buildSingleUseCase() {
        return userRepository.loginUser(user);
    }
}