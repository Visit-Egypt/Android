package com.visitegypt.domain.usecase;

import android.content.SharedPreferences;

import androidx.core.graphics.drawable.IconCompat;

import com.visitegypt.domain.model.User;
import com.visitegypt.domain.repository.UserRepository;
import com.visitegypt.domain.usecase.base.SingleUseCase;
import com.visitegypt.utils.Constants;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Single;

public class UpdateUserUseCase extends SingleUseCase<User> {

    private UserRepository userRepository;
    private SharedPreferences sharedPreferences;

    @Inject
    public UpdateUserUseCase(UserRepository userRepository, SharedPreferences sharedPreferences) {
        this.userRepository = userRepository;
        this.sharedPreferences = sharedPreferences;
    }

    public void setUser(User user) {
        this.user = user;
    }

    private User user = new User();


    @Override
    protected Single<User> buildSingleUseCase() {
        return userRepository.updateUser(sharedPreferences.getString(Constants.SHARED_PREF_USER_ID,null), user);
    }
}
