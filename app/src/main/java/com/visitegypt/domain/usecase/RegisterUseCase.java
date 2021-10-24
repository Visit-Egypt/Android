package com.visitegypt.domain.usecase;

import com.visitegypt.domain.model.User;
import com.visitegypt.domain.repository.UserRepository;
import com.visitegypt.domain.usecase.base.SingleUseCase;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Single;

public class RegisterUseCase extends SingleUseCase<User> {
    private UserRepository userRepository;
    private User user;

    @Inject
    public RegisterUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void saveUser(User user) {
        this.user = user;
    }

    @Override
    protected Single<User> buildSingleUseCase() {
        return userRepository.registerUser(user);
    }
}
