package com.example.mvvm_test.model.repositories;

import com.example.mvvm_test.model.App;
import com.example.mvvm_test.model.NumbersResponsePojo;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class WebRepo {
    public Observable<NumbersResponsePojo> getNumbers(String num) {

        return App.getWebApi().getFactEnteredNumber(num)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<NumbersResponsePojo> getRandomNumbers(String num) {

        return App.getWebApi().getFactRandomNumber()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
