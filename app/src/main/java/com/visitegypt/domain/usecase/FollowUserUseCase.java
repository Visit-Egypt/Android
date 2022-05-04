package com.visitegypt.domain.usecase;

import com.visitegypt.domain.repository.UserRepository;
import com.visitegypt.domain.usecase.base.SingleUseCase;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.rxjava3.core.Single;

public class FollowUserUseCase extends SingleUseCase<Boolean> {
    UserRepository userRepository;
    private String userId;

    @Inject
    public FollowUserUseCase(@Named("Normal") UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    protected Single<Boolean> buildSingleUseCase() {
        return userRepository.follow(userId);
    }
}
