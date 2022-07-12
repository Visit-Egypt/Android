package com.visitegypt.domain.usecase;

import android.util.Log;

import com.visitegypt.domain.model.XPUpdate;
import com.visitegypt.domain.repository.UserRepository;
import com.visitegypt.domain.usecase.base.SingleUseCase;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.rxjava3.core.Single;

public class UpdateReviewActivityUseCase extends SingleUseCase<XPUpdate> {
    private static final String TAG = "updateReviewUseCase";

    private UserRepository userRepository;
    private String placeId;

    @Inject
    public UpdateReviewActivityUseCase(@Named("Normal") UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    protected Single<XPUpdate> buildSingleUseCase() {
        if (placeId == null) {
            Log.e(TAG, "buildSingleUseCase: must call setPlaceId");
        }
        return userRepository.updateUserReviewPlaceActivity(placeId);
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }
}
