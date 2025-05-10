package com.example.chatterly.data.repository;

import android.util.Log;

import com.example.chatterly.data.local.TokenManager;
import com.example.chatterly.data.remote.UsersAPI;
import com.example.chatterly.model.data.User;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

import javax.inject.Inject;

public class AuthenticationRepository {
    TokenManager tokenManager;

    UsersAPI usersAPI;

    private User currentUser;

    @Inject
    public AuthenticationRepository(TokenManager tokenManager, UsersAPI usersAPI) {
        this.tokenManager = tokenManager;
        this.usersAPI = usersAPI;
    }

    public CompletableFuture<User> loadUser() {
        return tokenManager.getValidTokens().thenApply(tokensModel -> {
            Log.d("AuthenticationRepository", "Tokens model = " + (tokensModel == null ? "null" : tokensModel.getAccessToken()));
            if (tokensModel != null) {
                try {
                    currentUser = usersAPI.getUser(tokensModel.getIdFromAccessToken()).execute().body();
                } catch (IOException e) {
                    // Report to user...
                }
            }

            return currentUser;
        });
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void removeCurrentUserToken(){
        Log.d("AuthenticationRepository::removeCurrentUserToken", "Removing current user token (aka logout)");
        tokenManager.deleteTokens();
    }
}
