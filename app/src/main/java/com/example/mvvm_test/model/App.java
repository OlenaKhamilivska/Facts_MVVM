package com.example.mvvm_test.model;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.mvvm_test.db.AppDataBase;
import com.example.mvvm_test.db.Numbers;
import com.example.mvvm_test.db.NumbersDao;
import com.example.mvvm_test.model.exceptions.ConnectivityInterceptorImpl;
import com.example.mvvm_test.view.MainActivity;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class App extends Application {
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);
    private static Retrofit mRetrofit;
    private static AppDataBase mAppDataBase;

    private static OkHttpClient.Builder oktHttpClient;
    private HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
    private static final String mBASE_URL = "http://numbersapi.com";

    @SuppressLint("SuspiciousIndentation")
    @Override
    public void onCreate() {
        super.onCreate();
        mAppDataBase = Room.databaseBuilder(this,
                        AppDataBase.class, "word_database")
                .addCallback(sRoomDatabaseCallback)
                .build();

        oktHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new ConnectivityInterceptorImpl(this));
                oktHttpClient.addInterceptor(logging);

        mRetrofit = new Retrofit.Builder()
                .baseUrl(mBASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    public static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            databaseWriteExecutor.execute(() -> {
                NumbersDao dao = mAppDataBase.numbersDao();
                Numbers numbers = new Numbers();
                dao.insert(numbers);
            });
        }
    };

    public static WebApi getWebApi() {
        return mRetrofit.create(WebApi.class);
    }

    public static AppDataBase getDataBase() {
        return mAppDataBase;
    }
}
