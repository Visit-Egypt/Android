package com.visitegypt.domain.usecase;

import com.visitegypt.data.source.remote.RetrofitService;
import com.visitegypt.domain.model.Post;
import com.visitegypt.domain.repository.PostsRepository;
import com.visitegypt.domain.usecase.base.SingleUseCase;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Single;

public class GetPlacePostsUseCase extends SingleUseCase<List<Post>> {
    private PostsRepository postsRepository;
    private String postId;

    @Inject
    public GetPlacePostsUseCase(PostsRepository postsRepository) {
        this.postsRepository = postsRepository;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    @Override
    protected Single<List<Post>> buildSingleUseCase() {
        return postsRepository.getPlacePosts(postId);
    }
}
