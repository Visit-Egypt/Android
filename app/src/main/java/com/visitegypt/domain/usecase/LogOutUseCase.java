package com.visitegypt.domain.usecase;

import android.content.SharedPreferences;

import com.visitegypt.domain.repository.UserRepository;
import com.visitegypt.domain.usecase.base.SingleUseCase;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.rxjava3.core.Single;

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
