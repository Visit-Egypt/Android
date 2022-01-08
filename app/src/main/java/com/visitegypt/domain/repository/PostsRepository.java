package com.visitegypt.domain.repository;

import com.visitegypt.domain.model.Post;

import java.util.List;

import io.reactivex.rxjava3.core.Single;

public interface PostsRepository {
    Single<Post> getPost(String postId);
    Single<List<Post>> getPlacePosts(String postId);
    Single<List<Post>> getPostsByUser(String userId);
    Single<Post> addNewPost(String token, Post post);

    Single<Void> addLike(String token, String postId);

    Single<Post> updatePost(String token, String postId, Post post);

    Single<Void> deletePost(String token, String postId);

    Single<Void> unLike(String token, String postId);


}
