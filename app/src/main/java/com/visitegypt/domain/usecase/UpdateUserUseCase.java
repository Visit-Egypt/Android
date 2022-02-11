package com.visitegypt.domain.usecase;

import android.content.SharedPreferences;

import com.visitegypt.domain.model.Review;
import com.visitegypt.domain.model.User;
import com.visitegypt.domain.model.UserUpdateRequest;
import com.visitegypt.domain.repository.UserRepository;
import com.visitegypt.domain.usecase.base.SingleUseCase;
import com.visitegypt.utils.Constants;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.rxjava3.core.Single;

public class UpdateUserUseCase extends SingleUseCase<User> {

    private UserRepository userRepository;
    private SharedPreferences sharedPreferences;
    private UserUpdateRequest userUpdateRequest;

    @Inject
    public UpdateUserUseCase(@Named("Normal") UserRepository userRepository, SharedPreferences sharedPreferences) {
        this.userRepository = userRepository;
        this.sharedPreferences = sharedPreferences;
    }

    public void setUser(UserUpdateRequest userUpdateRequest) {
        this.userUpdateRequest = userUpdateRequest;
    }


    @Override
    protected Single<User> buildSingleUseCase() {
        return userRepository.updateUser(sharedPreferences.getString(Constants.SHARED_PREF_USER_ID,null), userUpdateRequest);

    }
}
