package com.example.chatterly.di;

import android.content.Context;

import com.example.chatterly.data.local.AuthenticationAPI;
import com.example.chatterly.data.local.TokenManager;
import com.example.chatterly.data.remote.AuthenticatedHttpClient;
import com.example.chatterly.data.remote.ChatsAPI;
import com.example.chatterly.data.remote.MessagesAPI;
import com.example.chatterly.data.remote.MessagingHub;
import com.example.chatterly.data.remote.UsersAPI;
import com.example.chatterly.data.repository.AuthenticationRepository;
import com.example.chatterly.utils.Config;
import com.example.chatterly.utils.CustomDateAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
@InstallIn(SingletonComponent.class)
public class AppModule {
    @Provides
    @Singleton
    public static TokenManager provideTokenManager(@ApplicationContext Context context, OkHttpClient httpClient) {
        return new TokenManager(context, httpClient);
    }

    @Provides
    public static AuthenticatedHttpClient provideAuthenticatedHttpClient(TokenManager tokenManager) {
        return new AuthenticatedHttpClient(tokenManager);
    }

    @Provides
    public static OkHttpClient provideHttpClient() {
        OkHttpClient httpClient = new OkHttpClient();
        httpClient.newBuilder().addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY));
        return httpClient;
    }

    @Provides
    public static Gson provideGson() {
        return new GsonBuilder().registerTypeAdapter(java.util.Date.class, new CustomDateAdapter()).create();
    }

//    @Provides
//    public static Retrofit provideRetrofit(AuthenticatedHttpClient authenticatedHttpClient, Gson gson) {
//        return new Retrofit.Builder()
//                .baseUrl(Config.api)
//                .client(authenticatedHttpClient.getAuthenticatedHttpClient())
//                .addConverterFactory(GsonConverterFactory.create(gson))
//                .build();
//    }

    @Provides
    public static UsersAPI provideUsersAPI(AuthenticatedHttpClient authenticatedHttpClient, Gson gson) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.api)
                .client(authenticatedHttpClient.getAuthenticatedHttpClient())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        return retrofit.create(UsersAPI.class);
    }

    @Provides
    public static ChatsAPI provideChatsAPI(AuthenticatedHttpClient authenticatedHttpClient, Gson gson) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.api)
                .client(authenticatedHttpClient.getAuthenticatedHttpClient())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        return retrofit.create(ChatsAPI.class);
    }

    @Provides
    public static MessagesAPI provideMessagesAPI(AuthenticatedHttpClient authenticatedHttpClient, Gson gson) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.api)
                .client(authenticatedHttpClient.getAuthenticatedHttpClient())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        return retrofit.create(MessagesAPI.class);
    }

    @Provides
    public static AuthenticationAPI provideAuthenticationAPI(OkHttpClient okHttpClient, Gson gson) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.api)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        return retrofit.create(AuthenticationAPI.class);
    }

    @Provides
    @Singleton
    public static MessagingHub provideMessagingHub(TokenManager tokenManager, Gson gson) {
        return new MessagingHub(tokenManager, gson);
    }

    @Provides
    @Singleton
    public static AuthenticationRepository provideAuthenticationRepository(TokenManager tokenManager, UsersAPI usersAPI) {
        return new AuthenticationRepository(tokenManager, usersAPI);
    }
}
