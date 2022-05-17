package com.visitegypt.data.source.local.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.visitegypt.domain.model.Tag;
import com.visitegypt.domain.model.User;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
@Dao
public interface TagDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insert(List<Tag> tag);
    @Query("SELECT * FROM tag_table")
    Single<List<Tag>> getAllTags();
    @Query("SELECT * FROM tag_table WHERE id IN (:ids) ")
    Single<List<Tag>> getTagsNameByIds(List<String> ids);
}
