package com.visitegypt.domain.repository;

import com.visitegypt.domain.model.Post;
import com.visitegypt.domain.model.PostPage;

import java.util.List;

import io.reactivex.rxjava3.core.Single;

public interface PostsRepository {
    Single<Post> getPost(String postId);
    Single<List<Post>> getPlacePosts(String postId);
    Single<PostPage> getPostsByUser(String userId);
    Single<Post> addNewPost(Post post);
    Single<Void> addLike(String postId);
    Single<Post> updatePost(String postId, Post post);
    Single<Void> deletePost(String postId);
    Single<Void> unLike(String postId);


}
