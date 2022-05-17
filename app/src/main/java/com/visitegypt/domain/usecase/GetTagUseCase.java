package com.visitegypt.domain.usecase;

import com.visitegypt.data.source.local.dao.TagDao;
import com.visitegypt.domain.model.Tag;
import com.visitegypt.domain.repository.TagRepository;
import com.visitegypt.domain.usecase.base.SingleUseCase;
import com.visitegypt.domain.usecase.base.SingleUseCaseCache;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class GetTagUseCase{
    TagRepository tagRepository;
    private TagDao tagDao;

    @Inject
    public GetTagUseCase(TagRepository tagRepository, TagDao tagDao) {
        this.tagRepository = tagRepository;
        this.tagDao = tagDao;
    }
    public Observable<List<Tag>> buildSingleUseCase ()
    {
        return tagRepository.getTags();
    }




//    @Override
//    protected Single<List<Tag>> buildSingleUseCase() {
//        Single<List<Tag>> dbData = tagRepository.getTags()
//                .subscribeOn(Schedulers.io());
//        Single<List<Tag>> networkData = tagDao.getAllTags()
//                .subscribeOn(Schedulers.io());
//
//        return Single.concat(dbData, networkData)
//                .firstElement()
//                .toSingle();
//    }
}
