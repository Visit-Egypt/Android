package com.visitegypt.domain.usecase;

import android.util.Log;

import com.google.gson.annotations.SerializedName;
import com.visitegypt.domain.model.BadgeTask;
import com.visitegypt.domain.repository.UserRepository;
import com.visitegypt.domain.usecase.base.ObservableUseCase;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.rxjava3.core.Observable;

public class UpdateUserBadgeTaskProgUseCase extends ObservableUseCase<List<BadgeTask>> {
    private static final String TAG = "update badgeTask progress";
    //badgeTask Constructor BadgeTask(String badgeId, String taskTitle, int progress)
    private BadgeTask badgeTask;
    UserRepository userRepository;
    @SerializedName("badge_id")
    private String badgeId;

    @Inject
    public UpdateUserBadgeTaskProgUseCase(@Named("Normal") UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void setBadgeTask(BadgeTask badgeTask) {
        this.badgeTask = badgeTask;
    }

    @Override
    protected Observable<List<BadgeTask>> buildObservableUseCase() {
        if (badgeTask == null) {
            Log.e(TAG, "buildSingleUseCase: you must call setBadgeTask()");
        }
        return userRepository.updateUserBadgeTaskProgress(badgeTask);
    }

    public String getBadgeId() {
        return badgeId;
    }

    public void setBadgeId(String badgeId) {
        this.badgeId = badgeId;
    }
}
