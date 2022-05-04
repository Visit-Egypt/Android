package com.visitegypt.domain.usecase;

import com.visitegypt.domain.model.Tag;
import com.visitegypt.domain.repository.TagRepository;
import com.visitegypt.domain.usecase.base.SingleUseCaseCache;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Single;

public class GetTagUseCase extends SingleUseCaseCache<List<Tag>> {
    TagRepository tagRepository;
    @Inject
    public GetTagUseCase(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }


    @Override
    protected Single<List<Tag>> buildSingleUseCase() {
        return tagRepository.getTags();
    }
}
