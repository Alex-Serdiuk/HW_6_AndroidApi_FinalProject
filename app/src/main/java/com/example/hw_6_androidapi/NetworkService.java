package com.example.hw_6_androidapi;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkService {
    private static NetworkService networkService;

    private Retrofit retrofit;

    private static final String BASE_URL = "http://192.168.0.106:8080/";

    private NetworkService(){
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static NetworkService getInstance(){
        if(networkService == null){
            networkService = new NetworkService();
        }
        return networkService;
    }

    public NoteApi getNoteApi(){
        return retrofit.create(NoteApi.class);
    }
}
