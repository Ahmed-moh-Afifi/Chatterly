package com.example.chatterly.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatterly.R;
import com.example.chatterly.data.remote.MessagingHub;
import com.example.chatterly.data.repository.AuthenticationRepository;
import com.example.chatterly.model.data.Chat;
import com.example.chatterly.model.data.User;
import com.example.chatterly.ui.adapter.ChatAdapter;
import com.example.chatterly.ui.viewmodel.MainViewModel;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {

    @Inject
    MessagingHub messagingHub;

    @Inject
    Gson gson;

    @Inject
    AuthenticationRepository authenticationRepository;

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private MainViewModel mainViewModel;
    private ChatAdapter chatAdapter;

    private EditText searchBar;
    private ListView searchResults;
    private ArrayAdapter<String> searchAdapter;
    private List<User> foundUsers = new ArrayList<>();
    private Handler handler = new Handler(Looper.getMainLooper());
    private User lastSelectedUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("MainActivity::onCreate", "Called");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progressBar);
        searchBar = findViewById(R.id.searchBar);
        searchResults = findViewById(R.id.searchResults);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        chatAdapter = new ChatAdapter(chat -> {
            Intent intent = new Intent(MainActivity.this, ChatActivity.class);
            intent.putExtra("chat", gson.toJson(chat));
            startActivity(intent);
        });
        recyclerView.setAdapter(chatAdapter);

        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);

        observeViewModel();
        mainViewModel.loadChats();

        messagingHub.setMainScreenListener(() -> mainViewModel.loadChats()); // Reload all chats

        setupSearchBar();
    }

    private void setupSearchBar() {
        searchAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        searchResults.setAdapter(searchAdapter);

        searchBar.addTextChangedListener(new android.text.TextWatcher() {
            private Runnable searchTask;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (searchTask != null) {
                    handler.removeCallbacks(searchTask);
                }
                searchTask = () -> {
                    String query = s.toString().trim();
                    if (!query.isEmpty()) {
                        mainViewModel.searchUsers(query);
                    } else {
                        searchResults.setVisibility(View.GONE);
                    }
                };
                handler.postDelayed(searchTask, 1000);
            }

            @Override
            public void afterTextChanged(android.text.Editable s) {}
        });

        searchResults.setOnItemClickListener((AdapterView<?> parent, View view, int position, long id) -> {
            User selectedUser = foundUsers.get(position);
            lastSelectedUser = selectedUser;
            mainViewModel.getChat(selectedUser.getId());
        });
    }

    private void observeViewModel() {
        mainViewModel.getChats().observe(this, this::updateChats);
        mainViewModel.getLoading().observe(this, loading -> {
            if (loading) {
                progressBar.setVisibility(View.VISIBLE);
            } else {
                progressBar.setVisibility(View.GONE);
            }
        });
        mainViewModel.getSearchResults().observe(this, users -> {
            foundUsers.clear();
            foundUsers.addAll(users);
            searchAdapter.clear();
            for (User user : users) {
                searchAdapter.add(user.getUserName());
            }
            searchResults.setVisibility(users.isEmpty() ? View.GONE : View.VISIBLE);
        });
        mainViewModel.getUserChat().observe(this, chat -> {
            if (chat != null) {
                Log.d("MainActivity::userChatObserver", "Got chat with id = " + chat.getId());
                mainViewModel.loadChats();
                ArrayList<User> users = new ArrayList<>();
                users.add(lastSelectedUser);
                users.add(authenticationRepository.getCurrentUser());
                chat.setUsers(users);
                Intent intent = new Intent(MainActivity.this, ChatActivity.class);
                intent.putExtra("chat", gson.toJson(chat));
                startActivity(intent);
            }
        });
    }

    private void updateChats(List<Chat> chats) {
        Log.d("MainActivity::updateChats", "Chats count = " + chats.size());
        chatAdapter.setChats(chats);
    }
}