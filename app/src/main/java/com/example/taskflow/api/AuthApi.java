package com.example.taskflow.api;

import com.example.taskflow.dto.RegistrationDto;
import com.example.taskflow.dto.TokenDto;
import com.example.taskflow.dto.UserAuthDto;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthApi {

    @POST("auth")
    Call<TokenDto> getAuthToken(@Body UserAuthDto addSubtask);

    @POST("registration")
    Call<TokenDto> registration(@Body RegistrationDto registrationDto);
}
