package com.visitegypt.domain.usecase;

import com.visitegypt.domain.model.Review;
import com.visitegypt.domain.repository.PostsRepository;
import com.visitegypt.domain.usecase.base.SingleUseCase;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Single;

public class UnLikePostUseCase extends SingleUseCase<Void> {
    PostsRepository postsRepository;
    private String postId;
    @Inject
    public UnLikePostUseCase(PostsRepository postsRepository) {
        this.postsRepository = postsRepository;
    }



    public void setPostId(String postId) {
        this.postId = postId;
    }

    @Override
    protected Single<Void> buildSingleUseCase() {
        return postsRepository.unLike(postId);

    }
}
