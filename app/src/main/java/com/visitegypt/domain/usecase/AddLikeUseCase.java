package com.visitegypt.domain.usecase;

import com.visitegypt.domain.model.Review;
import com.visitegypt.domain.repository.PostsRepository;
import com.visitegypt.domain.usecase.base.SingleUseCase;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Single;

public class AddLikeUseCase extends SingleUseCase<Void> {
    PostsRepository postsRepository;
    private String postId;

    public void setPostId(String postId) {
        this.postId = postId;
    }

    @Inject
    public AddLikeUseCase(PostsRepository postsRepository) {
        this.postsRepository = postsRepository;
    }


    @Override
    protected Single<Void> buildSingleUseCase() {
        return postsRepository.addLike(postId);
    }
}
