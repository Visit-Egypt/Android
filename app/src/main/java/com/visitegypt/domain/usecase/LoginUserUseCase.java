package com.visitegypt.domain.usecase;

import com.visitegypt.domain.model.User;
import com.visitegypt.domain.repository.UserRepository;
import com.visitegypt.domain.usecase.base.SingleUseCase;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Single;

public class LoginUserUseCase extends SingleUseCase<User> {
    UserRepository userRepository;
    User user;

    @Inject
    public LoginUserUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void saveUser(User user) {
        this.user = user;
    }


    @Override
    protected Single<User> buildSingleUseCase() {
        return userRepository.loginUser(user);
    }
}