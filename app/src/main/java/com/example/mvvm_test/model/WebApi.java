package com.example.mvvm_test.model;


import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;

public interface WebApi {
    @Headers("Content-Type: application/json")
    @GET("/{number}")
    Observable<NumbersResponsePojo> getFactEnteredNumber(@Path("number") String fact);

    @Headers("Content-Type: application/json")
    @GET("/random/math")
    Observable<NumbersResponsePojo> getFactRandomNumber();
}