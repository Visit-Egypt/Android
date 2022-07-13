package com.visitegypt.domain.repository;

import com.visitegypt.domain.model.ARResponsePage;

import io.reactivex.rxjava3.core.Single;

public interface ARRepository {
    Single<ARResponsePage> getARResponse(String userId);

}
