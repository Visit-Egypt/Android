package com.visitegypt.domain.usecase;

import com.visitegypt.domain.model.Place;
import com.visitegypt.domain.model.User;
import com.visitegypt.domain.repository.UserRepository;
import com.visitegypt.domain.usecase.base.SingleUseCase;

import java.util.List;

import io.reactivex.rxjava3.core.Single;
import retrofit2.Call;

public class LoginUserUseCase{
    UserRepository userRepository;
    User user;

    public LoginUserUseCase(UserRepository userRepository , User user) {
        this.userRepository = userRepository;
        this.user = user;
    }

    public Call<User> buildSingleUseCase() {
        return userRepository.loginUser(user);
    }
}
