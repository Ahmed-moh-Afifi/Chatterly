package com.example.chatterly.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.chatterly.R;
import com.example.chatterly.data.local.AuthenticationAPI;
import com.example.chatterly.data.local.TokenManager;
import com.example.chatterly.data.repository.AuthenticationRepository;
import com.example.chatterly.model.authentication.LoginModel;
import com.example.chatterly.model.authentication.TokensModel;

import androidx.appcompat.app.AppCompatActivity;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@AndroidEntryPoint
public class LoginActivity extends AppCompatActivity {

    @Inject
    AuthenticationAPI authenticationAPI;

    @Inject
    TokenManager tokenManager;

    @Inject
    AuthenticationRepository authenticationRepository;

    private EditText usernameEditText, passwordEditText;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        progressBar = findViewById(R.id.progressBar);

        findViewById(R.id.loginButton).setOnClickListener(v -> handleLogin());
        findViewById(R.id.registerTextView).setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }

    private void handleLogin() {
        String username = usernameEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        authenticationAPI.loginRequest(new LoginModel(username, password)).enqueue(new Callback<TokensModel>() {
            @Override
            public void onResponse(Call<TokensModel> call, Response<TokensModel> response) {
                tokenManager.saveTokens(response.body());
                authenticationRepository.loadUser().thenApply(user -> {
                    runOnUiThread(() -> {
                        if (user != null) {
                            Log.d("LoginActivity", "User does not equal null.");
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            Log.d("LoginActivity", "MainActivity started.");
                            finish();
                        }
                    });
                    return user;
                });
            }

            @Override
            public void onFailure(Call<TokensModel> call, Throwable t) {
                runOnUiThread(() -> {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(LoginActivity.this, "Login failed. Check your credentials.", Toast.LENGTH_SHORT).show();
                });
            }
        });

    }
}