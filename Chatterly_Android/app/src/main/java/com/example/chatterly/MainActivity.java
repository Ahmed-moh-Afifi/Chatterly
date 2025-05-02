package com.example.chatterly;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.ComponentActivity;

import com.example.chatterly.data.local.AuthenticationAPI;
import com.example.chatterly.data.local.TokenManager;
import com.example.chatterly.model.authentication.LoginModel;
import com.example.chatterly.model.authentication.RegisterModel;

import java.io.IOException;

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Add OkHttp logging
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
        RegisterModel registerModel = new RegisterModel("Ahmed", "Afifi", true, "leeh@mosaab.is-a.dev", "ahmedafifi", "Test@123");
        retrofitAPI.loginRequest(loginModel).enqueue(new Callback<LoginModel>() {
            @Override
            public void onResponse(Call<LoginModel> call, Response<LoginModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    LoginModel responseFromAPI = response.body();
                    String responseString = "Response Code : " + response.code() + response.message();
                    Log.d("API", responseString);
                } else {
                    Log.e("API", "Error: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<LoginModel> call, Throwable t) {
                Log.e("API", "Failed: " + t.getMessage());
            }
        });
        retrofitAPI.registerRequest(registerModel).enqueue(new Callback<RegisterModel>() {
            @Override
            public void onResponse(Call<RegisterModel> call, Response<RegisterModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    RegisterModel responseFromAPI = response.body();
                    String responseString = "Response Code : " + response.code() + response.message();
                    Log.d("API", responseString);
                } else {
                    Log.e("API", "Error: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<RegisterModel> call, Throwable t) {
                Log.e("API", "Failed: " + t.getMessage());
            }
        });
        retrofitAPI.loginRequest(loginModel).enqueue(new Callback<LoginModel>() {
            @Override
            public void onResponse(Call<LoginModel> call, Response<LoginModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    LoginModel responseFromAPI = response.body();
                    String responseString = "Response Code : " + response.code() + response.message();
                    Log.d("API", responseString);
                } else {
                    Log.e("API", "Error: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<LoginModel> call, Throwable t) {
                Log.e("API", "Failed: " + t.getMessage());
            }
        });
    }
}
