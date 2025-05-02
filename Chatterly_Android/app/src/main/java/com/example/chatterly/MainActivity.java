package com.example.chatterly;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.ComponentActivity;

import com.example.chatterly.data.local.AuthenticationAPI;
import com.example.chatterly.data.local.TokenManager;
import com.example.chatterly.model.authentication.LoginModel;
import com.example.chatterly.model.authentication.ResetPasswordModel;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@AndroidEntryPoint
public class MainActivity extends ComponentActivity {
    @Inject
    TokenManager tokenManager;

    public void testLogin(LoginModel loginModel, AuthenticationAPI authenticationAPI) {
        authenticationAPI.loginRequest(loginModel).enqueue(new Callback<LoginModel>() {
            @Override
            public void onResponse(Call<LoginModel> call, Response<LoginModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String responseString = "Response Code : " + response.code() + "\nResponse Message: " + response.message() + "\nResponse Body: " + response.body();
                    Log.d("LOGIN", responseString + "\n" + loginModel.toString());
                } else {
                    Log.e("LOGIN", "Error: " + response.code() + "\n" + loginModel.toString());
                }
            }

            @Override
            public void onFailure(Call<LoginModel> call, Throwable t) {
                Log.e("LOGIN", "Failed: " + t.getMessage() + "\n" +  loginModel.toString());
            }
        });
    }

    public void testResetPassword(ResetPasswordModel resetPasswordModel, AuthenticationAPI authenticationAPI){
        authenticationAPI.resetPasswordRequest(resetPasswordModel).enqueue(new Callback<ResetPasswordModel>() {
            @Override
            public void onResponse(Call<ResetPasswordModel> call, Response<ResetPasswordModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String responseString = "Response Code : " + response.code() + "\nResponse Message: " + response.message() + "\nResponse Body: " + response.body();
                    Log.d("RESET", responseString + "\n" +  resetPasswordModel.toString());
                } else {
                    Log.e("RESET", "Error: " + response.code() + "\n" +  resetPasswordModel.toString());
                }
            }

            @Override
            public void onFailure(Call<ResetPasswordModel> call, Throwable t) {
                Log.e("RESET", "Failed: " + t.getMessage() + "\n" +  resetPasswordModel.toString());
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.1.3:8080/Auth/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        AuthenticationAPI retrofitAPI = retrofit.create(AuthenticationAPI.class);
        LoginModel loginModel = new LoginModel("ahmedafifi", "Test@123");
        testLogin(loginModel, retrofitAPI);
        loginModel.setPassword("Test@132");
        testLogin(loginModel, retrofitAPI);
        ResetPasswordModel resetPasswordModel = new ResetPasswordModel(loginModel.getUsername(),"Test@132", "Test@123");
        testResetPassword(resetPasswordModel,retrofitAPI);
        testLogin(loginModel, retrofitAPI);
    }
}
