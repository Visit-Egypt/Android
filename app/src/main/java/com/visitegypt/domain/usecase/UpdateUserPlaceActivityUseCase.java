package com.visitegypt.domain.usecase;

import com.visitegypt.domain.model.PlaceActivity;
import com.visitegypt.domain.repository.UserRepository;
import com.visitegypt.domain.usecase.base.SingleUseCase;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.rxjava3.core.Single;

public class UpdateUserPlaceActivityUseCase extends SingleUseCase<List<PlaceActivity>> {
    UserRepository userRepository;
    private PlaceActivity placeActivity;

    @Inject
    public UpdateUserPlaceActivityUseCase(@Named("Normal") UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public PlaceActivity getPlaceActivity() {
        return placeActivity;
    }

    public void setPlaceActivity(PlaceActivity placeActivity) {
        this.placeActivity = placeActivity;
    }

    @Override
    protected Single<List<PlaceActivity>> buildSingleUseCase() {
        return userRepository.updateUserPlaceActivity(placeActivity.getId(), placeActivity);
    }
}
