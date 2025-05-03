package com.example.chatterly.data.remote;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.chatterly.data.local.TokenManager;

import java.io.IOException;

import javax.inject.Inject;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

public class AuthenticatedHttpClient {
    private final TokenManager tokenManager;
    private final OkHttpClient authenticatedHttpClient;

    @Inject
    public AuthenticatedHttpClient(TokenManager tokenManager) {
        this.tokenManager = tokenManager;
        this.authenticatedHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @NonNull
                    @Override
                    public Response intercept(@NonNull Chain chain) throws IOException {
                        Request unAuthenticatedRequest = chain.request();
                        String authHeader = "Bearer " + tokenManager.getValidTokens().join().getAccessToken();
                        Log.d("AuthenticatedHttpClient", authHeader);
                        Request authenticatedRequest = unAuthenticatedRequest.newBuilder()
                                .header("Authorization", authHeader)
                                .build();
                        return chain.proceed(authenticatedRequest);
                    }
                })
                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .build();
    }

    public OkHttpClient getAuthenticatedHttpClient() {
        return authenticatedHttpClient;
    }
}