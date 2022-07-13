package com.visitegypt.data.repository;

import com.visitegypt.data.source.remote.RetrofitService;
import com.visitegypt.domain.model.ARResponsePage;
import com.visitegypt.domain.repository.ARRepository;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.rxjava3.core.Single;

public class ARRepositoryImp implements ARRepository {

    private RetrofitService retrofitService;

    @Inject
    public ARRepositoryImp(@Named("Normal") RetrofitService retrofitService) {
        this.retrofitService = retrofitService;
    }

    @Override
    public Single<ARResponsePage> getARResponse(String userId) {
        return retrofitService.getARResponse(userId);
    }

}
