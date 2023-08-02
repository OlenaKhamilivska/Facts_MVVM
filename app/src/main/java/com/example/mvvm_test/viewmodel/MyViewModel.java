package com.example.mvvm_test.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.mvvm_test.db.Numbers;
import com.example.mvvm_test.model.NumbersResponsePojo;
import com.example.mvvm_test.model.repositories.DataBaseRepo;
import com.example.mvvm_test.model.repositories.WebRepo;
import com.example.mvvm_test.model.exceptions.NoConnectivityException;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class MyViewModel extends AndroidViewModel {
    private LiveData<List<Numbers>> numbers = new MutableLiveData<>();
    private final MutableLiveData<NumbersResponsePojo> pojoLiveData = new MutableLiveData<>();
    private MutableLiveData <String> errorMessageLiveData = new MutableLiveData<>();
    private final MutableLiveData<Void> showLoadingLiveData = new MutableLiveData<>();
    private final MutableLiveData<Void> hideLoadingLiveData = new MutableLiveData<>();
    private final WebRepo numberRepository;
    private final DataBaseRepo dataBaseRepo;

    public MyViewModel(Application application, WebRepo numberRepository) {
        super(application);
        dataBaseRepo = new DataBaseRepo(application);
        numbers = dataBaseRepo.getAllFacts();
        this.numberRepository = numberRepository;
    }

    private void insert(Numbers numbers) {
        dataBaseRepo.insert(numbers);
    }

    public void setPojoLiveData(NumbersResponsePojo pojoLiveData) {
        setIsLoading(true);
        this.pojoLiveData.setValue(pojoLiveData);
    }

    private void setIsLoading(boolean loading) {
        if (loading) {
            showLoadingLiveData.postValue(null);
        } else {
            hideLoadingLiveData.postValue(null);
        }
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

                        setIsLoading(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        setIsLoading(false);
                        errorMessageLiveData.postValue("Internet is missing");
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

                        setIsLoading(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        setIsLoading(false);
                        errorMessageLiveData.postValue("Internet is missing");
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    public LiveData<NumbersResponsePojo> getPojoLiveData() {
        return pojoLiveData;
    }


    public MutableLiveData<String> getErrorMessageLiveData() {
        return errorMessageLiveData;
    }

    public void setErrorMessageLiveData(String errorMessageLiveData) {
        this.errorMessageLiveData.setValue(errorMessageLiveData);
    }

    public LiveData<List<Numbers>> getDataFromDB() {
        return numbers;
    }

    public MutableLiveData<Void> getShowLoadingLiveData() {
        return showLoadingLiveData;
    }

    public MutableLiveData<Void> getHideLoadingLiveData() {
        return hideLoadingLiveData;
    }
}
