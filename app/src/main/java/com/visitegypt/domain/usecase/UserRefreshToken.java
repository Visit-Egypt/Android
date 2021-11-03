package com.visitegypt.domain.usecase;

import com.visitegypt.data.source.remote.RetrofitService;
import com.visitegypt.domain.model.User;
import com.visitegypt.domain.repository.UserRepository;
import com.visitegypt.domain.usecase.base.SingleUseCase;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Single;

public class UserRefreshToken extends SingleUseCase<User> {
    private UserRepository userRepository;
    private User user;
    @Inject
    public UserRefreshToken(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    protected Single<User> buildSingleUseCase() {
        return userRepository.refreshUserToken(user);
    }
}
