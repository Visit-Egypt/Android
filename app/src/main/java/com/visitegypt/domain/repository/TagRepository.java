package com.visitegypt.domain.repository;

import com.visitegypt.domain.model.Tag;
import com.visitegypt.domain.model.User;

import java.util.List;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

public interface TagRepository {
    public @NonNull Observable<List<Tag>> getTags();
    public Single<List<User>> getAllUserTag(List<String> ids);
}
