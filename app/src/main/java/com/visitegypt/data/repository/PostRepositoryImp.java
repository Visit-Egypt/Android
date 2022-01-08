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
    public Single<List<Post>> getPostsByUser(String userId) {
        return retrofitService.getPostsByUser(userId);
    }

    public Single<Post> addNewPost(Post post) {
        return retrofitService.addNewPost(post);

    }

    @Override
    public Single<Void> addLike(String postId) {
        return retrofitService.addLike(postId);
    }

    @Override
    public Single<Post> updatePost(String postId, Post post) {
        return retrofitService.updatePost(postId,post);
    }

    @Override
    public Single<Void> deletePost(String postId) {
        return retrofitService.deletePost(postId);
    }

    @Override
    public Single<Void> unLike(String postId) {
        return retrofitService.unLike(postId);
    }
}
