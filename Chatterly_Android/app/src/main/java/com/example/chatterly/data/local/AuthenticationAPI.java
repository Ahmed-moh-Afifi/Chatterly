package com.example.chatterly.data.local;

import com.example.chatterly.model.authentication.LoginModel;
import com.example.chatterly.model.authentication.RegisterModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthenticationAPI {
    @POST("Login")
    Call<LoginModel> loginRequest(@Body LoginModel loginData);

    @POST("Register")
    Call<RegisterModel> registerRequest(@Body RegisterModel registerModel);
}
