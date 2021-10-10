package com.visitegypt.data.source.local;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.visitegypt.data.source.local.dao.UserDao;
import com.visitegypt.domain.model.User;

@Database(entities = {User.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    // TODO https://developer.android.com/training/data-storage/room
    public abstract UserDao userDao();
}
