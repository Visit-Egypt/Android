package com.visitegypt.domain.usecase;

import android.util.Log;

import com.visitegypt.domain.repository.UserRepository;
import com.visitegypt.domain.usecase.base.SingleUseCase;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.rxjava3.core.Single;


public class UpdateUserChatbotArtifactUseCase extends SingleUseCase<Void> {
    private static final String TAG = "updateUserChatBotUseCase";

    private UserRepository userRepository;
    private String placeId;

    @Inject
    public UpdateUserChatbotArtifactUseCase(@Named("Normal") UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    protected Single<Void> buildSingleUseCase() {
        if (placeId == null) {
            Log.e(TAG, "buildSingleUseCase: must call setPlaceId");
        }
        return userRepository.updateChatBotArtifactUserActivity(placeId);
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }
}


