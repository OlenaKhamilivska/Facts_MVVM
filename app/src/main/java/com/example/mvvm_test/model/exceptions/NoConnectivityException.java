package com.example.mvvm_test.model.exceptions;

import androidx.annotation.NonNull;

import java.io.IOException;

public class NoConnectivityException extends IOException {

    @NonNull
    @Override
    public String getMessage() {

        return "No Internet Connection";
    }
}
