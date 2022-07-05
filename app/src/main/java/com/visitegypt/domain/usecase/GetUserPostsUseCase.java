package com.visitegypt.domain.usecase;

import android.content.SharedPreferences;

import com.visitegypt.domain.model.PostPage;
import com.visitegypt.domain.model.response.PostPageResponse;
import com.visitegypt.domain.repository.PostsRepository;
import com.visitegypt.domain.usecase.base.SingleUseCase;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Single;

public class GetUserPostsUseCase extends SingleUseCase<PostPageResponse> {
    PostsRepository postsRepository;
    SharedPreferences sharedPreferences;

    private String userId;

    @Inject
    public GetUserPostsUseCase(PostsRepository postsRepository, SharedPreferences sharedPreferences) {
        this.postsRepository = postsRepository;
        this.sharedPreferences = sharedPreferences;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    protected Single<PostPageResponse> buildSingleUseCase() {
        return postsRepository.getPostsWithUserId(userId);
    }
}

