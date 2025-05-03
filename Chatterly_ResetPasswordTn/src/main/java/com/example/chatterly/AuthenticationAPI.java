package com.example.chatterly;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.PUT;

public interface AuthenticationAPI {
    @PUT("Auth/ResetPasswordTn")
    Call<ResetPasswordTnModel> resetPasswordTnRequest(@Body ResetPasswordTnModel resetPasswordTnModel);
}
