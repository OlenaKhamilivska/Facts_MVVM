package com.example.mvvm_test.model;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.mvvm_test.db.AppDataBase;
import com.example.mvvm_test.db.Numbers;
import com.example.mvvm_test.db.NumbersDao;

import java.util.List;

public class DataBaseRepo {
    private NumbersDao mNumbersDao;
    private LiveData<List<Numbers>> mAllNumbers;

    public DataBaseRepo(Application application) {
        AppDataBase db = App.getDataBase();
        Log.d("muTAG", "DataBaseRepo: " + db.numbersDao());
        mNumbersDao = db.numbersDao();
        Log.d("muTAG", "numbersDao.getAllLiveFa: " + mNumbersDao.getAllFactsLiveData());
        mAllNumbers = mNumbersDao.getAllFactsLiveData();
    }

    public LiveData<List<Numbers>> getAllFacts() {
        return mAllNumbers;
    }

    public void insert(Numbers numbers) {
        App.databaseWriteExecutor.execute(() -> {
            mNumbersDao.insert(numbers);
        });
    }
}
