package com.example.taskflow;

import android.content.Context;

import com.example.taskflow.auth.AuthInterceptor;
import com.example.taskflow.utils.SharedPreferencesManager;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    public static final String BASE_URL = "http://10.0.2.2:7000/api/";
    private static  Retrofit retrofit = null;
    private static SharedPreferencesManager preferencesManager;

    public static void initialize(Context context) {
        preferencesManager = new SharedPreferencesManager(context);
    }

    public static Retrofit getRetrofitInstance (){
//        if (retrofit==null){
            String token = preferencesManager.getToken();

            if (token == null) {
                token = "eyJhbGciOiJIUzI1NiJ9.eyJtYWlsIjoiZHVtNHVrOTJAZ21haWwuY29tIiwicm9sZXMiOlsiUk9MRV9VU0VSIl0sInN1YiI6ItCU0LzQuNGC0YDQuNC5INCW0LjRgNGO0YLQuNC9IiwiaWF0IjoxNzIwODgzMTQ5LCJleHAiOjE3MjEwNjMxNDl9.XCJdIK0CY2ilAy0bBDPMCxpwJhRZPbfF_r4cWpQe4i4"; // установите здесь ваш начальный токен или пустую строку
            }

            System.out.println("your token from RetrofitClient" + token);
            // http log
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            // Создание и настройка OkHttpClient с добавлением loggingInterceptor
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(loggingInterceptor)
                    .addInterceptor(new AuthInterceptor( token

                    ))
                    .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();


//        }

        return retrofit;
    }

    public static void updateToken(String newToken) {
        if (preferencesManager != null) {
            preferencesManager.saveToken(newToken);
        }
    }
}
