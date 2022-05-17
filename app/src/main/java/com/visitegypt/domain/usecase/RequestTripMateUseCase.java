package com.visitegypt.domain.usecase;

import com.visitegypt.domain.model.TripMateRequest;
import com.visitegypt.domain.model.User;
import com.visitegypt.domain.repository.UserRepository;
import com.visitegypt.domain.usecase.base.SingleUseCase;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.rxjava3.core.Single;

public class RequestTripMateUseCase extends SingleUseCase<User> {
    private String userId;
    UserRepository userRepository;
    private TripMateRequest requestMateBody;

    @Inject
    public RequestTripMateUseCase(@Named("Normal") UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public TripMateRequest getRequestMateBody() {
        return requestMateBody;
    }

    public void setRequestMateBody(TripMateRequest requestMateBody) {
        this.requestMateBody = requestMateBody;
    }

    @Override
    protected Single<User> buildSingleUseCase() {

        return userRepository.requestTripMate(userId, requestMateBody);
    }
}
