package com.visitegypt.domain.usecase;

import android.content.SharedPreferences;

import com.visitegypt.domain.model.User;
import com.visitegypt.domain.repository.UserRepository;
import com.visitegypt.domain.usecase.base.SingleUseCase;
import com.visitegypt.utils.Constants;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.rxjava3.core.Single;

public class LoginUserUseCase extends SingleUseCase<User> {
    UserRepository userRepository;
    SharedPreferences sharedPreferences;
    User user;

    @Inject
    public LoginUserUseCase(@Named("Normal") UserRepository userRepository, SharedPreferences sharedPreferences) {
        this.userRepository = userRepository;
        this.sharedPreferences = sharedPreferences;
    }

    public void saveUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void saveUserData(User user) {

        sharedPreferences.edit()
                .putString(Constants.SHARED_PREF_USER_ACCESS_TOKEN, user.getAccessToken())
                .putString(Constants.SHARED_PREF_TOKEN_TYPE, user.getTokenType())
                .putString(Constants.SHARED_PREF_USER_REFRESH_TOKEN, user.getRefreshToken())
                .putString(Constants.SHARED_PREF_USER_ID, user.getUserId())
                .putString(Constants.SHARED_PREF_FIRST_NAME, user.getFirstName() + " " + user.getLastName())
                .apply();
    }

    public Boolean isUserDataValid() {
        if ((sharedPreferences.getString(Constants.SHARED_PREF_USER_ACCESS_TOKEN, null) != null)
                && (sharedPreferences.getString(Constants.SHARED_PREF_USER_REFRESH_TOKEN, null) != null)
                && (sharedPreferences.getString(Constants.SHARED_PREF_USER_ID, null) != null)
        ) {
            return true;
        }
        return false;

    }

    @Override
    protected Single<User> buildSingleUseCase() {
        return userRepository.loginUser(user);
    }
}