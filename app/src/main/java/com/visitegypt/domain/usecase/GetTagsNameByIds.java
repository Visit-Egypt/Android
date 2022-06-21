package com.visitegypt.domain.usecase;

import com.visitegypt.data.source.local.dao.TagDao;
import com.visitegypt.domain.model.Tag;
import com.visitegypt.domain.usecase.base.SingleUseCase;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Single;

public class GetTagsNameByIds extends SingleUseCase<List<Tag>> {
    private TagDao tagDao;
    private List<String> tagsId;
    @Inject
    public GetTagsNameByIds(TagDao tagDao) {
        this.tagDao = tagDao;
    }

    public void setTags(List<String> tagsId) {
        this.tagsId = tagsId;
    }

    @Override
    protected Single<List<Tag>> buildSingleUseCase() {
        return tagDao.getTagsNameByIds(tagsId);
    }
}
