package com.example.chatterly.ui.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.chatterly.data.local.TokenManager;
import com.example.chatterly.data.remote.ChatsAPI;
import com.example.chatterly.data.remote.UsersAPI;
import com.example.chatterly.data.repository.AuthenticationRepository;
import com.example.chatterly.model.data.Chat;
import com.example.chatterly.model.data.User;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@HiltViewModel
public class MainViewModel extends ViewModel {
    private final ChatsAPI chatsAPI;
    private final UsersAPI usersAPI;
    private final TokenManager tokenManager;
    private final AuthenticationRepository authenticationRepository;

    private final MutableLiveData<List<Chat>> chats = new MutableLiveData<>();
    private final MutableLiveData<List<User>> searchResults = new MutableLiveData<>();
    private final MutableLiveData<Chat> userChat = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>(false);

    @Inject
    public MainViewModel(ChatsAPI chatsAPI, UsersAPI usersAPI, TokenManager tokenManager, AuthenticationRepository authenticationRepository) {
        this.chatsAPI = chatsAPI;
        this.usersAPI = usersAPI;
        this.tokenManager = tokenManager;
        this.authenticationRepository = authenticationRepository;
    }

    public LiveData<List<Chat>> getChats() {
        return chats;
    }

    public LiveData<List<User>> getSearchResults() {
        return searchResults;
    }

    public LiveData<Chat> getUserChat() {
        return userChat;
    }

    public LiveData<Boolean> getLoading() {
        return loading;
    }

    public void loadChats() {
        loading.setValue(true);
        tokenManager.getValidTokens().thenApply(tokensModel -> {
            chatsAPI.getUserChats(tokensModel.getIdFromAccessToken()).enqueue(new Callback<>() {
                @Override
                public void onResponse(Call<List<Chat>> call, Response<List<Chat>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Log.d("MainViewModel::loadChats", "Loaded chats successfully.");
                        chats.setValue(response.body());
                    } else {
                        Log.d("MainViewModel::loadChats", "Failed to load chats: Invalid response.");
                    }
                    loading.setValue(false);
                }

                @Override
                public void onFailure(Call<List<Chat>> call, Throwable t) {
                    Log.d("MainViewModel::loadChats", "Failed to load chats: " + t.getMessage());
                    loading.setValue(false);
                }
            });
            return tokensModel;
        });
    }

    public void searchUsers(String query) {
        usersAPI.searchUsers(query).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("MainViewModel::searchUsers", "Search returned " + response.body().size() + " users.");
                    searchResults.setValue(response.body());
                } else {
                    Log.d("MainViewModel::searchUsers", "Failed to search users: Invalid response.");
                    searchResults.setValue(List.of());
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Log.d("MainViewModel::searchUsers", "Failed to search users: " + t.getMessage());
                searchResults.setValue(List.of());
            }
        });
    }

    public void getChat(String userId) {
        chatsAPI.getChatByParticipants(authenticationRepository.getCurrentUser().getId(), userId).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<Chat> call, Response<Chat> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("MainViewModel::getChat", "Got chat successfully.");
                    userChat.setValue(response.body());
                } else {
                    Log.d("MainViewModel::createChat", "Failed to get chat: Invalid response.");
                    userChat.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<Chat> call, Throwable t) {
                Log.d("MainViewModel::getChat", "Failed to get chat: " + t.getMessage());
                userChat.setValue(null);
            }
        });
    }
}