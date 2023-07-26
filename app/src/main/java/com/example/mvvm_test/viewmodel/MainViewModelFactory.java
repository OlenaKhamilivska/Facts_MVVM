package com.example.mvvm_test.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.mvvm_test.model.WebRepo;

public class MainViewModelFactory implements ViewModelProvider.Factory {

    private final WebRepo mNumberRepository;
    private final Application application;

    public MainViewModelFactory(Application application, WebRepo numberRepository) {
        this.mNumberRepository = numberRepository;
        this.application = application;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(MyViewModel.class)) {
            return (T) new MyViewModel(application, mNumberRepository);
        }

        throw new IllegalArgumentException("Unknown ViewModel class");
    }

}