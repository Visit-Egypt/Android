package com.visitegypt.data.repository;

import com.visitegypt.data.source.remote.RetrofitService;
import com.visitegypt.domain.model.Post;
import com.visitegypt.domain.repository.PostsRepository;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Single;

public class PostRepositoryImp implements PostsRepository {
    private RetrofitService retrofitService;
    @Inject
    public PostRepositoryImp(RetrofitService retrofitService) {
        this.retrofitService = retrofitService;
    }

    @Override
    public Single<Post> getPost(String postId) {
        return retrofitService.getPost(postId);
    }

    @Override
    public Single<List<Post>> getPlacePosts(String postId) {
        return retrofitService.getPlacePosts(postId);
    }

    @Override
    public Single<Post> addNewPost(String token, Post post) {
        return retrofitService.addNewPost(token,post);
    }

    @Override
    public Single<Void> addLike(String token, String postId) {
        return retrofitService.addLike(token, postId);
    }

    @Override
    public Single<Post> updatePost(String token, String postId, Post post) {
        return retrofitService.updatePost(token,postId,post);
    }

    @Override
    public Single<Void> deletePost(String token, String postId) {
        return retrofitService.deletePost(token, postId);
    }

    @Override
    public Single<Void> unLike(String token, String postId) {
        return retrofitService.unLike(token, postId);
    }
}
