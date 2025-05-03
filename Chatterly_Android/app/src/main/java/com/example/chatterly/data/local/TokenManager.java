package com.example.chatterly.data.local;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;

import com.example.chatterly.model.authentication.TokensModel;
import com.example.chatterly.utils.Config;
import com.google.gson.Gson;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.concurrent.CompletableFuture;

import javax.inject.Inject;

import dagger.hilt.android.qualifiers.ApplicationContext;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

public class TokenManager {
    private final String baseUrl = Config.api + "Auth/";

    OkHttpClient httpClient;

    private TokensModel tokensModel;
    private CompletableFuture<TokensModel> refreshTokensFuture;

    private final Context context;

    @Inject
    public TokenManager(@ApplicationContext Context context, OkHttpClient httpClient) {
        this.context = context;
        this.httpClient = httpClient;
    }

    public TokensModel readSavedTokens() {
        Log.d("TokenManager", "Reading saved tokens...");
        try {
            MasterKey masterKey = new MasterKey.Builder(context).setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build();
            SharedPreferences securePrefs = EncryptedSharedPreferences.create(context, "secure_prefs", masterKey, EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM);
            String jsonString = securePrefs.getString("tokens-model", null);
            tokensModel = null;
            if (jsonString != null) {
                Gson gson = new Gson();
                tokensModel = gson.fromJson(jsonString, TokensModel.class);
            }
            return tokensModel;
        } catch (GeneralSecurityException e) {
            // throw appropriate exception.
        } catch (IOException e) {
            // throw appropriate exception.
        }
        return null;
    }

    public void saveTokens(TokensModel tokensModel) {
        Log.d("TokenManager", "Saving tokens...");
        try {
            MasterKey masterKey = new MasterKey.Builder(context).setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build();
            SharedPreferences securePrefs = EncryptedSharedPreferences.create(context, "secure_prefs", masterKey, EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM);

            Gson gson = new Gson();
            String jsonString = gson.toJson(tokensModel);
            securePrefs.edit().putString("tokens-model", jsonString).apply();
            this.tokensModel = tokensModel;
        } catch (GeneralSecurityException e) {
            // throw appropriate exception.
        } catch (IOException e) {
            // throw appropriate exception.
        }
    }

    public void deleteTokens() {
        Log.d("TokenManager", "Saving tokens...");
        try {
            MasterKey masterKey = new MasterKey.Builder(context).setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build();
            SharedPreferences securePrefs = EncryptedSharedPreferences.create(context, "secure_prefs", masterKey, EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM);
            securePrefs.edit().remove("tokens-model").apply();
            tokensModel = null;
        } catch (GeneralSecurityException e) {
            // throw appropriate exception.
        } catch (IOException e) {
            // throw appropriate exception.
        }
    }

    private CompletableFuture<TokensModel> internalRefreshTokens() {
        Log.d("TokenManager", "Requesting new tokens...");
        Gson gson = new Gson();
        RequestBody body = RequestBody.create(gson.toJson(tokensModel), MediaType.get("application/json"));
        return CompletableFuture.supplyAsync(() -> {
            Request request = new Request.Builder().url(baseUrl + "Refresh").post(body).build();
            try {
                try (Response response = httpClient.newCall(request).execute()) {
                    if (response.code() == 200) {
                        return gson.fromJson(response.body().string(), TokensModel.class);
                    }
                }
            } catch (IOException e) {

            }
            return null;
        }).thenApply((tokensModel) -> {
            if (tokensModel != null) {
                saveTokens(tokensModel);
            }
            else {
                // Session expired
                Log.d("TokenManager", "Session expired, deleting tokens...");
                deleteTokens();
                return null;
            }
            return tokensModel;
        });
    }

    CompletableFuture<TokensModel> refreshTokens() {
        Log.d("TokenManager", "Refreshing tokens...");
        if (refreshTokensFuture == null || refreshTokensFuture.isDone()) {
            refreshTokensFuture = internalRefreshTokens();

        }
        return refreshTokensFuture;
    }

    public Boolean hasValidTokens() {
        Log.d("TokenManager", "Checking for valid tokens...");
        if (tokensModel == null) {
            readSavedTokens();
        }

        if (tokensModel == null) {
            return false;
        }

        return !(tokensModel.isAccessTokenExpired());
    }

    public CompletableFuture<TokensModel> getValidTokens() {
        if (hasValidTokens()) {
            return CompletableFuture.supplyAsync(() -> tokensModel);
        }

        return refreshTokens();
    }
}
