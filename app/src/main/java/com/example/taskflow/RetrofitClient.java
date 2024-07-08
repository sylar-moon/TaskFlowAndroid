package com.example.taskflow;

import com.example.taskflow.auth.AuthInterceptor;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static Retrofit retrofit;
    public static final String BASE_URL = "http://10.0.2.2:7000/api/";

    public static Retrofit getRetrofitInstance (){
        if (retrofit==null){

            // http log
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            // Создание и настройка OkHttpClient с добавлением loggingInterceptor
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(loggingInterceptor)
                    .addInterceptor(new AuthInterceptor("eyJhbGciOiJIUzI1NiJ9.eyJtYWlsIjoiZHVtNHVrOTJAZ21haWwuY29tIiwicm9sZXMiOlsiUk9MRV9VU0VSIl0sInN1YiI6ItCU0LzQuNGC0YDQuNC5INCW0LjRgNGO0YLQuNC9IiwiaWF0IjoxNzIwNDY0MzA4LCJleHAiOjE3MjA2NDQzMDh9.1wU3zdjMydZHiJfzXXcPHQS5purjh_cS6jm2yDkILgE"))
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();


        }

        return retrofit;
    }
}
