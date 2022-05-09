package com.visitegypt.domain.usecase;

import android.content.SharedPreferences;

import com.visitegypt.domain.repository.UserRepository;
import com.visitegypt.domain.usecase.base.SingleUseCase;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.rxjava3.core.Single;

public class ForgotPasswordUseCase extends SingleUseCase<String> {

    UserRepository userRepository;
    SharedPreferences sharedPreferences;
    private String email;


    @Inject
    public ForgotPasswordUseCase(@Named("Normal") UserRepository userRepository, SharedPreferences sharedPreferences) {
        this.userRepository = userRepository;
        this.sharedPreferences = sharedPreferences;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    protected Single<String> buildSingleUseCase() {
        return userRepository.forgotPassword(email);
    }
}
