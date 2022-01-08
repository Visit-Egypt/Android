package com.visitegypt.domain.usecase;

import static com.visitegypt.utils.Constants.SHARED_PREF_USER_ACCESS_TOKEN;

import android.content.SharedPreferences;

import com.visitegypt.domain.repository.PostsRepository;
import com.visitegypt.domain.usecase.base.SingleUseCase;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Single;

public class UnLikePostUseCase extends SingleUseCase<Void> {
    PostsRepository postsRepository;
    private String postId;
    @Inject
    public UnLikePostUseCase(PostsRepository postsRepository) {
        this.postsRepository = postsRepository;
    }

    @Override
    protected Single<Void> buildSingleUseCase() {
        return postsRepository.unLike(postId);
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }
}
