package com.visitegypt.domain.usecase;

import com.visitegypt.domain.model.User;
import com.visitegypt.domain.repository.UserRepository;
import com.visitegypt.domain.usecase.base.SingleUseCase;

import io.reactivex.rxjava3.core.Single;

public class RegisterUseCase extends SingleUseCase<User> {
    private UserRepository userRepository;
    private User user;

    public RegisterUseCase(UserRepository userRepository, User user) {
        this.userRepository = userRepository;
        this.user = user;
    }

    @Override
    protected Single<User> buildSingleUseCase() {
        return userRepository.registerUser(user) ;
    }
}
