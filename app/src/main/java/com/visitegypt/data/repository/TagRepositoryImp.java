package com.visitegypt.data.repository;

import com.visitegypt.data.source.remote.RetrofitService;
import com.visitegypt.domain.model.Tag;
import com.visitegypt.domain.repository.TagRepository;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Single;

public class TagRepositoryImp implements TagRepository {
    private RetrofitService retrofitService;
    @Inject
    public TagRepositoryImp(RetrofitService retrofitService) {
        this.retrofitService = retrofitService;
    }

    @Override
    public Single<List<Tag>> getTags() {
        return retrofitService.getTags();
    }
}
