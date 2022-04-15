package com.visitegypt.data.repository;

import com.visitegypt.data.source.remote.RetrofitService;
import com.visitegypt.domain.model.response.BadgeResponse;
import com.visitegypt.domain.repository.BadgesRepository;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Single;

public class BadgesRepositoryImp implements BadgesRepository {
    private RetrofitService retrofitService;

    @Inject
    public BadgesRepositoryImp(RetrofitService retrofitService) {
        this.retrofitService = retrofitService;
    }

    @Override
    public Single<BadgeResponse> getAllBadges() {
        return retrofitService.getAllBadges();
    }

    @Override
    public Single<BadgeResponse> getBadgesByPlace(String placeId) {
        return retrofitService.getBadgesByPlace(placeId);
    }
}
