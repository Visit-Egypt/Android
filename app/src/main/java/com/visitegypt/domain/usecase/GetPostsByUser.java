package com.visitegypt.domain.usecase;

import android.content.SharedPreferences;

import com.visitegypt.domain.model.PostPage;
import com.visitegypt.domain.model.Review;
import com.visitegypt.domain.repository.PostsRepository;
import com.visitegypt.domain.usecase.base.SingleUseCase;
import com.visitegypt.utils.Constants;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Single;

public class GetPostsByUser extends SingleUseCase<PostPage> {
    PostsRepository postsRepository;
    SharedPreferences sharedPreferences;

    @Inject
    public GetPostsByUser(PostsRepository postsRepository, SharedPreferences sharedPreferences) {
        this.postsRepository = postsRepository;
        this.sharedPreferences = sharedPreferences;
    }


    @Override
    protected Single<PostPage> buildSingleUseCase() {
        return postsRepository.getPostsByUser(sharedPreferences.getString(Constants.SHARED_PREF_USER_ID, null));

    }
}
