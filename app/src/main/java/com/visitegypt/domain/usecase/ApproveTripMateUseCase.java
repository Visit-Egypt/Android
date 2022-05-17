package com.visitegypt.domain.usecase;

import com.visitegypt.domain.model.User;
import com.visitegypt.domain.repository.UserRepository;
import com.visitegypt.domain.usecase.base.SingleUseCase;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.rxjava3.core.Single;

public class ApproveTripMateUseCase extends SingleUseCase<User> {
    private String requestId;
    private UserRepository userRepository;
    @Inject
    public ApproveTripMateUseCase(@Named("Normal") UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    @Override
    protected Single<User> buildSingleUseCase() {
        return userRepository.approveTripMateRequest(requestId) ;
    }
}
