package com.visitegypt.domain.usecase;

import com.visitegypt.domain.model.BadgeTask;
import com.visitegypt.domain.repository.UserRepository;
import com.visitegypt.domain.usecase.base.SingleUseCase;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.rxjava3.core.Single;

public class UpdateUserBadgeTaskProgUseCase extends SingleUseCase<List<BadgeTask>> {
     UserRepository userRepository;
    //badgeTask Constructor BadgeTask(String badgeId, String taskTitle, int progress)
    private BadgeTask badgeTask;

    @Inject
    public UpdateUserBadgeTaskProgUseCase(@Named("Normal")  UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void setBadgeTask(BadgeTask badgeTask) {
        this.badgeTask = badgeTask;
    }

    @Override
    protected Single<List<BadgeTask>> buildSingleUseCase() {
        String badgeId  = "623637b3da63f3494ba0ab2d";
        badgeTask = new BadgeTask(badgeId,"string8",10);
        return userRepository.updateUserBadgeTaskProgress(badgeTask);
    }
}
