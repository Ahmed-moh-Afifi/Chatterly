package com.example.chatterly.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

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

    private ImageButton backButton;
    private TextView chatName;
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

        backButton = findViewById(R.id.backToMain);
        chatName = findViewById(R.id.chatHeaderText);
        recyclerView = findViewById(R.id.chatRecyclerView);
        progressBar = findViewById(R.id.progressBar);
        messageEditText = findViewById(R.id.messageInput);
        sendButton = findViewById(R.id.sendButton);

        chat = gson.fromJson(getIntent().getStringExtra("chat"), Chat.class);
        chatName.setText(chat.getName());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        messageAdapter = new MessageAdapter(chat);
        recyclerView.setAdapter(messageAdapter);
        sendButton.setOnClickListener(v -> onSendButtonClicked());
        backButton.setOnClickListener(v -> onBackButtonClicked());

        chatViewModel = new ViewModelProvider(this).get(ChatViewModel.class);

        observeViewModel();

        chatViewModel.loadMessages(chat.getId());

        messagingHub.setChatScreenListener(chat.getId(), () -> {
            Log.d("ChatActivity", "Listener for chat " + chat.getId() + " is called");
            chatViewModel.loadMessages(chat.getId());
        }); // Reloading all messages for now. The callback should accept the message object and update the ui accordingly.

        recyclerView.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            if (messageAdapter.getItemCount() > 0 && isScrollNearToBottom())
                recyclerView.smoothScrollToPosition(messageAdapter.getItemCount() - 1);
        });
    }

    private boolean isScrollNearToBottom() {
        if (recyclerView.getAdapter() == null || recyclerView.getAdapter().getItemCount() == 0) {
            return true;
        }

        LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        assert layoutManager != null;
        int lastVisiblePosition = layoutManager.findLastCompletelyVisibleItemPosition();
        int totalItems = layoutManager.getItemCount();
        return totalItems - lastVisiblePosition <= 5;
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
        if (messageAdapter.getItemCount() == 0) {
            messageAdapter.setMessages(messages);
            recyclerView.scrollToPosition(messages.size() - 1);
        } else {
            messageAdapter.setMessages(messages);
            recyclerView.smoothScrollToPosition(messages.size() - 1);
        }
    }

    private void onSendButtonClicked() {
        Log.d("ChatActivity::onSendButtonClicked", "Sending message: " + messageEditText.getText().toString().trim());
        Message message = new Message(-1, messageEditText.getText().toString().trim(), new ArrayList<String>(), new ArrayList<String>(), new ArrayList<String>(), new Date(), authenticationRepository.getCurrentUser().getId(), 0, 0, chat.getId());
        chatViewModel.sendMessage(message);
        messageEditText.setText("");
    }

    private void onBackButtonClicked() {
        Log.d("ChatActivity::onBackButtonClicked", "Going back to MainActivity");
        finish();
    }
}