package com.visitegypt.domain.usecase;

import android.content.SharedPreferences;

import com.visitegypt.domain.model.User;
import com.visitegypt.domain.repository.UserRepository;
import com.visitegypt.domain.usecase.base.SingleUseCase;
import com.visitegypt.utils.Constants;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Single;

public class GetUserUseCase extends SingleUseCase<User> {
    private String userId;
    private String email;
    private SharedPreferences sharedPreferences;
    private UserRepository userRepository;

    @Inject
    public GetUserUseCase(UserRepository userRepository, SharedPreferences sharedPreferences) {
        this.userRepository = userRepository;
        this.sharedPreferences = sharedPreferences;
    }

    public void setUser(String userId, String email, String userAccessToken) {
        this.userId = userId;
        this.email = email;
    }

    public void saveUserData(User user) {
        sharedPreferences.edit()
                .putString(Constants.SHARED_PREF_FIRST_NAME, user.getFirstName())
                .putString(Constants.SHARED_PREF_LAST_NAME, user.getLastName())
                .putString(Constants.SHARED_PREF_PHONE_NUMBER, user.getPhoneNumber())
                .apply();
    }
    @Override
    protected Single<User> buildSingleUseCase() {
        return userRepository.getUser(userId, email);
    }
}
