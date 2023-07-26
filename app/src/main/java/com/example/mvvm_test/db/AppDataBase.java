package com.example.mvvm_test.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Numbers.class}, version = 2)

public abstract class AppDataBase extends RoomDatabase {
    public abstract NumbersDao numbersDao();
}
