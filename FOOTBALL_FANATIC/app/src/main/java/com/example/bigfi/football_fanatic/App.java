package com.example.bigfi.football_fanatic;

import android.app.Application;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by bigfi on 25.11.2017.
 */

public class App extends Application {

    private static FootballAPI sFootballAPI;
    private Retrofit mRetrofit;

    @Override
    public void onCreate(){
        super.onCreate();

        mRetrofit = new Retrofit.Builder()
                .baseUrl("http://api.football-data.org")
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        sFootballAPI = mRetrofit.create(FootballAPI.class);


    }

    public static FootballAPI getAPI(){
        return sFootballAPI;
    }
}
