package com.visitegypt.domain.usecase;

import com.visitegypt.domain.model.PlaceActivity;
import com.visitegypt.domain.repository.UserRepository;
import com.visitegypt.domain.usecase.base.SingleUseCase;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.rxjava3.core.Single;

public class GetUserPlaceActivityUseCase extends SingleUseCase<List<PlaceActivity>> {
    UserRepository userRepository;
    private String userId;

    @Inject
    public GetUserPlaceActivityUseCase(@Named("Normal") UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    protected Single<List<PlaceActivity>> buildSingleUseCase() {
        return userRepository.getUserPlaceActivity(userId);
    }
}
