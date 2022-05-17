package com.visitegypt.data.repository;

import com.visitegypt.data.source.local.dao.TagDao;
import com.visitegypt.data.source.remote.RetrofitService;
import com.visitegypt.domain.model.Tag;
import com.visitegypt.domain.model.User;
import com.visitegypt.domain.repository.TagRepository;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class TagRepositoryImp implements TagRepository {
    private RetrofitService retrofitService;
    private TagDao tagDao;

    @Inject
    public TagRepositoryImp(RetrofitService retrofitService, TagDao tagDao) {
        this.retrofitService = retrofitService;
        this.tagDao = tagDao;
    }

    @Override
    public @NonNull Observable<List<Tag>> getTags() {
        Single<List<Tag>> remoteTageSource = retrofitService
                .getTags()
                .subscribeOn(Schedulers.io());
        return  tagDao.getAllTags()
                .subscribeOn(Schedulers.computation())
                .flatMapObservable(tagsFromLocalSource ->  {
                   return remoteTageSource
                            .observeOn(Schedulers.computation())
                            .toObservable()
                            .filter(tagsFromRemoteSource -> tagsFromRemoteSource != tagsFromLocalSource )
                            .flatMapSingle(tagsFromRemoteSource -> tagDao
                                    .insert(tagsFromRemoteSource)
                                    .subscribeOn(Schedulers.computation())
                                    .andThen(Single.just(tagsFromRemoteSource)))
                            .startWith(Observable.just(tagsFromLocalSource));
                });

    }

    @Override
    public Single<List<User>> getAllUserTag(List<String> ids) {
        HashMap<String,List<String>> map = new HashMap<>();
        map.put("tags_ids",ids);
        return retrofitService.getAllUserTags(map);
    }


}
