package com.example.chatterly.data.remote;

import com.example.chatterly.model.data.Message;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface MessagesAPI {
    @GET("Messages/{userId}/Chats/{chatId}/Messages")
    Call<List<Message>> getChatMessages(
            @Path("userId") String userId,
            @Path("chatId") int chatId
    );

    @GET("Messages/{userId}/Chats/{chatId}/Messages/{messageId}")
    Call<Message> getChatMessage(
            @Path("userId") String userId,
            @Path("chatId") int chatId,
            @Path("messageId") int messageId
    );

    @GET("Messages/{userId}/Chats/{chatId}/Messages/{messageId}/Replies")
    Call<List<Message>> getMessageReplies(
            @Path("userId") String userId,
            @Path("chatId") int chatId,
            @Path("messageId") int messageId
    );
}