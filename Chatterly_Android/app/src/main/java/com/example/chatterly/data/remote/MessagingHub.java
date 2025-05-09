package com.example.chatterly.data.remote;

import android.util.Log;

import com.example.chatterly.data.local.AuthenticationAPI;
import com.example.chatterly.data.local.TokenManager;
import com.example.chatterly.model.authentication.LoginModel;
import com.example.chatterly.model.authentication.TokensModel;
import com.example.chatterly.model.data.Message;
import com.example.chatterly.model.data.User;
import com.example.chatterly.utils.Config;
import com.google.gson.Gson;
import com.microsoft.signalr.HubConnection;
import com.microsoft.signalr.HubConnectionBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Callable;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.disposables.Disposable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MessagingHub {

    private TokenManager tokenManager;
    private final HubConnection hubConnection;
    private Runnable mainScreenListener;
    private final Map<Integer, Runnable> chatScreenListeners = new HashMap<>();
    private final HashSet<String> sentMessages = new HashSet<>();
    private Gson gson;

    @Inject
    public MessagingHub(TokenManager tokenManager, Gson gson) {
        this.tokenManager = tokenManager;
        this.gson = gson;
        hubConnection = HubConnectionBuilder.create(Config.api + "MessagingHub").withAccessTokenProvider(getAccessTokenProvider()).build();

        hubConnection.on("MessageReceived", (object) -> {
            String jsonString = gson.toJson(object);
            Message message = gson.fromJson(jsonString, Message.class);
            Log.d("SignalRHub", "Message received in chat: " + message.getChatId());
            Log.d("SignalRHub", jsonString);
            if (mainScreenListener != null) {
                mainScreenListener.run();
            }
            if (chatScreenListeners.containsKey(message.getChatId())) {
                Log.d("MessagingHub", "Chat listener found.");
                Objects.requireNonNull(chatScreenListeners.get(message.getChatId())).run();
                Log.d("MessagingHub", "Finished running chat listener.");
            } else {
                Log.d("MessagingHub", "Chat listener not found.");
            }
        }, Object.class);
    }

    public Disposable connect() {
        return hubConnection.start().subscribe(() -> Log.d("SignalRHub", "Connected to SignalR Hub"),
                throwable -> Log.e("SignalRHub", "Error connecting to SignalR Hub", throwable));
    }

    public void setMainScreenListener(Runnable listener) {
        this.mainScreenListener = listener;
    }

    public void setChatScreenListener(int chatId, Runnable listener) {
        chatScreenListeners.put(chatId, listener);
    }

    public void removeChatScreenListener(int chatId) {
        chatScreenListeners.remove(chatId);
    }

    public Completable subscribeToChats(List<String> chatIds) {
        return hubConnection.invoke("SubscribeToChats", chatIds);
    }

    private Single<String> getAccessTokenProvider() {
        return Single.create(emitter -> {
            tokenManager.getValidTokens().thenApply((tokensModel) -> {
                emitter.onSuccess(tokensModel.getAccessToken());
                return tokensModel;
            });
        });
    }

    public Completable sendMessage(Message message) {
        Log.d("MessagingHub::sendMessage", "Sending message: " + message.getBody() + " to chat with id: " + message.getChatId());
        String json = gson.toJson(message);
        Log.d("MessagingHub::sendMessage", "Sending message: " + json);
        sentMessages.add(message.getUid());
        return hubConnection.invoke("SendToChatAsync", json);
    }
}