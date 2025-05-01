package com.example.chatterly;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.ComponentActivity;

import com.example.chatterly.data.local.TokenManager;
import com.example.chatterly.model.authentication.LoginModel;
import com.example.chatterly.model.authentication.TokensModel;
import com.example.chatterly.utils.Config;
import com.google.gson.Gson;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@AndroidEntryPoint
public class MainActivity extends ComponentActivity {
    @Inject
    TokenManager tokenManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Gson gson = new Gson();

        try {
            TokensModel tokensModel = tokenManager.readSavedTokens();
            if (tokensModel != null) {
                Log.d("MainActivity", "Saved Tokens " + gson.toJson(tokensModel));
            } else {
                Log.d("MainActivity", "Saved Tokens NULL");
            }

        } catch (Exception e) {
            Log.d("MainActivity", e.toString());
        }

        LoginModel loginModel = new LoginModel("ahmedafifi", "Test@123");
        RequestBody body = RequestBody.create(gson.toJson(loginModel), MediaType.get("application/json"));

        OkHttpClient httpClient = new OkHttpClient();
        Request request = new Request.Builder().url(Config.api + "Auth/Login").post(body).build();
        try {
//            CompletableFuture.supplyAsync(() -> {
//                try {
//                    Response response = httpClient.newCall(request).execute();
//                    Log.d("MainActivity", "Response code = " + response.code());
//                    return gson.fromJson(response.body().string(), TokensModel.class);
//                } catch (IOException e) {
//                    Log.d("MainActivity", e.toString());
//                }
//                return null;
//            }).thenApply((tokensModel) -> {
//                if (tokensModel != null) {
//                    try {
//                        tokenManager.saveTokens(tokensModel);
//                        Log.d("MainActivity", "Saved Tokens " + gson.toJson(tokenManager.readSavedTokens()));
//                    } catch (Exception e) {
//                        Log.d("MainActivity", e.toString());
//                    }
//                }
//                return tokensModel;
//            });

            tokenManager.getValidTokens().thenApply((tokensModel) -> {
                Log.d("MainActivity", "Saved Tokens " + gson.toJson(tokensModel));
                return tokensModel;
            });
        } catch (Exception e) {
            Log.d("MainActivity", e.toString());
        }
    }
}
