package com.visitegypt.domain.usecase;

import static com.visitegypt.utils.Constants.SHARED_PREF_USER_ACCESS_TOKEN;

import android.content.SharedPreferences;

import com.visitegypt.domain.model.Post;
import com.visitegypt.domain.repository.PostsRepository;
import com.visitegypt.domain.usecase.base.SingleUseCase;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Single;

public class UpdatePostUseCase extends SingleUseCase<Post> {
    SharedPreferences sharedPreferences;
    PostsRepository postsRepository;
    private Post post;

    public void setPost(Post post) {
        this.post = post;
    }

    @Inject
    public UpdatePostUseCase(SharedPreferences sharedPreferences, PostsRepository postsRepository) {
        this.sharedPreferences = sharedPreferences;
        this.postsRepository = postsRepository;
    }
    @Override
    protected Single<Post> buildSingleUseCase() {
        String token = "Bearer " + sharedPreferences.getString(SHARED_PREF_USER_ACCESS_TOKEN, "");
        return postsRepository.updatePost(token, post.getId(), post);
    }
}
