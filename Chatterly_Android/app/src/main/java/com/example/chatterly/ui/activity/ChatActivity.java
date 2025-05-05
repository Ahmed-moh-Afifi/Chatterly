package com.example.chatterly.ui.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.example.chatterly.R;
import com.example.chatterly.data.remote.MessagingHub;
import com.example.chatterly.data.repository.AuthenticationRepository;
import com.example.chatterly.model.data.Chat;
import com.example.chatterly.model.data.Message;
import com.example.chatterly.ui.adapter.MessageAdapter;
import com.example.chatterly.ui.viewmodel.ChatViewModel;
import com.google.gson.Gson;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ChatActivity extends AppCompatActivity {

    @Inject
    MessagingHub messagingHub;

    @Inject
    AuthenticationRepository authenticationRepository;

    @Inject
    Gson gson;

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private ChatViewModel chatViewModel;
    private MessageAdapter messageAdapter;
    private EditText messageEditText;
    private Button sendButton;
    private Chat chat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        recyclerView = findViewById(R.id.chatRecyclerView);
        progressBar = findViewById(R.id.progressBar);
        messageEditText = findViewById(R.id.messageInput);
        sendButton = findViewById(R.id.sendButton);

        chat = gson.fromJson(getIntent().getStringExtra("chat"), Chat.class);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        messageAdapter = new MessageAdapter(chat);
        recyclerView.setAdapter(messageAdapter);
        sendButton.setOnClickListener(v -> onSendButtonClicked());

        chatViewModel = new ViewModelProvider(this).get(ChatViewModel.class);

        observeViewModel();

        chatViewModel.loadMessages(chat.getId());

        messagingHub.setChatScreenListener(chat.getId(), () -> chatViewModel.loadMessages(chat.getId())); // Reloading all messages for now. The callback should accept the message object and update the ui accordingly.
    }

    private void observeViewModel() {
        chatViewModel.getMessages().observe(this, this::updateMessages);
        chatViewModel.getLoading().observe(this, loading -> {
            if (loading) {
                progressBar.setVisibility(View.VISIBLE);
            } else {
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void updateMessages(List<Message> messages) {
        messageAdapter.setMessages(messages);
    }

    private void onSendButtonClicked() {
        Log.d("ChatActivity::onSendButtonClicked", "Sending message: " + messageEditText.getText().toString());
        Message message = new Message(-1, messageEditText.getText().toString(), new ArrayList<String>(), new ArrayList<String>(), new ArrayList<String>(), new Date(), authenticationRepository.getCurrentUser().getId(), 0, 0, chat.getId());
        chatViewModel.sendMessage(message);
    }
}