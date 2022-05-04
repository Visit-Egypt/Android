package com.visitegypt.domain.repository;

import com.visitegypt.domain.model.Tag;

import java.util.List;

import io.reactivex.rxjava3.core.Single;

public interface TagRepository {
    public Single<List<Tag>> getTags();
}
