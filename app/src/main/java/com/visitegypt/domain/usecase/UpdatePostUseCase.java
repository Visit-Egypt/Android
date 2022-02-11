package com.visitegypt.domain.usecase;

import com.visitegypt.domain.model.Post;
import com.visitegypt.domain.model.Review;
import com.visitegypt.domain.repository.PostsRepository;
import com.visitegypt.domain.usecase.base.SingleUseCase;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Single;

public class UpdatePostUseCase extends SingleUseCase<Post> {
    PostsRepository postsRepository;
    private Post post;

    public void setPost(Post post) {
        this.post = post;
    }

    @Inject
    public UpdatePostUseCase(PostsRepository postsRepository) {
        this.postsRepository = postsRepository;
    }

    @Override
    protected Single<Post> buildSingleUseCase() {
        return postsRepository.updatePost(post.getId(), post);

    }
}
