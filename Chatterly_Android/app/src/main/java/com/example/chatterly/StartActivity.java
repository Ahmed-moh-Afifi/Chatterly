package com.example.chatterly;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.chatterly.data.remote.ChatsAPI;
import com.example.chatterly.data.remote.MessagingHub;
import com.example.chatterly.data.repository.AuthenticationRepository;
import com.example.chatterly.model.data.Chat;
import com.example.chatterly.ui.activity.LoginActivity;
import com.example.chatterly.ui.activity.MainActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class StartActivity extends AppCompatActivity {

    @Inject
    AuthenticationRepository authenticationRepository;

    @Inject
    ChatsAPI chatsAPI;

    @Inject
    MessagingHub messagingHub;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("StartActivity::onCreate", "Called");
        super.onCreate(savedInstanceState);

        authenticationRepository.loadUser().thenApply(user -> {
            if (user == null) {
                // Redirect to LoginActivity
                Log.d("StartActivity", "User equals null");
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
            } else {
                // Redirect to MainActivity
                Log.d("StartActivity", "Subscribing to chats");
                try {
                    List<Chat> chats = chatsAPI.getUserChats(user.getId()).execute().body();
                    List<String> chatIds = new ArrayList<>();
                    for (Chat chat: chats) {
                        chatIds.add(String.valueOf(chat.getId()));
                    }
                    messagingHub.subscribeToChats(chatIds).blockingAwait();
                } catch (IOException e) {
                    // Report to user.
                }
                Log.d("StartActivity", "User does not equal null, starting MainActivity");
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            }

            return user;
        });

        // Close this activity to prevent returning to it
        finish();
    }
}