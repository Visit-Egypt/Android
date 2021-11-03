package com.visitegypt.domain.usecase;

import android.content.SharedPreferences;

import com.visitegypt.domain.model.User;
import com.visitegypt.domain.repository.UserRepository;
import com.visitegypt.domain.usecase.base.SingleUseCase;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Single;

public class GetUser extends SingleUseCase<User> {
    private String userId;
    private String email;
    private String userAccessToken;
    private SharedPreferences sharedPreferences;
    private UserRepository userRepository;

    @Inject
    public GetUser(UserRepository userRepository, SharedPreferences sharedPreferences) {
        this.userRepository = userRepository;
        this.sharedPreferences = sharedPreferences;
    }

    public void setUser(String userId, String email, String userAccessToken) {
        this.userId = userId;
        this.email = email;
        this.userAccessToken = userAccessToken;
    }

    public void saveUserData(User user) {

        sharedPreferences.edit()
                .putString("firstName", user.getFirstName())
                .putString("lastName", user.getLastName())
                .putString("phoneNumber", user.getPhoneNumber())
                .commit();
    }

    @Override
    protected Single<User> buildSingleUseCase() {
        return userRepository.getUser(userId, email, "Bearer " + userAccessToken);
    }
}
