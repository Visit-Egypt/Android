package com.visitegypt.domain.usecase;

import static com.visitegypt.utils.Constants.SHARED_PREF_USER_ACCESS_TOKEN;

import android.content.SharedPreferences;

import com.visitegypt.domain.model.Post;
import com.visitegypt.domain.repository.PostsRepository;
import com.visitegypt.domain.usecase.base.SingleUseCase;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Single;

public class AddNewPostUseCase extends SingleUseCase<Post> {
    public void setPost(Post post) {
        this.post = post;
    }

    private Post post;
    SharedPreferences sharedPreferences;
    PostsRepository postsRepository;
    @Inject
    public AddNewPostUseCase(SharedPreferences sharedPreferences, PostsRepository postsRepository) {
        this.sharedPreferences = sharedPreferences;
        this.postsRepository = postsRepository;
    }

    @Override
    protected Single<Post> buildSingleUseCase() {
        String token = "Bearer " + sharedPreferences.getString(SHARED_PREF_USER_ACCESS_TOKEN, "");
        return postsRepository.addNewPost(token,post);
    }
}
