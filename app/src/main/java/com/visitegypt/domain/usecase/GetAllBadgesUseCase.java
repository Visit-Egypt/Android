package com.visitegypt.domain.usecase;

import com.visitegypt.domain.model.response.BadgeResponse;
import com.visitegypt.domain.repository.BadgesRepository;
import com.visitegypt.domain.usecase.base.SingleUseCase;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Single;

public class GetAllBadgesUseCase extends SingleUseCase<BadgeResponse> {
    BadgesRepository badgesRepository;
    @Inject
    public GetAllBadgesUseCase(BadgesRepository badgesRepository) {
        this.badgesRepository = badgesRepository;
    }

    @Override
    protected Single<BadgeResponse> buildSingleUseCase() {
        return badgesRepository.getAllBadges();
    }
}
