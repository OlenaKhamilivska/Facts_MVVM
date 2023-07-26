package com.example.mvvm_test.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.mvvm_test.db.Numbers;
import com.example.mvvm_test.model.NumbersResponsePojo;
import com.example.mvvm_test.model.DataBaseRepo;
import com.example.mvvm_test.model.WebRepo;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class MyViewModel extends AndroidViewModel {
    private final MutableLiveData<NumbersResponsePojo> pojoLiveData = new MutableLiveData<>();
    private final WebRepo numberRepository;
    private final DataBaseRepo dataBaseRepo;
    private LiveData<List<Numbers>> numbers = new MutableLiveData<>();

    public LiveData<List<Numbers>> getDataFromDB() {
        return numbers;
    }

    public MyViewModel(Application application, WebRepo numberRepository) {
        super(application);
        dataBaseRepo = new DataBaseRepo(application);
        numbers = dataBaseRepo.getAllFacts();
        this.numberRepository = numberRepository;
    }

    void insert(Numbers numbers) {
        dataBaseRepo.insert(numbers);
    }

    public LiveData<NumbersResponsePojo> getPojoLiveData() {
        return pojoLiveData;
    }

    public void setPojoLiveData(NumbersResponsePojo pojoLiveData) {
        this.pojoLiveData.setValue(pojoLiveData);
        ;
    }

    public void getNumbers(String num) {
        numberRepository.getNumbers(num)
                .subscribe(new Observer<NumbersResponsePojo>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(NumbersResponsePojo numbersResponsePojo) {
                        Numbers numbers = new Numbers();
                        numbers.facts = numbersResponsePojo.getText();
                        numbers.number = numbersResponsePojo.getNumber();
                        numbers.time = System.currentTimeMillis();
                        setPojoLiveData(numbersResponsePojo);
                        insert(numbers);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("numTAg", "onError: " + e.getMessage());

                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    public void getRandomNumbers(String num) {
        numberRepository.getRandomNumbers(num)
                .subscribe(new Observer<NumbersResponsePojo>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(NumbersResponsePojo numbersResponsePojo) {
                        Numbers numbers = new Numbers();
                        numbers.facts = numbersResponsePojo.getText();
                        numbers.number = numbersResponsePojo.getNumber();
                        numbers.time = System.currentTimeMillis();
                        setPojoLiveData(numbersResponsePojo);
                        insert(numbers);
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }
}
