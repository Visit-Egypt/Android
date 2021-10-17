package com.visitegypt.data.source.local.dao;

import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.visitegypt.domain.model.User;

import io.reactivex.rxjava3.core.Single;
import retrofit2.http.DELETE;

public interface UserDao {
    // TODO https://developer.android.com/training/data-storage/room
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(User user);

    @Query("SELECT * FROM User where id = (:id)")
    Single<User> getUserById(String id);

    @DELETE
    void deleteUserById(String id);
}
