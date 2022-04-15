package com.visitegypt.domain.usecase;

import com.visitegypt.domain.model.response.BadgeResponse;
import com.visitegypt.domain.repository.BadgesRepository;
import com.visitegypt.domain.usecase.base.SingleUseCase;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Single;

public class GetBadgesOfPlaceUseCase extends SingleUseCase<BadgeResponse> {

    private BadgesRepository badgesRepository;
    private String placeId;

    @Inject
    public GetBadgesOfPlaceUseCase(BadgesRepository badgesRepository) {
        this.badgesRepository = badgesRepository;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    @Override
    protected Single<BadgeResponse> buildSingleUseCase() {
        return badgesRepository.getBadgesByPlace(placeId);
    }
}
