package com.visitegypt.domain.usecase;

import static com.visitegypt.utils.Constants.SHARED_PREF_USER_ACCESS_TOKEN;

import android.content.SharedPreferences;

import com.visitegypt.domain.model.Post;
import com.visitegypt.domain.repository.PostsRepository;
import com.visitegypt.domain.usecase.base.SingleUseCase;

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
