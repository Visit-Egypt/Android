package com.visitegypt.domain.repository;

import com.visitegypt.domain.model.response.BadgeResponse;

import io.reactivex.rxjava3.core.Single;

public interface BadgesRepository {
    public Single<BadgeResponse> getAllBadges();
}
