package com.visitegypt.domain.usecase;

import android.content.SharedPreferences;

import com.visitegypt.domain.model.Post;
import com.visitegypt.domain.repository.PostsRepository;
import com.visitegypt.domain.usecase.base.SingleUseCase;
import com.visitegypt.utils.Constants;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Single;

public class GetPostsByUser extends SingleUseCase<List<Post>> {
     PostsRepository postsRepository;
    SharedPreferences sharedPreferences;
    @Inject
    public GetPostsByUser(PostsRepository postsRepository) {
        this.postsRepository = postsRepository;
    }
    @Override
    protected Single<List<Post>> buildSingleUseCase() {

        return postsRepository.getPostsByUser(sharedPreferences.getString(Constants.SHARED_PREF_USER_ID,null));
    }
}
