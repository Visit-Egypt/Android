package com.visitegypt.domain.usecase;

import static com.visitegypt.utils.Constants.SHARED_PREF_USER_ACCESS_TOKEN;

import android.content.SharedPreferences;

import com.visitegypt.domain.repository.PostsRepository;
import com.visitegypt.domain.usecase.base.SingleUseCase;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Single;

public class DeletePostUseCase extends SingleUseCase<Void> {
    PostsRepository postsRepository;
    private String postId;
    public void setPostId(String postId) {
        this.postId = postId;
    }


    @Inject
    public DeletePostUseCase(PostsRepository postsRepository) {
        this.postsRepository = postsRepository;
    }

    @Override
    protected Single<Void> buildSingleUseCase() {
        return postsRepository.deletePost(postId);
    }
}
