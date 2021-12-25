package com.visitegypt.domain.usecase;

import android.content.SharedPreferences;

import com.visitegypt.domain.model.Post;
import com.visitegypt.domain.model.User;
import com.visitegypt.domain.repository.PostsRepository;
import com.visitegypt.domain.usecase.base.SingleUseCase;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Single;

public class GetPostUseCase extends SingleUseCase<Post> {
    private PostsRepository postsRepository;
    private String postId;

    @Inject
    public GetPostUseCase(PostsRepository postsRepository) {
        this.postsRepository = postsRepository;
    }

    @Override
    protected Single<Post> buildSingleUseCase() {
        return postsRepository.getPost(postId);
    }

    public void getPostId(String postId) {
        this.postId = postId;
    }
}
