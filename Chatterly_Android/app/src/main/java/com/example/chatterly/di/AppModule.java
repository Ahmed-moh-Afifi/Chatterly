package com.example.chatterly.di;

import android.content.Context;

import com.example.chatterly.data.local.TokenManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class AppModule {
    @Provides
    @Singleton
    public static TokenManager provideTokenManager(@ApplicationContext Context context) {
        return new TokenManager(context);
    }
}
