package com.example.chatterly.ui.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.chatterly.data.local.TokenManager;
import com.example.chatterly.data.remote.MessagesAPI;
import com.example.chatterly.data.remote.MessagingHub;
import com.example.chatterly.model.data.Message;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@HiltViewModel
public class ChatViewModel extends ViewModel {
    MessagesAPI messagesAPI;
    TokenManager tokenManager;
    MessagingHub messagingHub;

    @Inject
    public ChatViewModel(MessagesAPI messagesAPI, TokenManager tokenManager, MessagingHub messagingHub) {
        this.messagesAPI = messagesAPI;
        this.tokenManager = tokenManager;
        this.messagingHub = messagingHub;
    }

    private final MutableLiveData<List<Message>> messages = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>(false);

    public LiveData<List<Message>> getMessages() {
        return messages;
    }

    public LiveData<Boolean> getLoading() {
        return loading;
    }

    public void loadMessages(int chatId) {
        loading.setValue(true);
        // Load messages and set the messages property...
        tokenManager.getValidTokens().thenApply(tokensModel -> {
            messagesAPI.getChatMessages(tokensModel.getIdFromAccessToken(), chatId).enqueue(new Callback<>() {
                @Override
                public void onResponse(Call<List<Message>> call, Response<List<Message>> response) {
                    messages.setValue(response.body());
                    loading.setValue(false);
                }

                @Override
                public void onFailure(Call<List<Message>> call, Throwable t) {
                    // Report to user.
                }
            });
            return tokensModel;
        });
    }

    public void sendMessage(Message message) {
        messagingHub.sendMessage(message);
    }
}
