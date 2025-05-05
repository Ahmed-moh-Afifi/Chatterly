package com.example.chatterly.data.remote;

import com.example.chatterly.model.data.Chat;
import com.example.chatterly.model.data.StringListWrapper;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ChatsAPI {
    @POST("{userId}/Chats/{name}")
    Call<Chat> createChat(
            @Path("userId") String userId,
            @Path("name") String name,
            @Body StringListWrapper userIds
    );

    @GET("{userId}/Chats")
    Call<List<Chat>> getUserChats(
            @Path("userId") String userId
    );

    @GET("{userId}/Chats/{chatId}")
    Call<Chat> getChat(
            @Path("userId") String userId,
            @Path("chatId") int chatId
    );

    @POST("{userId}/Chats/{chatId}/Users/{addedUserId}/Add")
    Call<Void> addUserToChat(
            @Path("userId") String userId,
            @Path("chatId") int chatId,
            @Path("addedUserId") String addedUserId
    );

    @POST("{userId}/Chats/{chatId}/Users/{removedUserId}/Remove")
    Call<Void> removeUserFromChat(
            @Path("userId") String userId,
            @Path("chatId") int chatId,
            @Path("removedUserId") String removedUserId
    );

    @DELETE("{userId}/Chats/{chatId}")
    Call<Void> deleteChat(
            @Path("userId") String userId,
            @Path("chatId") int chatId
    );

    @GET("{userId}/Chats/{conversationInitiator}/And/{targetedGuy}")
    Call<Chat> getChatByParticipants(
            @Path("conversationInitiator") String conversationInitiator,
            @Path("targetedGuy") String targetedGuy
    );
}
