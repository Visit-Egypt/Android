package com.visitegypt.domain.usecase;

import static com.visitegypt.utils.Constants.SHARED_PREF_USER_ACCESS_TOKEN;

import android.content.SharedPreferences;

import com.visitegypt.domain.repository.PostsRepository;
import com.visitegypt.domain.usecase.base.SingleUseCase;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Single;

public class UnLikePostUseCase extends SingleUseCase<Void> {
    SharedPreferences sharedPreferences;
    PostsRepository postsRepository;
    private String postId;
    @Inject
    public UnLikePostUseCase(SharedPreferences sharedPreferences, PostsRepository postsRepository) {
        this.sharedPreferences = sharedPreferences;
        this.postsRepository = postsRepository;
    }

    @Override
    protected Single<Void> buildSingleUseCase() {
        String token = "Bearer " + sharedPreferences.getString(SHARED_PREF_USER_ACCESS_TOKEN, "");
        return postsRepository.unLike(token,postId);
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }
}
