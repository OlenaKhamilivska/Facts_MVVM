package com.example.mvvm_test.model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.mvvm_test.db.AppDataBase;
import com.example.mvvm_test.db.Numbers;
import com.example.mvvm_test.db.NumbersDao;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class App extends Application {
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);
    private static Retrofit mRetrofit;
    private static AppDataBase mAppDataBase;


    private static final String mBASE_URL = "http://numbersapi.com";

    @Override
    public void onCreate() {
        super.onCreate();
        mAppDataBase = Room.databaseBuilder(this,
                        AppDataBase.class, "word_database")
                .addCallback(sRoomDatabaseCallback)
                .build();

        mRetrofit = new Retrofit.Builder()
                .baseUrl(mBASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    public static WebApi getWebApi() {
        return mRetrofit.create(WebApi.class);
    }

    public static AppDataBase getDataBase() {
        return mAppDataBase;
    }

    public static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            databaseWriteExecutor.execute(() -> {
                // Populate the database in the background.
                // If you want to start with more words, just add them.
                NumbersDao dao = mAppDataBase.numbersDao();
                Numbers numbers = new Numbers();//!!!!!
                dao.insert(numbers);
            });
        }
    };
}