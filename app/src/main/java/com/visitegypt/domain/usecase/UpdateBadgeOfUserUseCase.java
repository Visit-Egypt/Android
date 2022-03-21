package com.visitegypt.domain.usecase;

import com.visitegypt.domain.model.Badge;
import com.visitegypt.domain.repository.UserRepository;
import com.visitegypt.domain.usecase.base.SingleUseCase;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.rxjava3.core.Single;

public class UpdateBadgeOfUserUseCase extends SingleUseCase<List<Badge>> {
    /*
     * Please note that,Khaled
     * this use case returns list of badges without badge task
     */
    UserRepository userRepository;
    private String badgeId;
    //Badge Constructor Badge(int progress, boolean owned, boolean pinned)
    private Badge badge;

    @Inject
    public UpdateBadgeOfUserUseCase(@Named("Normal") UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void setBadgeId(String badgeId) {
        this.badgeId = badgeId;
    }

    public void setBadge(Badge badge) {
        this.badge = badge;
    }

    @Override
    protected Single<List<Badge>> buildSingleUseCase() {
        badgeId = "623637b3da63f3494ba0ab2d";
        //badge = new Badge(5, true, true);

        return userRepository.updateUserBadge(badgeId, badge);
    }
}
