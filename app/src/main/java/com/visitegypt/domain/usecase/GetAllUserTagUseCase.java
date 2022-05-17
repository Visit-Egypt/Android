package com.visitegypt.domain.usecase;

import com.visitegypt.domain.model.User;
import com.visitegypt.domain.repository.TagRepository;
import com.visitegypt.domain.usecase.base.SingleUseCase;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Single;

public class GetAllUserTagUseCase extends SingleUseCase<List<User>> {
    private TagRepository tagRepository;
    private List<String> ids;
    @Inject
    public GetAllUserTagUseCase(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    public void setIds(List<String> ids) {
        this.ids = ids;
    }

    @Override
    protected Single<List<User>> buildSingleUseCase() {
        return tagRepository.getAllUserTag(ids);
    }
}
