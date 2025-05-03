package com.example.chatterly.data.local;

import com.example.chatterly.model.authentication.ForgetPasswordModel;
import com.example.chatterly.model.authentication.LoginModel;
import com.example.chatterly.model.authentication.RegisterModel;
import com.example.chatterly.model.authentication.ResetPasswordModel;
import com.example.chatterly.model.authentication.TokensModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface AuthenticationAPI {
    @POST("Auth/Login")
    Call<TokensModel> loginRequest(@Body LoginModel loginData);

    @POST("Auth/Register")
    Call registerRequest(@Body RegisterModel registerModel);

    @PUT("Auth/ResetPassword")
    Call resetPasswordRequest(@Body ResetPasswordModel resetPasswordModel);

    @POST("Auth/ForgetPasswordRequest")
    Call forgetPasswordRequest(@Body ForgetPasswordModel forgetPasswordModel);
}
