package com.example.mvvm_test.db;

import androidx.lifecycle.LiveData;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@androidx.room.Dao
public interface NumbersDao {
    @Query("SELECT * FROM numbers")
    List<Numbers> getAllFacts();

    @Query("SELECT * FROM numbers order by time DESC")
    List<Numbers> getAllFactsSortByTime();

    @Query("SELECT * FROM numbers order by time DESC")
    LiveData<List<Numbers>> getAllFactsLiveData();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Numbers numbers);

    @Update
    void update(Numbers numbers);

    @Delete
    void delete(Numbers numbers);
}
